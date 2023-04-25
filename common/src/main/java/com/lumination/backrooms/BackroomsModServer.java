package com.lumination.backrooms;

import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ModRegisteries.registerMod(false);
        BackroomsMod.print("Initialized Server Backrooms");
    }
}
