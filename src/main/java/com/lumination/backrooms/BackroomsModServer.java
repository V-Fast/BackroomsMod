package com.lumination.backrooms;

import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;

@Environment(EnvType.SERVER)
public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        try {
            ModRegisteries.registerMod(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BackroomsMod.print("Initialized Server Backrooms");
    }
}
