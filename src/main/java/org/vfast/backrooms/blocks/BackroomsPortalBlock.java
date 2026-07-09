package org.vfast.backrooms.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.interfaces.LevelPortal;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.Set;

public class BackroomsPortalBlock extends HorizontalDirectionalBlock implements LevelPortal {
    public static final MapCodec<BackroomsPortalBlock> CODEC = simpleCodec(BackroomsPortalBlock::new);

    public static final BooleanProperty OVERWORLD = BooleanProperty.create("overworld");

    public BackroomsPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OVERWORLD, true));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        LevelPortal.super.entityInside(state, level, pos, entity);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.gameMode() == GameType.CREATIVE) {
            BlockState newState = state.cycle(OVERWORLD);
            level.setBlock(pos, newState, 3);
            InteractionHand hand = player.swingingArm != null ? player.swingingArm : InteractionHand.MAIN_HAND;
            player.swing(hand);
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(OVERWORLD, true);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OVERWORLD);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 0;
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        assert entity.isAlive();
        this.prepareEntity(entity, true);

        boolean fromLevel = currentLevel.dimension() == BackroomsLevels.LEVEL_0;
        ResourceKey<Level> newDimension = fromLevel ? Level.OVERWORLD : BackroomsLevels.LEVEL_0;
        ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
        if (newLevel != null) {
            LevelPortal.SpawnLocation spawnLoc;
            if (!fromLevel) {
                // going to level 0
                spawnLoc = this.selectStartPosition(portalEntryPos, newLevel, BackroomsBlocks.BACKROOMS_PORTAL.defaultBlockState().setValue(OVERWORLD, false));

                if (entity instanceof ServerPlayer) {
                    ServerPlayer.RespawnConfig respawn = LevelPortal.getSpawnConfig(spawnLoc.position(), spawnLoc.yRot(), spawnLoc.xRot(), newDimension);
                    ((ServerPlayer) entity).setRespawnPosition(respawn, false);
                }
            } else {
                BlockPos newPos = newLevel.getRespawnData().pos();
                float pitch = newLevel.getRespawnData().pitch();
                float yaw = newLevel.getRespawnData().yaw();

                if (entity instanceof ServerPlayer) {
                    ServerPlayer.RespawnConfig respawn = ((ServerPlayer) entity).getRespawnConfig();
                    assert respawn != null;

                    newPos = respawn.respawnData().pos();
                    pitch = respawn.respawnData().pitch();
                    yaw = respawn.respawnData().yaw();
                }

                spawnLoc = new LevelPortal.SpawnLocation(newPos, pitch, yaw);
            }

            this.prepareEntity(entity, false);
            return new TeleportTransition(newLevel, spawnLoc.position().getBottomCenter(), Vec3.ZERO, spawnLoc.yRot(), spawnLoc.xRot(), Set.of(), LevelPortal::affectPlayer);
        } else {
            this.prepareEntity(entity, false);
            return null;
        }
    }

    @Override
    public BlockState suffocatingBlock(BlockState blockState) {
        boolean isConcrete = blockState.getValue(OVERWORLD);
        if (isConcrete) {
            return BackroomsBlocks.STAINED_CONCRETE.defaultBlockState();
        } else {
            return BackroomsBlocks.MOIST_SILK.defaultBlockState();
        }
    }

    private void prepareEntity(Entity entity, boolean ongoing) {
        if (entity instanceof LivingEntity) {
            LevelPortal.prepareEntity((LivingEntity) entity, ongoing);
        }
    }

    @Override
    public Block invalidBlock() {
        return BackroomsBlocks.MOIST_SILK;
    }

    @Override
    public Transition getLocalTransition() {
        return Transition.NONE;
    }
}
