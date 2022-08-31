package com.lumination.backrooms.utils;

import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;

public class ModRegisteries {
    private static final boolean serverEntity = false;

    public static void registerMod() {
        ModBlocks.registerModBlock();
        ModBlockEntities.registerBlockEntities();
        ModItems.registerModItems();
        ModSounds.registerSoundEvents();
        BackroomDimensions.registerDims();

        if (serverEntity) {
            ModEntities.registerMobs();
        }
    }
}
