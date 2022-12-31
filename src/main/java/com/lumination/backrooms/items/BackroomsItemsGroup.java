package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BackroomsItemsGroup {
    public static final ItemGroup Main = FabricItemGroup.builder(
            new Identifier(BackroomsMod.MOD_ID, "backrooms"))
            .icon(() -> new ItemStack((ModItems.SILK)))
            .build();
    public static final ItemGroup Weapons = FabricItemGroup.builder(
            new Identifier(BackroomsMod.MOD_ID, "weapons"))
            .icon(() -> new ItemStack((ModItems.NAILED_BAT)))
            .build();
    public static final ItemGroup MusicTapes = FabricItemGroup.builder(
            new Identifier(BackroomsMod.MOD_ID, "music_tapes"))
            .icon(() -> new ItemStack((ModItems.TAPE)))
            .build();
}
