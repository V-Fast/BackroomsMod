package org.vfast.backrooms.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.vfast.backrooms.sounds.BackroomsSounds;

public class CamcorderItem extends Item {
    public CamcorderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.swing(hand);
        ItemStack stack = player.getItemInHand(hand);
        stack.set(BackroomsComponents.VHS_COMPONENT, !stack.getOrDefault(BackroomsComponents.VHS_COMPONENT, false));

//        if (!level.isClientSide()) {
//            level.playPlayerSound(BackroomsSounds.CAMERA_CLICK); TODO: FINISH THIS BITCH ASS
//        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.CROSSBOW;
    }
}
