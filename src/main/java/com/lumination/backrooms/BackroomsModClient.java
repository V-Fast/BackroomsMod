package com.lumination.backrooms;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();
    public static final String versionId = "Dev";
    public static final String versionName = "Developer (A-0.0.5A)";
    public static final boolean hasPrivateServer = false;
    public static JsonObject latestVersion;
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

        try {
            latestVersion = findLatestVersion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonObject findLatestVersion() throws IOException {
        URL url = new URL("https://api.modrinth.com/v2/project/backrooms/version");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());

        BufferedReader reader = new BufferedReader(inputStream);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return JsonParser.parseString(sb.toString()).getAsJsonArray().get(0).getAsJsonObject();
    }

    public static void setStartDate() {
        long now = new Date().getTime();
        start = now;
    }
}