package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.generation.GenerationCore;
import com.lumination.backrooms.generation.MazeCore;
import com.lumination.backrooms.generation.type.ShapeType;
import com.lumination.backrooms.generation.type.StructureType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class MazeTool extends Item {
    public MazeTool(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        MazeCore mazeCore = new MazeCore(Blocks.PODZOL, Blocks.STONE, Blocks.BEDROCK, 2);
        mazeCore.setWallsVariants(List.of(Blocks.GRANITE, Blocks.COBBLESTONE));
        mazeCore.setSize(3, 3);
        mazeCore.setHeight(3);
        mazeCore.setShape(ShapeType.BOX);
        mazeCore.setType(StructureType.BLOCKY);
        GenerationCore.setBreakNonAir(true, true);

        mazeCore.generate(context.getWorld(), context.getBlockPos());
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.empty()); // Jump To Line
        tooltip.add(getDescription());
    }

    private MutableText getDescription() {
        return Text.translatable(this.getTranslationKey() + ".desc").formatted(Formatting.GRAY);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
