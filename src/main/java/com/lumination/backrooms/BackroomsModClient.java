package com.lumination.backrooms;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lumaa.libu.LibuLib;
import com.lumaa.libu.update.ModrinthMod;
import com.lumaa.libu.update.UpdateChecker;
import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.client.screens.SilkBookScreen;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.items.ModItemsClient;
import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;

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