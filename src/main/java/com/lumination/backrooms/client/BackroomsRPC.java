package com.lumination.backrooms.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.io.IOException;

@ClientOnly
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