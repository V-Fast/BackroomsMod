package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.drinks.AlmondWater;
import com.lumination.backrooms.items.interactables.CameraItem;
import com.lumination.backrooms.items.interactables.MusicTape;
import com.lumination.backrooms.items.interactables.SilkenBook;
import com.lumination.backrooms.items.interactables.SilkenBookClient;
import com.lumination.backrooms.sounds.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItemsClient {
    public static final Item SILKEN_BOOK = registerItem("silken_book",
            new SilkenBookClient(new FabricItemSettings().group(BackroomsItemsGroup.Main)));

    // Register

    public static Item registerItem(String name, Item item) {
        
        return Registry.register(Registry.ITEM, new Identifier(BackroomsMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BackroomsMod.print("Registering ModItemsClient");
    }
}
