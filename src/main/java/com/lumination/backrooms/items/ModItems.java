package com.lumination.backrooms.items;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.drinks.AlmondWater;
import com.lumination.backrooms.items.interactables.CameraItem;
import com.lumination.backrooms.items.interactables.SilkenBook;
import com.lumination.backrooms.sounds.ModSounds;
import com.lumination.backrooms.items.interactables.MusicTape;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    //public static final Item TESTITEM = registerItem("test_item", new TestItem(new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(1)));

    public static final Item SILK = registerItem("silk",
            new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item SILKEN_BOOK = registerItem("silken_book",
            new SilkenBook(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item ALMOND_WATER = registerItem("almond_water", new AlmondWater(
            new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(2*2).saturationModifier(3f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 15, 0), 1F).build())));
    public static final Item COOKED_ALMOND_WATER = registerItem("cooked_almond_water", CookedAlmondWater(new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(8).food(new FoodComponent.Builder().alwaysEdible().hunger(4*2).saturationModifier(4f).build())));
    public static final Item TAPE = registerItem("tape", new Item(new FabricItemSettings().group(BackroomsItemsGroup.Main)));
    public static final Item CAMERA = registerItem("camera", new CameraItem(
            new FabricItemSettings().group(BackroomsItemsGroup.Main).maxCount(1)));

    // tapes
    public static final Item HALLS_TAPE = registerItem("halls_tape", new MusicTape(7, ModSounds.HALLS, new FabricItemSettings(), 1));
    public static final Item GOVERNMENT_TAPE = registerItem("government_funding_tape", new MusicTape(15, ModSounds.GOVERNMENT_FUNDING, new FabricItemSettings(), 1));
    public static final Item COMPLEX_TAPE = registerItem("the_complex_tape", new MusicTape(13, ModSounds.COMPLEX, new FabricItemSettings(), 1));
    public static final Item INSTANCES_TAPE = registerItem("instances_tape", new MusicTape(4, ModSounds.INSTANCES, new FabricItemSettings(), 1));
    public static final Item NIGHTS_TAPE = registerItem("warm_nights_tape", new MusicTape(6, ModSounds.WARM_NIGHTS, new FabricItemSettings(), 1));
    public static final Item TITLE_TAPE = registerItem("title_screen_tape", new MusicTape(8, ModSounds.TITLE_SCREEN, new FabricItemSettings(), 1));
    public static final Item SNOW_TAPE = registerItem("snow_world_tape", new MusicTape(5, ModSounds.SNOW_WORLD, new FabricItemSettings(), 1));
    public static final Item AUDITORY_TAPE = registerItem("auditory_guidepost_tape", new MusicTape(9, ModSounds.AUDITORY_GUIDEPOST, new FabricItemSettings(), 1));
    public static final Item NOT_DECISION_TAPE = registerItem("not_your_decision_tape", new MusicTape(8, ModSounds.NOT_YOUR_DECISION, new FabricItemSettings(), 1));
    public static final Item SEALED_AWAY_TAPE = registerItem("sealed_away_tape", new MusicTape(5, ModSounds.SEALED_AWAY, new FabricItemSettings(), 1));
    public static final Item CLIFFS_DOVER_TAPE = registerItem("white_cliffs_of_dover_tape", new MusicTape(3, ModSounds.CLIFFS_OF_DOVER, new FabricItemSettings(), 1));

    public static AlmondWater CookedAlmondWater(Item.Settings settings) {
        return new AlmondWater(settings).setCooked(true);
    }

    // Register

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(BackroomsMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BackroomsMod.print("Registering ModItems");
    }
}
