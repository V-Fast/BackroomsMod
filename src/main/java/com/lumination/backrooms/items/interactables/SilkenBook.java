package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.screens.SilkBookScreen;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.SERVER)
public class SilkenBook extends Item {
    public SilkenBook(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Word word = null;
        if (!world.isClient) {
            if (!itemStack.hasNbt()) {
                Random r = new Random();
                int x = r.nextInt(Word.values().length);
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
            }
        }
        if (world.isClient) {
            //MinecraftClient.getInstance().setScreen(new SilkBookScreen(word));
        }

        return TypedActionResult.success(itemStack, true);
    }

    public enum Word {
        LightOfDay(0),
        Missing(1),
        Water(2),
        Eye(3),
        A(4),
        AloneSos(5),
        Empty(6),
        SCRATCH(7),
        JustBzz(8),
        Basement(9),
        Car(10),
        SOS(11),
        Green(12),
        Blue(13),
        Deeper(14),
        HangAlone(15),
        LightsOff(16),
        EndlessWalls(17);

        // constructor
        private int code;

        Word(int code) {
            this.code = code;
        }

        public static Word getWordByCode(int code) {
            for (Word word : Word.values()) {
                if (word.code == code) {
                    return word;
                }
            }
            return null;
        }

        public Identifier getTexture() {
            return new Identifier(BackroomsMod.MOD_ID, "textures/screen/words/" + this.code + ".png");
        }
    }
}
