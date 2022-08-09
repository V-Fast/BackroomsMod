package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BackroomsItemsGroup {
    public static final ItemGroup Main = FabricItemGroupBuilder.build(
            new Identifier(BackroomsMod.MOD_ID, "backrooms"),
            () -> new ItemStack((ModItems.SILK)));
}
