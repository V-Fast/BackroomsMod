package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;

public class ModRegisteries {
    public static void registerMod() {
        ModBlocks.registerModBlock();
        ModItems.registerModItems();
        ModSounds.registerSoundEvents();
        ModEntities.registerMobs();
        BackroomDimensions.registerDims();

        BackroomsMod.print("Registered mod");
    }
}
