package org.vfast.backrooms.items;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.BackroomsBlocks;

public class BackroomsItemsGroup {
    public static final CreativeModeTab MAIN = registerItemGroup(FabricCreativeModeTab.builder()
            .icon(BackroomsItems.SILK::getDefaultInstance)
            .title(Component.translatable("itemGroup.backrooms.backrooms"))
            .displayItems((_, tab) -> {
                // items

                // Overworld

                // Level 0
                tab.accept(BackroomsItems.SILK);

                // Level 1
                tab.accept(BackroomsItems.DECAYING_BRICK);

                tab.accept(BackroomsItems.CAMCORDER);

                // blocks

                // Overworld
                tab.accept(BackroomsBlocks.TEXT_SIGN);
                tab.accept(BackroomsBlocks.BACKROOMS_PORTAL);
                tab.accept(BackroomsBlocks.FAKE_BLOCK);

                // Level 0
                tab.accept(BackroomsBlocks.MOIST_SILK);
                tab.accept(BackroomsBlocks.MOIST_SILK_OAK_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_BIRCH_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_SPRUCE_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_JUNGLE_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_ACACIA_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_DARK_OAK_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_WARPED_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_BAMBOO_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_CHERRY_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_CRIMSON_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_SILK_MANGROVE_PLANKS);
                tab.accept(BackroomsBlocks.MOIST_CARPET);
                tab.accept(BackroomsBlocks.DROPPED_CEILING);
                tab.accept(BackroomsBlocks.FLUORESCENT_LIGHT);
                tab.accept(BackroomsBlocks.AIR_VENT);
                tab.accept(BackroomsBlocks.WOOLY_CHAIR);
                tab.accept(BackroomsBlocks.CRT_TV);
                tab.accept(BackroomsBlocks.BLUE_TAPESTRY);
                tab.accept(BackroomsBlocks.STRIPED_BLUE_TAPESTRY);

                // Level 1

                tab.accept(BackroomsBlocks.SCRATCHED_CONCRETE);
                tab.accept(BackroomsBlocks.SCRATCHED_CONCRETE_STAIRS);
                tab.accept(BackroomsBlocks.STAINED_CONCRETE);
                tab.accept(BackroomsBlocks.STAINED_CONCRETE_STAIRS);
                tab.accept(BackroomsBlocks.DECAYING_BRICKS);
                tab.accept(BackroomsBlocks.SMOOTH_IRON);
                tab.accept(BackroomsBlocks.STREET_LIGHT);
                tab.accept(BackroomsBlocks.MARKED_A_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_B_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_C_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_D_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_E_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_F_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_G_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_H_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_I_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_J_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_K_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_L_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_M_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_N_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_O_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_P_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_Q_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_R_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_S_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_T_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_U_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_V_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_W_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_X_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_Y_CONCRETE);
                tab.accept(BackroomsBlocks.MARKED_Z_CONCRETE);
            })
            .build());

    public static CreativeModeTab registerItemGroup(CreativeModeTab tab) {
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(BackroomsMod.ID, "backrooms"));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return tab;
    }

    public static void registerItemGroups() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Item groups initialized");
    }
}
