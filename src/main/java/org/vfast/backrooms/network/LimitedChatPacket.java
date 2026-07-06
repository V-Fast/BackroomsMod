package org.vfast.backrooms.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;

public record LimitedChatPacket(String content) implements CustomPacketPayload {
    public static final Identifier LIMITED_CHAT_PACKET_ID = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "limited_chat");
    public static final CustomPacketPayload.Type<LimitedChatPacket> TYPE = new CustomPacketPayload.Type<>(LIMITED_CHAT_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, LimitedChatPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, LimitedChatPacket::content, LimitedChatPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
