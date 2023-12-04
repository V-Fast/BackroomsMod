package org.vfast.backrooms.item.interactable;

import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.client.screen.SilkBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class SilkenBook extends Item {

    public SilkenBook(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Word word;
        if (world.isClient()) {
             word = SilkenBook.Word.getWordByCode(itemStack.getOrCreateNbt().getInt("InscriptionCode"));
             MinecraftClient.getInstance().setScreen(new SilkBookScreen(word));

        } else {
            if (!itemStack.hasNbt()) {
                Random r = new Random();
                int x = r.nextInt(Word.values().length);
                word = Word.getWordByCode(x);

                // Prevent Crash
                if (word == null) {
                    BackroomsMod.LOGGER.info("Recurrent anomaly");
                    user.sendMessage(Text.literal("Please click again."), true);
                    return TypedActionResult.fail(itemStack);
                }

                NbtCompound itemNbt = new NbtCompound();
                itemNbt.putInt("InscriptionCode", x);
                itemStack.setNbt(itemNbt);
            }
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

        // Constructor
        private final int code;

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
            return new Identifier(BackroomsMod.ID, "textures/screen/words/" + this.code + ".png");
        }
    }
}
