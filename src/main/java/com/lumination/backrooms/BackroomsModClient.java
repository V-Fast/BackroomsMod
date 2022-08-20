package com.lumination.backrooms;

import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static final boolean hasPrivateServer = false;

    @Override
    public void onInitializeClient() {
        ModRegisteries.registerMod();
        BackroomsSettings.loadConfig();
        KeyInputHandler.register();

        HudRenderCallback.EVENT.register(camHud);
    }
}
