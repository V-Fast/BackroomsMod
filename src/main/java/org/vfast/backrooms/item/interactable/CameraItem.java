package org.vfast.backrooms.item.interactable;

import org.vfast.backrooms.BackroomsModClient;
import org.vfast.backrooms.sound.BackroomsSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CameraItem extends Item {

    public CameraItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        world.playSound(user.getX(), user.getY(), user.getZ(), BackroomsSounds.CAMERA_CLICK, SoundCategory.PLAYERS, 0.5f, 1f, true);
        if (!world.isClient && !user.isSpectator()) {
            if (itemStack.hasNbt()) {
                NbtCompound nbt = itemStack.getNbt();
                setRecord(itemStack, !nbt.getBoolean("records"));
            } else {
                setRecord(itemStack, true);
            }
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity.isPlayer()) {
            if (world.isClient) {
                BackroomsModClient.cameraHud.updateVisible();
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void setRecord(ItemStack itemStack, boolean record) {
        if (!itemStack.hasNbt()) {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("records", record);
            itemStack.setNbt(nbt);
        } else {
            NbtCompound nbt = itemStack.getNbt();
            nbt.putBoolean("records", record);
            itemStack.setNbt(nbt);
        }
    }
}
