package org.vfast.backrooms.attachments;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.vfast.backrooms.BackroomsMod;

import java.util.List;

public class BackroomsAttachments {
    public static final AttachmentType<List<ItemStack>> SAVED_INVENTORY = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(BackroomsMod.ID, "saved_inventory"),
            builder -> builder
                    .persistent(ItemStack.OPTIONAL_CODEC.listOf())
                    .syncWith(ItemStack.OPTIONAL_LIST_STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                    .copyOnDeath()
    );

    public static final AttachmentType<BlockPos> SAVED_SPAWN = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(BackroomsMod.ID, "saved_spawn"),
            builder -> builder
                    .persistent(BlockPos.CODEC)
                    .syncWith(BlockPos.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                    .copyOnDeath()
    );

    public static final AttachmentType<Boolean> LOADING_WORLD = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(BackroomsMod.ID, "loading_world"),
            builder -> builder.initializer(() -> false)
    );

    public static void register() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Attachments registered");
    }
}
