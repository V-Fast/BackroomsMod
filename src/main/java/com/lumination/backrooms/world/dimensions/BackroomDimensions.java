package com.lumination.backrooms.world.dimensions;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.ModItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BackroomDimensions {
    public static void registerDims() {
        BackroomDimensions.level(new Identifier(BackroomsMod.MOD_ID, "level_0"), Blocks.IRON_BLOCK, ModItems.SILK, 195, 180, 10);
        BackroomDimensions.level(new Identifier(BackroomsMod.MOD_ID, "level_1"), Blocks.GRAY_CONCRETE, ModItems.WRENCH, RGB.zero());
        BackroomsMod.print("Registered custom dimensions and portals");
    }

    private static PortalLink level(Identifier dimId, Block frame, Item lighter, RGB portalColor) {
        BackroomsMod.print("Registered custom portal of " + dimId.getNamespace());
        return CustomPortalBuilder
                .beginPortal()
                .destDimID(dimId)
                .frameBlock(frame)
                .lightWithItem(lighter)
                .tintColor(portalColor.r, portalColor.g, portalColor.b)
                .onlyLightInOverworld()
                .registerPortal();
    }

    public static PortalLink level(Identifier dimId, Block frame, Item lighter, int r, int g, int b) {
        return BackroomDimensions.level(dimId, frame, lighter, new RGB(r, g, b));
    }

    private record RGB(int r, int g, int b) {

        private RGB(int r, int g, int b) {
            this.r = MathHelper.clamp(r, 0, 255);
            this.g = MathHelper.clamp(g, 0, 255);
            this.b = MathHelper.clamp(b, 0, 255);
        }

        private static RGB zero() {
            return new RGB(0, 0, 0);
        }
    }
}

