package org.vfast.backrooms.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.BackroomsBlocks;
import org.vfast.backrooms.blocks.ProximityLightBlock;
import org.vfast.backrooms.world.BackroomsGameRules;

import java.util.List;

public class ProximityLightBlockEntity extends BlockEntity {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public ProximityLightBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BackroomsBlockEntities.PROXI_LIGHT_ENTITY, worldPosition, blockState);
    }

    public static void tick(final Level level, final BlockPos pos, final BlockState selfState, final ProximityLightBlockEntity entity) {
        if (!level.isClientSide()) {
            if (entity.shouldTick((ServerLevel) level, pos)) {
                boolean entityNear = entity.isEntityNearby((ServerLevel) level, pos);
                boolean neighborBlock = entity.hasNeighboursEntity((ServerLevel) level, pos);
                boolean actualState = entityNear || neighborBlock;
                if (level.getBlockState(pos).getValueOrElse(LIT, false) != actualState) {
                    BlockState newState = selfState.cycle(LIT);
                    level.setBlockAndUpdate(pos, newState);
                    entity.playSound(null, level, pos, actualState);
                }
            } else {
                if (level.getBlockState(pos).getValueOrElse(LIT, false)) {
                    BlockState newState = selfState.cycle(LIT);
                    level.setBlockAndUpdate(pos, newState);
                }
            }
        }
    }

    public boolean isEntityNearby(ServerLevel level, BlockPos blockPos) {
        AABB detectionZone = new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1).inflate(ProximityLightBlock.PROXIMITY_RANGE / 2);
        List<LivingEntity> detectedEntities = level.getEntitiesOfClass(LivingEntity.class, detectionZone);
        return !detectedEntities.isEmpty();
    }

    public boolean shouldTick(ServerLevel level, BlockPos blockPos) {
        int maxChain = level.getGameRules().get(BackroomsGameRules.PROXI_LIGHT_CHAIN);
        AABB detectionZone = new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1).inflate(ProximityLightBlock.PROXIMITY_RANGE + maxChain);
        List<LivingEntity> detectedEntities = level.getEntitiesOfClass(LivingEntity.class, detectionZone);
        return !detectedEntities.isEmpty();
    }

    public double entityDistance(BlockPos pos1, BlockPos pos2) {
        int x = pos2.getX() - pos1.getX();
        int y = pos2.getY() - pos1.getY();
        int z = pos2.getZ() - pos1.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double closestEntityDistance(BlockPos source) {
        AABB detectionZone = new AABB(source.getX(), source.getY(), source.getZ(), source.getX() + 1, source.getY() + 1, source.getZ() + 1).inflate(ProximityLightBlock.PROXIMITY_RANGE / 2);
        List<LivingEntity> detectedEntities = level.getEntitiesOfClass(LivingEntity.class, detectionZone);
        double minDist = ProximityLightBlock.PROXIMITY_RANGE / 2;
        for (LivingEntity entity : detectedEntities) {
            double entDist = this.entityDistance(source, entity.blockPosition());
            minDist = Math.min(entDist, minDist);
        }

        return minDist;
    }

    private @Nullable Direction getChainDirection(ServerLevel level, BlockPos origin) {
        if (level.getBlockState(origin.north()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.NORTH;
        } else if (level.getBlockState(origin.south()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.SOUTH;
        } else if (level.getBlockState(origin.east()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.EAST;
        } else if (level.getBlockState(origin.west()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.WEST;
        } else if (level.getBlockState(origin.above()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.UP;
        } else if (level.getBlockState(origin.below()).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
            return Direction.DOWN;
        }
        return null;
    }

    public boolean hasNeighboursEntity(ServerLevel level, BlockPos pos) {
        int maxChain = level.getGameRules().get(BackroomsGameRules.PROXI_LIGHT_CHAIN);
        Direction chainDirection = this.getChainDirection(level, pos);

        if (chainDirection != null) {
            for (int i = 0; i <= maxChain; i++) {
                BlockPos iPos = pos.relative(chainDirection, i);
                BlockState state = level.getBlockState(iPos);
                if (state.getBlock() == BackroomsBlocks.PROXI_LIGHT) {
                    if (this.isEntityNearby(level, iPos)) {
                        return true;
                    }
                } else i = maxChain + 1;
            }

            for (int i = 0; i <= maxChain; i++) {
                BlockPos iPos = pos.relative(chainDirection.getOpposite(), i);
                if (level.getBlockState(iPos).getBlock() == BackroomsBlocks.PROXI_LIGHT) {
                    if (this.isEntityNearby(level, iPos)) {
                        return true;
                    }
                } else i = maxChain + 1;
            }

            return false;
        } else return false;
    }

    protected void playSound(final @Nullable Player player, final LevelAccessor level, final BlockPos pos, final boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getSound(pressed), SoundSource.BLOCKS);
    }

    protected SoundEvent getSound(final boolean pressed) {
        return pressed ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
