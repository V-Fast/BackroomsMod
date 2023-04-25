package com.lumination.backrooms.blocks;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.interactable.FluorescentLight;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import net.minecraft.block.*;
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

    // level 0
    public static final Block MOIST_SILK = registerBlock("moist_silk",
            new Block(AbstractBlock.Settings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).requiresTool().strength(2f)), BackroomsItemsGroup.Main);
    public static final Block MOIST_SILK_PLANKS = registerBlock("moist_silk_planks",
            new Block(AbstractBlock.Settings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(2f)), BackroomsItemsGroup.Main);
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(AbstractBlock.Settings.of(Material.WOOL).sounds(BlockSoundGroup.WOOL).requiresTool().strength(2f)), BackroomsItemsGroup.Main);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            new Block(AbstractBlock.Settings.of(Material.WOOL).sounds(BlockSoundGroup.WOOL).strength(1f)), BackroomsItemsGroup.Main);
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLight(AbstractBlock.Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(13)), BackroomsItemsGroup.Main);

    // level 1
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            new Block(AbstractBlock.Settings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(Blocks.IRON_BLOCK.getHardness())), BackroomsItemsGroup.Main);
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block SCRATCHED_CONCRETE_STAIRS = registerBlock("slightly_scratched_concrete_stairs",
            new StairsBlock(SCRATCHED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(SCRATCHED_CONCRETE)), BackroomsItemsGroup.Main);
    public static final Block STAINED_CONCRETE_STAIRS = registerBlock("stained_concrete_stairs",
            new StairsBlock(STAINED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(STAINED_CONCRETE)), BackroomsItemsGroup.Main);
    public static final Block STAINED_MARKED_CONCRETE = registerBlock("stained_marked_concrete",
            new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.Main);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            new Block(AbstractBlock.Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(15)), BackroomsItemsGroup.Main);

    // other

    public static final Block TAPE_PLAYER = registerBlock("tape_player",
            new TapePlayer(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), List.of(BackroomsItemsGroup.Main, ItemGroups.REDSTONE));
    public static final Block RADIO = registerBlock("radio",
            new Radio(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()));

    // Register

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    // can be used for new 1.19.3+ creative inventory system
    private static Item registerBlockItem(String name, Block block, List<ItemGroup> tabs) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));

        // put in item group
        for (int i = 0; i < tabs.size(); i++) {
            ItemGroup tab = tabs.get(i);
        }
        return item;
    }

    public static void registerModBlock() {
        BackroomsMod.print("Registering ModBlocks for " + BackroomsMod.MOD_ID);
    }
}
