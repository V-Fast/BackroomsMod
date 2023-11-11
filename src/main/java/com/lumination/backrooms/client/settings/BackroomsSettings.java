package com.lumination.backrooms.client.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lumination.backrooms.BackroomsMod;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ClientOnly
public class BackroomsSettings {
    private static final Path CONFIG_FILE = QuiltLoader.getConfigDir().resolve(BackroomsMod.SETTINGS_NAME + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static BackroomsSettings instance;

    private boolean showRecord = true;
    private boolean explainErrors = false;
    private boolean discordPresence = true;
    private String discordLabel = "By Lumaa";

    private static BackroomsSettings getInstance() {
        if (BackroomsSettings.instance == null) {
            BackroomsSettings.loadConfig();
        }

        return BackroomsSettings.instance;
    }

    public static void saveConfig() {
        if (BackroomsSettings.instance != null) {
            BackroomsSettings.writeFile(BackroomsSettings.instance);
        }
    }

    public static void loadConfig() {
        BackroomsMod.print(instance == null ? "Loading config..." : "Reloading config...");

        BackroomsSettings.instance = readFile();

        if (BackroomsSettings.instance == null) {
            BackroomsSettings.instance = new BackroomsSettings();
        }

        BackroomsSettings.writeFile(BackroomsSettings.instance);
    }

    @Nullable
    private static BackroomsSettings readFile() {
        if (!Files.isRegularFile(CONFIG_FILE))
            return null;

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, BackroomsSettings.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writeFile(BackroomsSettings instance) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Boolean canShowRecord() {
        return BackroomsSettings.getInstance().showRecord;
    }

    public static void setShowRecord(boolean bool) {
        BackroomsSettings.getInstance().showRecord = bool;
    }

    public static Boolean explainsError() {
        return BackroomsSettings.getInstance().explainErrors;
    }

    public static void setExplainError(boolean bool) {
        BackroomsSettings.getInstance().explainErrors = bool;
    }

    public static String discordLabel() {
        return BackroomsSettings.getInstance().discordLabel;
    }

    public static void setDiscordLabel(String label) {
        BackroomsSettings.getInstance().discordLabel = label;
    }

    public static boolean hasDiscordPresence() {
        return BackroomsSettings.getInstance().discordPresence;
    }

    public static void setDiscordPresence(boolean bool) {
        BackroomsSettings.getInstance().discordPresence = bool;
    }
}