package com.lumination.backrooms;

import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Date;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static final String versionId = "A-0.0.3A";
    public static final String versionName = "Developer (A-0.0.5A)";
    public static final boolean hasPrivateServer = false;
    public static long start;


    @Override
    public void onInitializeClient() {
        ModRegisteries.registerMod(true);

        BackroomsMod.print("Initialized Client Backrooms");
    }

    public static void setStartDate() {
        long now = new Date().getTime();
        start = now;
    }
}