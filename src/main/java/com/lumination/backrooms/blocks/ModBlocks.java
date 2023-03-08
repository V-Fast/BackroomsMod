package com.lumination.backrooms.blocks;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.interactable.FluorescentLight;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.List;


public class ModBlocks {
    // Normal

    public static final Block MOIST_SILK = registerBlock("moist_silk",
            new Block(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).requiresTool().strength(2f)), BackroomsItemsGroup.Main);
    public static final Block MOIST_SILK_PLANKS = registerBlock("moist_silk_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(2f)), BackroomsItemsGroup.Main);
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(FabricBlockSettings.of(Material.WOOL).sounds(BlockSoundGroup.WOOL).requiresTool().strength(2f)), BackroomsItemsGroup.Main);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            new Block(FabricBlockSettings.of(Material.WOOL).sounds(BlockSoundGroup.WOOL).strength(1f)), BackroomsItemsGroup.Main);
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLight(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(13)), BackroomsItemsGroup.Main);
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(Blocks.IRON_BLOCK.getHardness())), BackroomsItemsGroup.Main);
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block STAINED_MARKED_CONCRETE = registerBlock("stained_marked_concrete",
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            new Block(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(15)), BackroomsItemsGroup.Main);

    // Interactables

    public static final Block TAPE_PLAYER = registerBlock("tape_player",
            new TapePlayer(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), List.of(BackroomsItemsGroup.Main, ItemGroups.REDSTONE));
    public static final Block RADIO = registerBlock("radio",
            new Radio(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.Main);

    // Register

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, List<ItemGroup> tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));

        // put in item group
        ItemGroupEvents.modifyEntriesEvent(tab).register(content -> {
            content.add(item);
        });

        return item;
    }

    // can be used for new 1.19.3 creative inventory system
    private static Item registerBlockItem(String name, Block block, List<ItemGroup> tabs) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));

        // put in item group
        for (int i = 0; i < tabs.size(); i++) {
            ItemGroup tab = tabs.get(i);
            ItemGroupEvents.modifyEntriesEvent(tab).register(content -> {
                content.add(item);
            });
        }
        return item;
    }

    public static void registerModBlock() {
        BackroomsMod.print("Registering ModBlocks for " + BackroomsMod.MOD_ID);
    }
}
