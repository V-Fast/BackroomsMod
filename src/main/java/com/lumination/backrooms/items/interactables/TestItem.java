package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.utils.NoClipper.EntityClip;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

// Developer note: USE ONLY FOR TESTING PARTICULAR FUNCTIONS
public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        EntityClip nc = new EntityClip(user);
        nc.startClipping();

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
