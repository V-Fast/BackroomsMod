package com.lumination.backrooms;

import com.lumination.backrooms.client.events.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class BackroomsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
