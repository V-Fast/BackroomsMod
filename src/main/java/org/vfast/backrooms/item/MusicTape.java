package org.vfast.backrooms.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MusicTape extends Item {
    public SoundEvent MUSIC;
    public String FULL_NAME;
    public ImmersionLevel IMMERSION;

    public MusicTape(Settings settings, SoundEvent music, String fullName, ImmersionLevel immersion) {
        super(settings.rarity(Rarity.RARE).maxCount(1));
        this.MUSIC = music;
        this.FULL_NAME = fullName;
        this.IMMERSION = immersion;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(this.FULL_NAME).formatted(Formatting.GRAY));
        tooltip.add(Text.empty());
        tooltip.add(this.IMMERSION.toText().formatted(this.IMMERSION.toColor()));


        super.appendTooltip(stack, context, tooltip, type);
    }

    /// This only starts the song in the world
    public void play(ServerWorld world, BlockPos position) {
        if (!world.isClient) {
            world.playSound(null, position, this.MUSIC, SoundCategory.RECORDS, 0.75f, 1f);
            world.getServer().getPlayerManager().broadcast(Text.translatable("record.nowPlaying", Text.literal(this.FULL_NAME).getString()).formatted(Formatting.YELLOW), true);
        }
    }

    /// This only stops the song in the world
    public void stop(ServerWorld world) {
        if (!world.isClient) {
            MinecraftServer server = world.getServer();
            StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(this.MUSIC.getId(), null);
            server.getPlayerManager().getPlayerList().forEach(player1 -> player1.networkHandler.sendPacket(stopSoundS2CPacket));
        }
    }

    public enum ImmersionLevel {
        LOW,
        MEDIUM,
        HIGH;

        public MutableText toText() {
            return switch (this) {
                case LOW -> Text.translatable("immersion.backrooms.low");
                case MEDIUM -> Text.translatable("immersion.backrooms.medium");
                case HIGH -> Text.translatable("immersion.backrooms.high");
            };
        }

        public Formatting toColor() {
            return switch (this) {
                case LOW -> Formatting.GREEN;
                case MEDIUM -> Formatting.YELLOW;
                case HIGH -> Formatting.RED;
            };
        }
    }
}
