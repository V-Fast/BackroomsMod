package com.lumination.backrooms;

import com.lumination.backrooms.client.events.KeyInputHandler;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.fabricmc.api.ClientModInitializer;

public class BackroomsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BackroomsSettings.loadConfig();
        KeyInputHandler.register();
    }
}
