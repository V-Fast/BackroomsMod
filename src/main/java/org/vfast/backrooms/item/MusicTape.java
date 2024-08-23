package org.vfast.backrooms.item;

import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;

public class MusicTape extends Item {
    public SoundEvent MUSIC;

    public MusicTape(Settings settings, SoundEvent music) {
        super(settings.rarity(Rarity.RARE).maxCount(1));
        this.MUSIC = music;
    }

    /// This only starts the song in the world
    public void play(ServerWorld world, BlockPos position) {
        if (!world.isClient) {
            world.playSound(null, position, this.MUSIC, SoundCategory.RECORDS, 0.75f, 1f);
            world.getServer().getPlayerManager().broadcast(Text.translatable("record.nowPlaying", Text.translatable(this.getTranslationKey() + ".desc").getString()).formatted(Formatting.YELLOW), true);
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
}
