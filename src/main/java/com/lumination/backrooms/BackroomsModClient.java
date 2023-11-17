package com.lumination.backrooms;

import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.entities.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

import java.io.IOException;
import java.util.Date;

@ClientOnly
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static long start;

    @Override
    public void onInitializeClient(ModContainer mod) {
        // Client Only
        QuiltLoader.getModContainer(BackroomsMod.MOD_ID).ifPresent(modContainer -> {
            ResourceLoader.registerBuiltinResourcePack(new Identifier(BackroomsMod.MOD_ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
        });
        BackroomsSettings.loadConfig();
        ModEntities.registerRenderers();
        // KeyInputHandler.register();
        BackroomsModClient.setStartDate();
//            Discord.library();
//            BackroomsRPC.loadingRpc();

//            MobRegistries.registerEvents();
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);
        BackroomsMod.print("Initialized Client Backrooms");
    }

    private static void registerEvents() {
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (handler.getServerInfo() != null) {
                try {
                    BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getPlayerManager().getMaxPlayerCount());
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
                    BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), client.getServer().getPlayerManager().getMaxPlayerCount());
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

    public static void setStartDate() {
        start = new Date().getTime();
    }
}