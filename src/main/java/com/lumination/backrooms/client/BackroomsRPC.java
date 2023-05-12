package com.lumination.backrooms.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class BackroomsRPC {

    public static void loadingRpc() throws IOException {
        Discord.setPresence("Loading...", "", "mod");
    }

    public static void customLabelRpc(String subtitle, int playerCount, int maxPlayer) throws IOException {
        try {
            Discord.setPresence(BackroomsSettings.discordLabel(), subtitle, "mod", playerCount, maxPlayer);
        } catch (IOException e) {
            Discord.setPresence(BackroomsSettings.discordLabel(), subtitle, "mod", 0, 0);
            BackroomsMod.LOGGER.error("Numbers didn't load");
            BackroomsMod.LOGGER.error(e.getLocalizedMessage());
        }
    }
}