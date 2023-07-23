package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.interactables.SilkenBook;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModItemsServer {
    public static final Item SILKEN_BOOK = registerItem("silken_book",
            new SilkenBook(new FabricItemSettings()), BackroomsItemsGroup.MAIN);

    // Register

    public static Item registerItem(String name, Item item, RegistryKey<ItemGroup> group) {
        Item newItem = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name), item);

        // put in item group
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(item);
        });

        return newItem;
    }

    public static void registerModItems() {
        BackroomsMod.print("Registering ModItemsServer");
    }
}
