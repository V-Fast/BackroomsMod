package com.lumination.backrooms.utils;

import com.lumaa.libu.LibuLib;
import com.lumaa.libu.update.ModrinthMod;
import com.lumaa.libu.update.UpdateChecker;
import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.screens.MissingLibuScreen;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.items.ModItemsClient;
import com.lumination.backrooms.items.ModItemsServer;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;

public class ModRegisteries {
    private static final boolean serverEntity = false;

    public static void registerMod(boolean client) {
        if (client) {
            if (FabricLoader.getInstance().isModLoaded("libu")) {
                try {
                    UpdateChecker backroomsUpdate = new UpdateChecker(new ModrinthMod("The Backrooms", "backrooms", BackroomsModClient.versionId));
                    backroomsUpdate.findLatestVersion();
                    LibuLib.addUpdate(backroomsUpdate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                BackroomsMod.print("Missing Libu!");
                MinecraftClient.getInstance().setScreen(new MissingLibuScreen(Text.empty()));
                return;
            }

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
            HudRenderCallback.EVENT.register(BackroomsModClient.camHud);
        } else {
            ModBlocks.registerModBlock();
            ModBlockEntities.registerBlockEntities();
            ModSounds.registerSoundEvents();
            BackroomDimensions.registerDims();
            ModItems.registerModItems();

            // server only
            ModItemsServer.registerModItems();
        }

        if (serverEntity) {
            ModEntities.registerMobs();
        }
    }
}
