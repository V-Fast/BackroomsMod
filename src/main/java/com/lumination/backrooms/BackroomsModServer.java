package com.lumination.backrooms;

import com.lumination.backrooms.utils.ModRegistries;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.DedicatedServerOnly;
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer;

import java.io.IOException;

@DedicatedServerOnly
public class BackroomsModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer(ModContainer mod) {
        try {
            ModRegistries.registerMod(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BackroomsMod.print("Initialized Server Backrooms");
    }
}
