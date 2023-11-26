package org.vfast.backrooms.items.interactables;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackroomsGenCore extends Item {
    public BackroomsGenCore(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        super.useOnBlock(context);
        if (!context.getPlayer().getMainHandStack().hasNbt()) return ActionResult.FAIL;
        if (context.getPlayer().isSneaking()) return super.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();

        int type = context.getPlayer().getMainHandStack().getNbt().getInt("type");
        if (type == 0) {
            // full-on rewrite planned
            return ActionResult.FAIL;
        } else if (type == 1) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()) {
            tooltip.add(Text.translatable("item.backrooms.backrooms_core.desc", stack.getNbt().getInt("type")).formatted(Formatting.GRAY));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !world.isClient) {
            ItemStack holding = user.getMainHandStack();
            if (!holding.hasNbt()) {
                NbtCompound nbt = new NbtCompound();
                holding.setNbt(nbt);
            }
            if (holding.getNbt().isEmpty() || !holding.getNbt().contains("type")) {
                holding.getNbt().putInt("type", 0);
            } else {
                holding.getNbt().putInt("type", scroll(holding.getNbt().getInt("type") + 1));
            }
            user.sendMessage(Text.translatable("item.backrooms.backrooms_core.use", holding.getNbt().getInt("type")).formatted(Formatting.GRAY), true);
        }
        return TypedActionResult.fail(user.getMainHandStack());
    }

    private static int scroll(int value) {
        if (value > 1 || value < 0) {
            if (value > 1) {
                value = 0;
            } else {
                value = 1;
            }
        }
        return value;
    }
}
