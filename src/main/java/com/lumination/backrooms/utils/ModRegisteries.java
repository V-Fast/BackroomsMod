package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.items.ModItemsClient;
import com.lumination.backrooms.items.ModItemsServer;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;

public class ModRegisteries {
    public static void registerMod(boolean client) {
        if (client) {
            ModBlocks.registerModBlock();
            ModBlockEntities.registerBlockEntities();
            ModSounds.registerSoundEvents();
            BackroomDimensions.registerDims();
            ModItems.registerModItems();

            // client only
            ModItemsClient.registerModItems();
            BackroomsSettings.loadConfig();
            KeyInputHandler.register();
            BackroomsModClient.setStartDate();
            Discord.initDiscord();
            BackroomsRPC.loadingRpc();

            registerEvents();
        } else {
            if (!FabricLoader.getInstance().isModLoaded("libu")) {
                BackroomsMod.print("Missing Libu!");
            }

            ModBlocks.registerModBlock();
            ModBlockEntities.registerBlockEntities();
            ModSounds.registerSoundEvents();
            BackroomDimensions.registerDims();
            ModItems.registerModItems();

            // server only
            ModItemsServer.registerModItems();
        }
    }

    private static void registerEvents() {
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (handler.getServerInfo() != null) {
                BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getMaxPlayerCount());
            } else {
                BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
            }
            BackroomsModClient.setStartDate();
        });

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            if (handler.getServerInfo() != null) {
                BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getMaxPlayerCount());
            } else {
                BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            Discord.setPresence("On the title screen", "", "mod");
            BackroomsModClient.setStartDate();
        });
    }
}
