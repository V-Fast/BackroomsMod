package com.lumination.backrooms;

import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.utils.ModRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;
import java.util.Date;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static long start;

    @Override
    public void onInitializeClient() {
        try {
            ModRegistries.registerMod(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BackroomsMod.print("Initialized Client Backrooms");
    }

    public static void setStartDate() {
        start = new Date().getTime();
    }
}