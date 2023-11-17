package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class BackroomsItemsGroup {

    public static final RegistryKey<ItemGroup> MAIN = BackroomsItemsGroup.createItemGroup("backrooms", (builder -> builder
        .icon(() -> new ItemStack(BackroomsItems.SILK))
        .displayName(Text.translatable("itemGroup.backrooms.backrooms"))
    ));

    public static final RegistryKey<ItemGroup> WEAPONS = BackroomsItemsGroup.createItemGroup("weapons", (builder -> builder
            .icon(() -> new ItemStack(BackroomsItems.NAILED_BAT))
            .displayName(Text.translatable("itemGroup.backrooms.weapons"))
    ));

    public static final RegistryKey<ItemGroup> MUSIC_TAPES = BackroomsItemsGroup.createItemGroup("music_tapes", (builder -> builder
            .icon(() -> new ItemStack(BackroomsItems.TAPE))
            .displayName(Text.translatable("itemGroup.backrooms.music_tapes"))
    ));

    public static final RegistryKey<ItemGroup> ENTITIES = BackroomsItemsGroup.createItemGroup("entities", (builder -> builder
            .icon(() -> new ItemStack(BackroomsItems.BACTERIA_EGG))
            .displayName(Text.translatable("itemGroup.backrooms.entities"))
    ));

    public static RegistryKey<ItemGroup> createItemGroup(String idPath, Function<ItemGroup.Builder, ItemGroup.Builder> builderSupplier) {

        ItemGroup.Builder itemGroupBuilder = builderSupplier.apply(FabricItemGroup.builder());

        RegistryKey<ItemGroup> itemGroupKey = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(BackroomsMod.MOD_ID, idPath));

        Registry.register(Registries.ITEM_GROUP, itemGroupKey, itemGroupBuilder.build());

        return itemGroupKey;
    }
}
