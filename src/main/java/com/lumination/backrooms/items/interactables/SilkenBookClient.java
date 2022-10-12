package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.interactables.SilkenBook.Word;
import com.lumination.backrooms.client.screens.SilkBookScreen;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class SilkenBookClient extends Item {
    public SilkenBookClient(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Word word = null;
        if (world.isClient) {
            if (!itemStack.hasNbt()) {
                Random r = new Random();
                int x = r.nextInt(Word.values().length + 1);
                word = Word.getWordByCode(x);

                // prevent crash
                if (word == null) {
                    BackroomsMod.print("Recurrent anomaly");
                    user.sendMessage(Text.literal("Please click again."), true);
                    if (BackroomsSettings.explainsError()) {
                        user.sendMessage(Text.literal("[The Backrooms - Error] The problem is occurs when selecting a random inscription. A fix has not been found yet.").formatted(Formatting.GRAY, Formatting.ITALIC));
                    }
                    return TypedActionResult.fail(itemStack);
                }

                NbtCompound itemNbt = new NbtCompound();
                itemNbt.putInt("InscriptionCode", x);
                itemStack.setNbt(itemNbt);
            } else {
                word = Word.getWordByCode(itemStack.getNbt().getInt("InscriptionCode"));
            }

            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new SilkBookScreen(word));
        }

        return TypedActionResult.success(itemStack, true);
    }
}
