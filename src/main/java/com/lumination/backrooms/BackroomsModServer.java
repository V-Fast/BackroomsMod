package com.lumination.backrooms;

import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.DedicatedServerModInitializer;

public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ModRegisteries.registerMod(false);
        BackroomsMod.print("Initialized Server Backrooms");
    }
}
