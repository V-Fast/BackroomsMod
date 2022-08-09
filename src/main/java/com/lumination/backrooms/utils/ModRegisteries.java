package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.sounds.ModSounds;

public class ModRegisteries {
    public static void registerMod() {
        ModBlocks.registerModBlock();
        ModItems.registerModItems();
        ModSounds.registerSoundEvents();
        ModEntities.registerMobs();

        BackroomsMod.print("Registered mod");
    }
}
