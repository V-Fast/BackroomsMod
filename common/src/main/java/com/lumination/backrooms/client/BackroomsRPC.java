package com.lumination.backrooms.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BackroomsRPC {
    public static final String appId = "1036661108154585098";

    public static void loadingRpc() {
        Discord.setPresence("Loading...", "", "mod");
    }

    public static void customLabelRpc(String subtitle, int playerCount, int maxPlayer) {
        try {
            Discord.setPresence(BackroomsSettings.discordLabel(), subtitle, "mod", playerCount, maxPlayer);
        } catch (NumberFormatException e) {
            Discord.setPresence(BackroomsSettings.discordLabel(), subtitle, "mod", 0, 0);
            BackroomsMod.LOGGER.error("Numbers didn't load");
            BackroomsMod.LOGGER.error(e.getLocalizedMessage());
        }
    }
}