package org.vfast.backrooms.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;

/** Tells a client to fade its screen to black, hold it, then fade back in. All durations are in ticks */
public record ThresholdFadePacket(int fadeIn, int hold, int fadeOut) implements CustomPacketPayload {
    public static final Identifier THRESHOLD_FADE_PACKET_ID = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "threshold_fade");
    public static final CustomPacketPayload.Type<ThresholdFadePacket> TYPE = new CustomPacketPayload.Type<>(THRESHOLD_FADE_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ThresholdFadePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ThresholdFadePacket::fadeIn,
            ByteBufCodecs.VAR_INT, ThresholdFadePacket::hold,
            ByteBufCodecs.VAR_INT, ThresholdFadePacket::fadeOut,
            ThresholdFadePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
