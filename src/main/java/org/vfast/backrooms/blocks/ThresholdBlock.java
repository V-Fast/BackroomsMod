package org.vfast.backrooms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.attachments.BackroomsAttachments;
import org.vfast.backrooms.interfaces.LevelPortal;
import org.vfast.backrooms.items.BackroomsItems;
import org.vfast.backrooms.world.BackroomsLevels;
import org.vfast.backrooms.world.ThresholdTeleporter;

/**
 * Only a player holding a {@link BackroomsItems#KEYCARD} can use it, by right clicking.
 * The teleportation is delayed, see {@link ThresholdTeleporter}.
 */
public class ThresholdBlock extends HorizontalDirectionalBlock implements LevelPortal {
    protected ThresholdBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.getItem() != BackroomsItems.KEYCARD) {
            return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
        }

        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            ThresholdTeleporter.start(serverPlayer, serverLevel, pos);
        }

        return InteractionResult.SUCCESS;
    }

    /** Not triggered by walking into the block: only {@link ThresholdTeleporter} calls this, and only with players */
    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        assert entity.isAlive();
        LevelPortal.prepareEntity(entity, true);

        boolean fromLevel = BackroomsLevels.isBackrooms(currentLevel.dimension());
        ResourceKey<Level> newDimension = fromLevel ? Level.OVERWORLD : BackroomsLevels.LEVEL_0;
        ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
        if (newLevel != null) {
            LevelPortal.SpawnLocation spawnLoc;
            if (!fromLevel) {
                // going to level 0
                spawnLoc = this.selectStartPosition(portalEntryPos, newLevel, null);
            } else {
                // going back to overworld
                BlockPos newPos = newLevel.getRespawnData().pos();
                float pitch = newLevel.getRespawnData().pitch();
                float yaw = newLevel.getRespawnData().yaw();

                BlockPos respawn = entity.getAttached(BackroomsAttachments.SAVED_SPAWN);
                if (respawn != null) {
                    newPos = respawn;
                }

                spawnLoc = new LevelPortal.SpawnLocation(newPos, pitch, yaw);
            }

            return this.returnTransition(newLevel, entity, spawnLoc.position(), spawnLoc.yRot(), spawnLoc.xRot());
        } else {
            LevelPortal.prepareEntity(entity, false);
            return null;
        }
    }

    @Override
    public Block invalidBlock() {
        return null;
    }

    @Override
    public BlockState suffocatingBlock(BlockState currentState) {
        return null;
    }
}
