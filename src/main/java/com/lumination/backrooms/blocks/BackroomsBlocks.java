package com.lumination.backrooms.blocks;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.interactable.FluorescentLight;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import com.lumination.backrooms.items.BackroomsItemsGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;


public class BackroomsBlocks {

    // Level 0
    public static final Block MOIST_SILK = registerBlock("moist_silk",
            new Block(QuiltBlockSettings.copyOf(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN);
    public static final Block MOIST_SILK_PLANKS = registerBlock("moist_silk_planks",
            new Block(QuiltBlockSettings.copyOf(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).strength(2f)), BackroomsItemsGroup.MAIN);
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            new Block(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).strength(1f)), BackroomsItemsGroup.MAIN);
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLight(QuiltBlockSettings.copyOf(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(9)), BackroomsItemsGroup.MAIN);
    public static final Block LEVEL_ZERO_PORTAl = registerBlock("level_zero_portal",
            new LevelZeroPortalBlock(QuiltBlockSettings.copyOf(Blocks.NETHER_PORTAL).luminance(15).breakInstantly()), BackroomsItemsGroup.MAIN);

    // Level 1
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            new Block(QuiltBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.METAL).strength(Blocks.IRON_BLOCK.getHardness())), BackroomsItemsGroup.MAIN);
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            new Block(QuiltBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN);
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            new Block(QuiltBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN);
    public static final Block SCRATCHED_CONCRETE_STAIRS = registerBlock("slightly_scratched_concrete_stairs",
            new StairsBlock(SCRATCHED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(SCRATCHED_CONCRETE)), BackroomsItemsGroup.MAIN);
    public static final Block STAINED_CONCRETE_STAIRS = registerBlock("stained_concrete_stairs",
            new StairsBlock(STAINED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(STAINED_CONCRETE)), BackroomsItemsGroup.MAIN);
    public static final Block STAINED_MARKED_CONCRETE = registerBlock("stained_marked_concrete",
            new Block(QuiltBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            new Block(QuiltBlockSettings.copyOf(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(15)), BackroomsItemsGroup.MAIN);

    // Other

    public static final Block TAPE_PLAYER = registerBlock("tape_player",
            new TapePlayer(QuiltBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.MAIN, ItemGroups.REDSTONE);
    public static final Block RADIO = registerBlock("radio",
            new Radio(QuiltBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.MAIN);

    // Register

    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> group) {
        BackroomsBlocks.registerBlockItem(name, block, group);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    @SafeVarargs
    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup>... groups) {
        BackroomsBlocks.registerBlockItem(name, block, groups);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, RegistryKey<ItemGroup> group) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new QuiltItemSettings()));

        // Put in item group
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(item);
        });
    }

    // Can be used for new 1.19.3+ creative inventory system
    @SafeVarargs
    private static void registerBlockItem(String name, Block block, RegistryKey<ItemGroup>... groups) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.MOD_ID, name),
                new BlockItem(block, new QuiltItemSettings()));

        // Put in item group
        for (int i = 0; i < groups.length; i++) {
            RegistryKey<ItemGroup> tab = groups[i];
            ItemGroupEvents.modifyEntriesEvent(tab).register(content -> {
                content.add(item);
            });
        }
    }

    public static void registerModBlock() {
        BackroomsMod.LOGGER.debug("Registered Blocks");
    }
}
