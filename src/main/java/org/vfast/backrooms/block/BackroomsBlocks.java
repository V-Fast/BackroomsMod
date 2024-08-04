package org.vfast.backrooms.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.block.interactable.FluorescentLightBlock;
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
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block MOIST_SILK_OAK_PLANKS = registerBlock("moist_silk_oak_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD).strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).requiresTool().strength(2f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            new Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).strength(1f)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            new FluorescentLightBlock(AbstractBlock.Settings.copy(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(value -> 9)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    // Level 1
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.METAL).strength(Blocks.IRON_BLOCK.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block SCRATCHED_CONCRETE_STAIRS = registerBlock("slightly_scratched_concrete_stairs",
            new StairsBlock(SCRATCHED_CONCRETE.getDefaultState(), FabricBlockSettings.copy(SCRATCHED_CONCRETE)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_CONCRETE_STAIRS = registerBlock("stained_concrete_stairs",
            new StairsBlock(STAINED_CONCRETE.getDefaultState(), FabricBlockSettings.copy(STAINED_CONCRETE)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STAINED_MARKED_CONCRETE = registerBlock("stained_marked_concrete",
            new Block(AbstractBlock.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE).strength(Blocks.BLACK_CONCRETE.getHardness())), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            new Block(AbstractBlock.Settings.copy(Blocks.GLASS).sounds(BlockSoundGroup.GLASS).strength(0.1f).luminance(value -> 15)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);

    // Level Run
    public static final Block HOSPITAL_DOOR = registerBlock("hospital_door", new DoorBlock(BlockSetTypeBuilder.copyOf(BlockSetType.IRON).openableByHand(true).build(Identifier.of(BackroomsMod.ID, "ceramic")), AbstractBlock.Settings.copy(Blocks.IRON_DOOR)), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);
    public static final Block EXIT_SIGN = registerBlock("exit_sign", new ExitSignBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.GLASS).strength(0.3f, 0.1f).luminance(6).nonOpaque().requiresTool()), BackroomsItemsGroup.MAIN, ItemGroups.BUILDING_BLOCKS);

    // Other

    // Register
    @SafeVarargs
    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup>... groups) {
        BackroomsBlocks.registerBlockItem(name, block, groups);
        return Registry.register(Registries.BLOCK, Identifier.of(BackroomsMod.ID, name), block);
    }

    @SafeVarargs
    private static void registerBlockItem(String name, Block block, RegistryKey<ItemGroup>... groups) {
        Item item = Registry.register(Registries.ITEM, Identifier.of(BackroomsMod.ID, name),
                new BlockItem(block, new Item.Settings()));

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
