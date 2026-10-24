package org.vfast.backrooms.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.vfast.backrooms.blocks.ThresholdBlock;
import org.vfast.backrooms.network.ThresholdFadePacket;
import org.vfast.backrooms.sounds.BackroomsSounds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/** Runs the delayed teleportation of a player using a {@link ThresholdBlock}: fade to black, teleport, fade back in */
public final class ThresholdTeleporter {
    public static final int FADE_IN_TICKS = 40;
    public static final int HOLD_TICKS = 20;
    public static final int FADE_OUT_TICKS = 40;

    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    private ThresholdTeleporter() {}

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ThresholdTeleporter::tick);
    }

    public static boolean isTeleporting(ServerPlayer player) {
        return PENDING.containsKey(player.getUUID());
    }

    /** @return `false` if this player is already teleporting */
    public static boolean start(ServerPlayer player, ServerLevel level, BlockPos threshold) {
        if (isTeleporting(player)) return false;

        PENDING.put(player.getUUID(), new Pending(level.dimension(), threshold, FADE_IN_TICKS));

        ServerPlayNetworking.send(player, new ThresholdFadePacket(FADE_IN_TICKS, HOLD_TICKS, FADE_OUT_TICKS));
        // only that player hears it
        player.connection.send(new ClientboundSoundPacket(BackroomsSounds.THRESHOLD, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1.0f, 1.0f, RandomSource.create().nextLong()));
        return true;
    }

    private static void tick(MinecraftServer server) {
        Iterator<Map.Entry<UUID, Pending>> iterator = PENDING.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Pending> entry = iterator.next();
            Pending pending = entry.getValue();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());

            if (player == null || !player.isAlive()) {
                iterator.remove();
                continue;
            }

            pending.remainingTicks--;
            if (pending.remainingTicks > 0) continue;

            iterator.remove();
            ThresholdTeleporter.teleport(player, pending);
        }
    }

    private static void teleport(ServerPlayer player, Pending pending) {
        // leaving the level or breaking the block during the fade cancels the teleportation
        if (player.level().dimension() != pending.level) return;

        ServerLevel level = player.level();
        if (!(level.getBlockState(pending.pos).getBlock() instanceof ThresholdBlock threshold)) return;

        TeleportTransition transition = threshold.getPortalDestination(level, player, pending.pos);
        if (transition != null) {
            player.teleport(transition);
        }
    }

    private static final class Pending {
        private final ResourceKey<Level> level;
        private final BlockPos pos;
        private int remainingTicks;

        private Pending(ResourceKey<Level> level, BlockPos pos, int remainingTicks) {
            this.level = level;
            this.pos = pos;
            this.remainingTicks = remainingTicks;
        }
    }
}
