package org.vfast.backrooms.attachments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.storage.LevelData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlayerSnapshot {
    private PlayerSnapshot() {}

    // 36 inv + 4 armor + 1 offhand
    private static final int INVENTORY_SIZE = 41;

    public static void saveAndClear(Player player) {
        if (PlayerSnapshot.hasSavedData(player)) return;

        player.setAttached(BackroomsAttachments.SAVED_INVENTORY, capture(player));
        player.getInventory().clearContent();
        if (player instanceof ServerPlayer) {
            ServerPlayer.RespawnConfig respawn = ((ServerPlayer) player).getRespawnConfig();
            if (respawn != null) {
                player.setAttached(BackroomsAttachments.SAVED_SPAWN, respawn.respawnData().pos());
            }
        }
    }

    public static void restore(Player player) {
        List<ItemStack> snapshot = player.getAttachedOrElse(BackroomsAttachments.SAVED_INVENTORY, Collections.emptyList());
        if (snapshot.isEmpty()) return;

        apply(player, snapshot);
        player.removeAttached(BackroomsAttachments.SAVED_INVENTORY);
        if (player.hasAttached(BackroomsAttachments.SAVED_SPAWN)) {
            BlockPos spawn = player.getAttached(BackroomsAttachments.SAVED_SPAWN);
            if (player instanceof ServerPlayer) {
                GlobalPos spawnPos = GlobalPos.of(Level.OVERWORLD, spawn);
                ServerPlayer.RespawnConfig config = new ServerPlayer.RespawnConfig(new LevelData.RespawnData(spawnPos, 0.0f, 0.0f), true);
                ((ServerPlayer) player).setRespawnPosition(config, false);
                player.removeAttached(BackroomsAttachments.SAVED_SPAWN);
            }
        }
    }

    private static List<ItemStack> capture(Player player) {
        Inventory inv = player.getInventory();
        List<ItemStack> slots = new ArrayList<>(INVENTORY_SIZE);

        for (ItemStack itemStack : inv) {
            slots.add(itemStack);
        }

        return slots;
    }

    private static void apply(Player player, List<ItemStack> slots) {
        Inventory inv = player.getInventory();
        inv.clearContent();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = slots.get(i).copy();
            inv.setItem(i, item);
        }
    }

    private static boolean hasSavedData(Player player) {
        return player.hasAttached(BackroomsAttachments.SAVED_INVENTORY);
    }
}
