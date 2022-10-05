package com.lumination.backrooms.items.interactables;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.screens.SilkBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class SilkedBook extends Item {
    public SilkedBook(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Word word = null;
        if (!itemStack.hasNbt()) {
            Random r = new Random();
            word = Word.getWordByCode(r.nextInt(Word.values().length + 1));
            itemStack.getNbt().putInt("InscriptionCode", word.code);
        } else {
            word = Word.getWordByCode(itemStack.getNbt().getInt("InscriptionCode"));
        }

        // open gui
        if (world.isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new SilkBookScreen(word));
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
