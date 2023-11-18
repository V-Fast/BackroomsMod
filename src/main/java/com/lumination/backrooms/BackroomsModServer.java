package com.lumination.backrooms;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.DedicatedServerOnly;
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer;

@DedicatedServerOnly
public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer(ModContainer mod) {
        // Server Only
        /*EntityWorldChangeEvents.AFTER_PLAYER_WORLD_CHANGE.register((player, origin, destination) -> {
            if (destination.getDimension() == BackroomDimensions.LEVEL_ZERO.getDimensionTypeSupplier().get()) {
                BlockPos spawnPos = null;
                Random rand = destination.getRandom();
                while (spawnPos == null) {
                    BlockPos randPos = new BlockPos(rand.nextBetween(player.getBlockX()-50, player.getBlockX()+50))
                }
            }
        });*/
    }
}
