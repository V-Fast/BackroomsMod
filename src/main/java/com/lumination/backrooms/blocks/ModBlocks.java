package com.lumination.backrooms.blocks;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.interactable.FluorescentLight;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


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

    // Interactables

    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLight(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(13)), BackroomsItemsGroup.Main);
    public static final Block TAPE_PLAYER = registerBlock("tape_player",
            new TapePlayer(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.Main);
    public static final Block RADIO = registerBlock("radio",
            new Radio(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.Main);

    // Register

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
            new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlock() {
        BackroomsMod.print("Registering ModBlocks for " + BackroomsMod.MOD_ID);
    }
}
