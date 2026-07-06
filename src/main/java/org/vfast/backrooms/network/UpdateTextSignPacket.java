package org.vfast.backrooms.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;

public record UpdateTextSignPacket(BlockPos pos, Boolean isFrontText, String text) implements CustomPacketPayload {
    public static final Identifier UPDATE_TEXT_SIGN_PACKET_ID = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "update_text_sign");
    public static final CustomPacketPayload.Type<UpdateTextSignPacket> TYPE = new CustomPacketPayload.Type<>(UPDATE_TEXT_SIGN_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTextSignPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, UpdateTextSignPacket::pos, ByteBufCodecs.BOOL, UpdateTextSignPacket::isFrontText, ByteBufCodecs.STRING_UTF8, UpdateTextSignPacket::text, UpdateTextSignPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
