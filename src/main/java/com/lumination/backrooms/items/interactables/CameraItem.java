package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.BackroomsMod;

import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.huds.CameraRecordHud;
import com.lumination.backrooms.sounds.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CameraItem extends Item {
    public boolean onUse = false;
    private CameraRecordHud hud = BackroomsModClient.CameraHud;
    // private float battery = 1f;

    public CameraItem(Settings settings) {
        super(settings);
        hud.setVisible(false);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //world.playSound(user.getX(), user.getY(), user.getZ(), ModSounds.CAMERA_CLICK, SoundCategory.PLAYERS, 0.5f, 1f, true);
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient) {
            if (onUse) {
                if (!user.isSpectator()) {
                    BackroomsMod.print("On");
                    hud.setVisible(true);
                    onUse = false;
                } else {
                    BackroomsMod.print("Held off but on");
                    hud.setVisible(false);
                    onUse = true;
                }
            } else {
                BackroomsMod.print("Off");
                hud.setVisible(false);
                onUse = true;
            }
        }

        return TypedActionResult.success(itemStack);
    }

    public boolean isOn() {
        return this.onUse;
    }
}
