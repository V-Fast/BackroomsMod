package com.lumination.backrooms;

import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.utils.ModRegistries;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.io.IOException;
import java.util.Date;

@ClientOnly
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static long start;

    @Override
    public void onInitializeClient(ModContainer mod) {
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