package com.lumination.backrooms.items.interactables;

import com.lumaa.libu.generation.GenerationCore;
import com.lumaa.libu.generation.MazeCore;
import com.lumination.backrooms.blocks.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

import java.util.List;

// Developer note: USE ONLY FOR TESTING PARTICULAR FUNCTIONS
public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        super.useOnBlock(context);

        MazeCore mazeCore = new MazeCore(ModBlocks.MOIST_CARPET, ModBlocks.MOIST_SILK, ModBlocks.DROPPED_CEILING, 3);
        mazeCore.setWallsVariants(List.of(ModBlocks.MOIST_SILK_PLANKS));
        mazeCore.setWallsVariantsAmount(GenerationCore.VariantAmount.LOW);
        mazeCore.generate(context.getWorld(), context.getBlockPos());

        return ActionResult.PASS;
    }
}
