package org.vfast.backrooms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.attachments.BackroomsAttachments;
import org.vfast.backrooms.interfaces.LevelPortal;
import org.vfast.backrooms.world.BackroomsLevels;

public class ThresholdBlock extends HorizontalDirectionalBlock implements LevelPortal {
    protected ThresholdBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        assert entity.isAlive();
        LevelPortal.prepareEntity((LivingEntity) entity, true);

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
