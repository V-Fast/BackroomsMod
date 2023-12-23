package org.vfast.backrooms.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.block.interactable.FluorescentLight;
import org.vfast.backrooms.block.interactable.Radio;
import org.vfast.backrooms.block.interactable.TapePlayer;
import org.vfast.backrooms.item.BackroomsItemsGroup;
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

public class BackroomsBlocks {

    // Level 0
    public static final Block MOIST_SILK = registerBlock("moist_silk",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block MOIST_SILK_PLANKS = registerBlock("moist_silk_planks",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).strength(1f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLight(FabricBlockSettings.copyOf(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(9)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block BACKROOMS_PORTAL = registerBlock("backrooms_portal",
            new BackroomsPortalBlock(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL).luminance(15).breakInstantly()), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);

    // Level 1
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.METAL).strength(Blocks.IRON_BLOCK.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block SCRATCHED_CONCRETE_STAIRS = registerBlock("slightly_scratched_concrete_stairs",
            new StairsBlock(SCRATCHED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(SCRATCHED_CONCRETE)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_CONCRETE_STAIRS = registerBlock("stained_concrete_stairs",
            new StairsBlock(STAINED_CONCRETE.getDefaultState(), AbstractBlock.Settings.copy(STAINED_CONCRETE)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_MARKED_CONCRETE = registerBlock("stained_marked_concrete",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            new Block(FabricBlockSettings.copyOf(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(15)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);

    // Level Run
    public static final Block HOSPITAL_DOOR = registerBlock("hospital_door", new DoorBlock(BlockSetTypeBuilder.copyOf(BlockSetType.IRON).openableByHand(true).build(new Identifier(BackroomsMod.ID, "ceramic")), FabricBlockSettings.copyOf(Blocks.IRON_DOOR)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);

    // Other

    public static final Block TAPE_PLAYER = registerBlock("tape_player",
            new TapePlayer(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);
    public static final Block RADIO = registerBlock("radio",
            new Radio(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.IRON_BLOCK.getHardness()).requiresTool().nonOpaque()), BackroomsItemsGroup.MAIN, ItemGroups.FUNCTIONAL);

    // Register
    @SafeVarargs
    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup>... groups) {
        BackroomsBlocks.registerBlockItem(name, block, groups);
        return Registry.register(Registries.BLOCK, new Identifier(BackroomsMod.ID, name), block);
    }

    // Can be used for new 1.19.3+ creative inventory system
    @SafeVarargs
    private static void registerBlockItem(String name, Block block, RegistryKey<ItemGroup>... groups) {
        Item item = Registry.register(Registries.ITEM, new Identifier(BackroomsMod.ID, name),
                new BlockItem(block, new FabricItemSettings()));

        // Put in item group
        for (int i = 0; i < groups.length; i++) {
            RegistryKey<ItemGroup> tab = groups[i];
            ItemGroupEvents.modifyEntriesEvent(tab).register(content -> {
                content.add(item);
            });
        }
    }

    public static void registerBlocks() {}
}
