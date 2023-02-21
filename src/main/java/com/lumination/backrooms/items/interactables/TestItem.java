package com.lumination.backrooms.items.interactables;

import com.lumaa.libu.generation.GenerationCore;
import com.lumaa.libu.generation.MazeCore;
import com.lumaa.libu.util.BetterText;
import com.lumaa.libu.util.Color;
import com.lumination.backrooms.blocks.ModBlocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
        mazeCore.setSize(16, 16);
        mazeCore.generate(context.getWorld(), context.getBlockPos());

        return ActionResult.PASS;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.empty());
        tooltip.add(new BetterText("This is an experimental item.", BetterText.TextType.LITERAL).withColor(Color.brand));
    }
}
