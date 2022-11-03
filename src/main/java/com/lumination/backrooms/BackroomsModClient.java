package com.lumination.backrooms;

import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.util.Date;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static final boolean hasPrivateServer = false;
    public static long start;

    @Override
    public void onInitializeClient() {
        BackroomsSettings.loadConfig();
        KeyInputHandler.register();
        setStartDate();
        Discord.initDiscord();
        BackroomsRPC.loadingRpc();
        ModRegisteries.registerMod(true);

        HudRenderCallback.EVENT.register(camHud);
    }

    public static void setStartDate() {
        long now = new Date().getTime();
        start = now;
    }
}
