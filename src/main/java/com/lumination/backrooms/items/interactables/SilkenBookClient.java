package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.client.screens.SilkBookScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class SilkenBookClient extends Item {
    public SilkenBookClient(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        SilkenBook.Word word;

        if (world.isClient()) {
            //word = SilkenBook.Word.getWordByCode(itemStack.getNbt().getInt("InscriptionCode"));
            //MinecraftClient.getInstance().setScreen(new SilkBookScreen(word));
        }
        return TypedActionResult.success(itemStack, true);
    }
}
