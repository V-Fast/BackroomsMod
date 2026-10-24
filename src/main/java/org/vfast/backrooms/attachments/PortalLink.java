package org.vfast.backrooms.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;

/** A portal block at `from` (in the level holding this link) that leads back to the portal block at `to` */
public record PortalLink(BlockPos from, GlobalPos to) {
    public static final Codec<PortalLink> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("from").forGetter(PortalLink::from),
            GlobalPos.CODEC.fieldOf("to").forGetter(PortalLink::to)
    ).apply(instance, PortalLink::new));
}
