package com.lumination.backrooms.client.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lumination.backrooms.BackroomsMod;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackroomsSettings {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(BackroomsMod.SETTINGS_NAME + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static BackroomsSettings INSTANCE;

    private boolean showRecord = true;

    private static BackroomsSettings getInstance() {
        if (INSTANCE == null) {
            loadConfig();
        }

        return INSTANCE;
    }

    public static void saveConfig() {
        if (INSTANCE != null) {
            writeFile(INSTANCE);
        }
    }

    public static void loadConfig() {
        BackroomsMod.print(INSTANCE == null ? "Loading config..." : "Reloading config...");

        INSTANCE = readFile();

        if (INSTANCE == null) {
            INSTANCE = new BackroomsSettings();
        }

        writeFile(INSTANCE);
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

    public static final Boolean canShowRecord() {
        return getInstance().showRecord;
    }

    public static final void setShowRecord(boolean bool) {
        getInstance().showRecord = bool;
    }
}
