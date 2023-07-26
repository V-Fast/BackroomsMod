package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class ModRegistries {
    public static void registerMod(boolean client) throws IOException {
        ModBlocks.registerModBlock();
        ModBlockEntities.registerBlockEntities();
        ModSounds.registerSoundEvents();
        BackroomDimensions.registerDims();
        ModItems.registerModItems();
        ModEntities.registerMobs();

        if (client) {
            // Client Only
            FabricLoader.getInstance().getModContainer(BackroomsMod.MOD_ID).ifPresent(modContainer -> {
                ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(BackroomsMod.MOD_ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
            });

            ModItemsClient.registerModItems();
            BackroomsSettings.loadConfig();
            // KeyInputHandler.register();
            BackroomsModClient.setStartDate();
//            Discord.library();
//            BackroomsRPC.loadingRpc();

//            MobRegistries.registerEvents();
        } else {
            // Server Only
            ModItemsServer.registerModItems();
        }
    }

    private static void registerEvents() {
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (handler.getServerInfo() != null) {
                try {
                    BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getMaxPlayerCount());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            BackroomsModClient.setStartDate();
        });

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            if (handler.getServerInfo() != null) {
                try {
                    BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getMaxPlayerCount());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            try {
                Discord.setPresence("On the title screen", "", "mod");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BackroomsModClient.setStartDate();
        });
    }
}
