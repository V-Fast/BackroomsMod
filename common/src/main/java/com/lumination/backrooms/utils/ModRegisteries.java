package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.items.ModItemsClient;
import com.lumination.backrooms.items.ModItemsServer;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

public class ModRegisteries {
    public static void registerMod(boolean client) {
        ModBlocks.registerModBlock();
        ModBlockEntities.registerBlockEntities();
        ModSounds.registerSoundEvents();
        BackroomDimensions.registerDims();
        ModItems.registerModItems();
        ModEntities.registerMobs();

        if (client) {
            // client only

            ModItemsClient.registerModItems();
            BackroomsSettings.loadConfig();
            // KeyInputHandler.register();
            BackroomsModClient.setStartDate();
            Discord.initDiscord();
            BackroomsRPC.loadingRpc();

            registerEvents();
        } else {
            // server only
            ModItemsServer.registerModItems();
        }
    }

    private static void registerEvents() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((clientPlayer) -> {
            if (clientPlayer.getServer() != null) {
                MinecraftServer server = clientPlayer.getServer();
                BackroomsRPC.customLabelRpc("Playing on " + server.getName(), server.getCurrentPlayerCount(), server.getMaxPlayerCount());
            } else {
                BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
            }
            BackroomsModClient.setStartDate();
        });

        ClientTickEvent.CLIENT_POST.register((minecraftClient) -> {
            if (minecraftClient.getServer() != null) {
                IntegratedServer server = minecraftClient.getServer();
                BackroomsRPC.customLabelRpc("Playing on " + server.getName(), server.getCurrentPlayerCount(), server.getMaxPlayerCount());
            } else {
                BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
            }
        });

        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((clientPlayer) -> {
            Discord.setPresence("On the title screen", "", "mod");
            BackroomsModClient.setStartDate();
        });
    }
}
