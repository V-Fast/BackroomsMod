package com.lumination.backrooms;

import com.lumination.backrooms.utils.ModRegistries;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;

@Environment(EnvType.SERVER)
public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        try {
            ModRegistries.registerMod(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BackroomsMod.print("Initialized Server Backrooms");
    }
}
