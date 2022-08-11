package com.lumination.backrooms;

import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud CameraHud = new CameraRecordHud(MinecraftClient.getInstance());

    @Override
    public void onInitializeClient() {
        BackroomsSettings.loadConfig();
        KeyInputHandler.register();
    }
}
