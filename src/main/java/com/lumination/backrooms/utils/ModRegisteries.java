package com.lumination.backrooms.utils;

import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.items.ModItemsClient;
import com.lumination.backrooms.items.ModItemsServer;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;

public class ModRegisteries {
    private static final boolean serverEntity = false;

    public static void registerMod(boolean client) {
        if (client) {
            ModBlocks.registerModBlock();
            ModBlockEntities.registerBlockEntities();
            ModSounds.registerSoundEvents();
            BackroomDimensions.registerDims();
            ModItems.registerModItems();

            // client only
            ModItemsClient.registerModItems();
        } else {
            ModBlocks.registerModBlock();
            ModBlockEntities.registerBlockEntities();
            ModSounds.registerSoundEvents();
            BackroomDimensions.registerDims();
            ModItems.registerModItems();

            // server only
            ModItemsServer.registerModItems();
        }



        if (serverEntity) {
            ModEntities.registerMobs();
        }
    }
}
