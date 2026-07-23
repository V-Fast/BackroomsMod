package org.vfast.backrooms.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.vfast.backrooms.BackroomsMod;

import java.util.function.Function;

public class BackroomsBlocks {
    public static final Block FAKE_BLOCK = registerBlock("fake_block", FakeBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.INFESTED_COBBLESTONE), false);

    // Moist Silk blocks
    public static final Block MOIST_SILK = registerBlock("moist_silk",
              BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).requiresCorrectToolForDrops().strength(2f));
    public static final Block MOIST_SILK_OAK_PLANKS = registerBlock("moist_silk_oak_planks",
            createMoistSilkPlanksBlock(Blocks.OAK_PLANKS));
    public static final Block MOIST_SILK_SPRUCE_PLANKS = registerBlock("moist_silk_spruce_planks",
            createMoistSilkPlanksBlock(Blocks.SPRUCE_PLANKS));
    public static final Block MOIST_SILK_ACACIA_PLANKS = registerBlock("moist_silk_acacia_planks",
            createMoistSilkPlanksBlock(Blocks.ACACIA_PLANKS));
    public static final Block MOIST_SILK_DARK_OAK_PLANKS = registerBlock("moist_silk_dark_oak_planks",
            createMoistSilkPlanksBlock(Blocks.DARK_OAK_PLANKS));
    public static final Block MOIST_SILK_BIRCH_PLANKS = registerBlock("moist_silk_birch_planks",
            createMoistSilkPlanksBlock(Blocks.BIRCH_PLANKS));
    public static final Block MOIST_SILK_JUNGLE_PLANKS = registerBlock("moist_silk_jungle_planks",
            createMoistSilkPlanksBlock(Blocks.JUNGLE_PLANKS));
    public static final Block MOIST_SILK_BAMBOO_PLANKS = registerBlock("moist_silk_bamboo_planks",
            createMoistSilkPlanksBlock(Blocks.BAMBOO_PLANKS));
    public static final Block MOIST_SILK_MANGROVE_PLANKS = registerBlock("moist_silk_mangrove_planks",
            createMoistSilkPlanksBlock(Blocks.MANGROVE_PLANKS));
    public static final Block MOIST_SILK_CHERRY_PLANKS = registerBlock("moist_silk_cherry_planks",
            createMoistSilkPlanksBlock(Blocks.CHERRY_PLANKS));
    public static final Block MOIST_SILK_CRIMSON_PLANKS = registerBlock("moist_silk_crimson_planks",
            createMoistSilkPlanksBlock(Blocks.CRIMSON_PLANKS));
    public static final Block MOIST_SILK_WARPED_PLANKS = registerBlock("moist_silk_warped_planks",
            createMoistSilkPlanksBlock(Blocks.WARPED_PLANKS));

    // Marked Concrete blocks
    public static final Block MARKED_A_CONCRETE = registerBlock("marked_a_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_B_CONCRETE = registerBlock("marked_b_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_C_CONCRETE = registerBlock("marked_c_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_D_CONCRETE = registerBlock("marked_d_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_E_CONCRETE = registerBlock("marked_e_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_F_CONCRETE = registerBlock("marked_f_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_G_CONCRETE = registerBlock("marked_g_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_H_CONCRETE = registerBlock("marked_h_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_I_CONCRETE = registerBlock("marked_i_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_J_CONCRETE = registerBlock("marked_j_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_K_CONCRETE = registerBlock("marked_k_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_L_CONCRETE = registerBlock("marked_l_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_M_CONCRETE = registerBlock("marked_m_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_N_CONCRETE = registerBlock("marked_n_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_O_CONCRETE = registerBlock("marked_o_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_P_CONCRETE = registerBlock("marked_p_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_Q_CONCRETE = registerBlock("marked_q_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_R_CONCRETE = registerBlock("marked_r_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_S_CONCRETE = registerBlock("marked_s_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_T_CONCRETE = registerBlock("marked_t_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_U_CONCRETE = registerBlock("marked_u_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_V_CONCRETE = registerBlock("marked_v_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_W_CONCRETE = registerBlock("marked_w_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_X_CONCRETE = registerBlock("marked_x_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_Y_CONCRETE = registerBlock("marked_y_concrete",
            createMarkedConcreteBlock());
    public static final Block MARKED_Z_CONCRETE = registerBlock("marked_z_concrete",
            createMarkedConcreteBlock());

    // Level 0
    public static final Block BACKROOMS_PORTAL = registerBlock("backrooms_portal", BackroomsPortalBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL).lightLevel((b) -> 0), false);
    public static final Block DROPPED_CEILING = registerBlock("dropped_ceiling",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).strength(1f));
    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).requiresCorrectToolForDrops().strength(2f));
    public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).sound(SoundType.GLASS).strength(0.1f).lightLevel(state -> 9));
    public static final Block BLUE_TAPESTRY = registerBlock("blue_tapestry",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).strength(2f));
    public static final Block STRIPED_BLUE_TAPESTRY = registerBlock("striped_blue_tapestry",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).strength(2f));
//    public static final Block BLUE_TAPE = registerTape("blue_tape");

    // Level 1
    public static final Block SMOOTH_IRON = registerBlock("smooth_iron_block",
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.METAL));
    public static final Block SCRATCHED_CONCRETE = registerBlock("slightly_scratched_concrete",
            createMarkedConcreteBlock());
    public static final Block STAINED_CONCRETE = registerBlock("stained_concrete",
            createMarkedConcreteBlock());
    public static final Block SCRATCHED_CONCRETE_STAIRS = registerBlock("slightly_scratched_concrete_stairs",
            (p) -> new StairBlock(SCRATCHED_CONCRETE.defaultBlockState(), p), BlockBehaviour.Properties.ofFullCopy(SCRATCHED_CONCRETE).requiresCorrectToolForDrops(), false);
    public static final Block STAINED_CONCRETE_STAIRS = registerBlock("stained_concrete_stairs",
            (p) -> new StairBlock(STAINED_CONCRETE.defaultBlockState(), p), BlockBehaviour.Properties.ofFullCopy(STAINED_CONCRETE).requiresCorrectToolForDrops(), false);
    public static final Block STREET_LIGHT = registerBlock("street_light",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).sound(SoundType.GLASS).strength(0.1f).lightLevel(state -> 15));
    public static final Block DECAYING_BRICKS = registerBlock("decaying_bricks",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE).requiresCorrectToolForDrops());

    // custom model
    public static final Block AIR_VENT = registerBlock("air_vent",
            VentBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(2f), false);
//    public static final Block EXIT_SIGN = registerBlock("exit_sign",
//            ExitBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(2f), false);
    public static final Block WOOLY_CHAIR = registerBlock("wooly_chair",
            WoolyChairBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.white()).sound(SoundType.WOOL).noOcclusion().requiresCorrectToolForDrops().strength(2f), false);
    public static final Block TEXT_SIGN = registerBlock("text_ceiling_sign",
            TextSignBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).requiresCorrectToolForDrops(), false);
    public static final Block CRT_TV = registerBlock("crt_tv",
            TvBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).requiresCorrectToolForDrops(), false);

    private static BlockBehaviour.Properties createMoistSilkPlanksBlock(Block sourceBlock) {
        return BlockBehaviour.Properties.ofFullCopy(sourceBlock).sound(SoundType.WOOD).strength(2f);
    }

    private static BlockBehaviour.Properties createMarkedConcreteBlock() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.CONCRETE.black()).sound(SoundType.STONE);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> blockConstructor, BlockBehaviour.Properties properties, Boolean itemless) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name));
        Block block = blockConstructor.apply(properties.setId(key));

        if (!itemless) {
            BackroomsBlocks.registerBlockItem(name, block);
        }

        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name), block);
    }

    private static Block registerBlock(String name, BlockBehaviour.Properties properties) {
        return registerBlock(name, Block::new, properties, false);
    }

    private static Block registerTape(String name) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WIRE);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name));
        Block block = new TapeBlock(properties.setId(key));

        BackroomsBlocks.registerBlockItem(name, block);

        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name));
        Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix()));
    }

    public static void registerBlocks() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Blocks initialized");
    }
}
