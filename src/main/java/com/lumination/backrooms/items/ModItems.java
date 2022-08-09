package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.ModEntities;
import com.lumination.backrooms.items.drinks.AlmondWater;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item SILK = registerItem("silk",
            new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item SILKED_BOOK = registerItem("silked_book",
            new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item ALMOND_WATER = registerItem("almond_water", new AlmondWater(
            new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(2*2).saturationModifier(3f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 6, 0), 1F).build())));
    public static final Item COOKED_ALMOND_WATER = registerItem("cooked_almond_water", CookedAlmondWater(new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(4*2).saturationModifier(4f).build())));
    public static final Item ENTITY_SPAWN_EGG = registerItem("entity_spawn_egg",
            new SpawnEggItem(ModEntities.ENTITY,0x0f0f0f, 0x1e1e1e,
                    new FabricItemSettings().group(BackroomsItemsGroup.Main)));

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
