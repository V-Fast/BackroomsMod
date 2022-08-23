package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.drinks.AlmondWater;
import com.lumination.backrooms.items.interactables.CameraItem;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.items.interactables.MusicTape;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    //public static final Item TESTITEM = registerItem("test_item", new TestItem(new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(1)));

    public static final Item SILK = registerItem("silk",
            new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item SILKED_BOOK = registerItem("silked_book",
            new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item ALMOND_WATER = registerItem("almond_water", new AlmondWater(
            new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(2*2).saturationModifier(3f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15, 0), 1F).build())));
    public static final Item COOKED_ALMOND_WATER = registerItem("cooked_almond_water", CookedAlmondWater(new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(4*2).saturationModifier(4f).build())));
    public static final Item TAPE = registerItem("tape", new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item CAMERA = registerItem("camera", new CameraItem(
            new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(1)));

    // tapes
    public static final Item HALLS_TAPE = registerItem("halls_tape", new MusicTape(15, ModSounds.HALLS, new FabricItemSettings(), 166));

    public static AlmondWater CookedAlmondWater(Item.Settings settings) {
        return new AlmondWater(settings).setCooked(true);
    }

    // Register

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(BackroomsMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BackroomsMod.LOGGER.debug("Registering ModItems for " + BackroomsMod.MOD_ID);
    }
}
