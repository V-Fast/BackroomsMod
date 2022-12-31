package com.lumination.backrooms.world.dimensions;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.ModItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class BackroomDimensions {


    public static void registerDims() {
        level(new Identifier(BackroomsMod.MOD_ID, "level_0"), Blocks.IRON_BLOCK, ModItems.SILK);
        BackroomsMod.print("Registered custom dimensions and portals");
    }

    private static PortalLink level(Identifier dimId, Block frame, Item lighter) {
        BackroomsMod.print("Registered custom portal of " + dimId);
        return CustomPortalBuilder
                .beginPortal()
                .destDimID(dimId)
                .frameBlock(frame)
                .lightWithItem(lighter)
                .tintColor(195, 180, 10)
                .onlyLightInOverworld()
                .registerPortal();
    }
}

