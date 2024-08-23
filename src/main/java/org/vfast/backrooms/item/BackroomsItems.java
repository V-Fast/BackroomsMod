package org.vfast.backrooms.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.util.Identifier;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.item.consumable.AlmondWater;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.vfast.backrooms.sound.BackroomsSounds;

public class BackroomsItems {
    public static final Item SILK = registerItem("silk",
            new Item(new Item.Settings()), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);
    public static final Item ALMOND_WATER = registerItem("almond_water", new AlmondWater(
            new Item.Settings().maxCount(8).food(new FoodComponent.Builder().alwaysEdible().nutrition(2*2).saturationModifier(3f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15, 0), 1F).build())), BackroomsItemsGroup.MAIN, ItemGroups.FOOD_AND_DRINK);
    public static final Item COOKED_ALMOND_WATER = registerItem("cooked_almond_water", new AlmondWater(new Item.Settings().maxCount(8).food(new FoodComponent.Builder().alwaysEdible().nutrition(4*2).saturationModifier(4f).build())).setCooked(true), BackroomsItemsGroup.MAIN, ItemGroups.FOOD_AND_DRINK);
    public static final Item TAPE = registerItem("tape", new Item(new Item.Settings()), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);
//    public static final Item CAMERA = registerItem("camera", new CameraItem(
//            new Item.Settings().maxCount(1)), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);
    public static final Item ENERGY_BAR = registerItem("energy_bar", new Item(
            new Item.Settings().maxCount(64).food(new FoodComponent.Builder().alwaysEdible().nutrition(2*3).saturationModifier(3f).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 5, 0), 1F).build())), BackroomsItemsGroup.MAIN, ItemGroups.FOOD_AND_DRINK);

    // Music Tapes
    public static final Item PAPER_BIRCH_TAPE = registerItem("paper_birch_tape",
            new MusicTape(new Item.Settings().maxCount(1), BackroomsSounds.PAPER_BIRCH), BackroomsItemsGroup.MAIN);

    public static Item registerItem(String name, Item item, RegistryKey<ItemGroup> group) {
        Item newItem = Registry.register(Registries.ITEM, Identifier.of(BackroomsMod.ID, name), item);

        // Put In Item Group
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(newItem));

        return newItem;
    }

    // Can be used for new 1.19.3+ creative inventory system
    @SafeVarargs
    private static Item registerItem(String name, Item item, RegistryKey<ItemGroup>... groups) {
        Item newItem = Registry.register(Registries.ITEM, Identifier.of(BackroomsMod.ID, name), item);

        // Put In Item Group
        for (RegistryKey<ItemGroup> group : groups) {
            ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
                content.add(newItem);
            });
        }
        return newItem;
    }

    public static void registerItems() {}
}