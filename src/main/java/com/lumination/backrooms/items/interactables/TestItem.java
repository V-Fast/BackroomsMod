package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.utils.NoClipper.NoClip;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NoClip nc = new NoClip(user, world.getSpawnPos());
        nc.start();

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
