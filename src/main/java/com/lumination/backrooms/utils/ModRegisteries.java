package com.lumination.backrooms.utils;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.ModBlocks;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

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

        BackroomsMod.print("Registered mod");
    }
}
