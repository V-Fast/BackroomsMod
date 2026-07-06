package org.vfast.backrooms.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.interfaces.TextBlockEntity;
import org.vfast.backrooms.world.BackroomsGameRules;

import java.util.List;

public class BackroomsNetworking {
    public static void registerNetwork() {
        BackroomsNetworking.registerPackets();
        BackroomsNetworking.registerReceivers();

        BackroomsMod.LOGGER.info("[BackroomsMod] Networking registered!");
    }

    private static void registerPackets() {
        PayloadTypeRegistry.serverboundPlay().register(UpdateTextSignPacket.TYPE, UpdateTextSignPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LimitedChatPacket.TYPE, LimitedChatPacket.CODEC);
    }

    private static void registerReceivers() {
        // update text sign
        ServerPlayNetworking.registerGlobalReceiver(UpdateTextSignPacket.TYPE, (payload, context) -> {
            ServerLevel level = context.player().level();
            BlockEntity be = level.getBlockEntity(payload.pos());

            if (be instanceof TextBlockEntity) {
                ((TextBlockEntity) be).updateText(payload.text(), payload.isFrontText());
            } else {
                BackroomsMod.LOGGER.warn("[BackroomsMod] UpdateTextSignPacket used on non-TextBlockEntity");
            }
        });

        // limited chat
        ServerPlayNetworking.registerGlobalReceiver(LimitedChatPacket.TYPE, (payload, context) -> {
            if (!context.player().level().getGameRules().get(BackroomsGameRules.LIMITED_CHATTING)) {
                context.player().connection.sendDisguisedChatMessage(Component.literal(payload.content()), ChatType.bind(ChatType.CHAT, context.player()));
                return;
            }

            BlockPos senderPos = context.player().blockPosition();
            float radius = 30.0f;

            List<ServerPlayer> receivers = context.player().level().getEntitiesOfClass(
                    ServerPlayer.class,
                    new AABB(senderPos).inflate(radius),
                    player -> player.distanceToSqr(Vec3.atCenterOf(senderPos)) <= radius * radius
            );

            if (receivers.size() == 1) {
                receivers.getFirst().sendSystemMessage(Component.translatable("message.backrooms.chat_alone").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            } else {
                for (ServerPlayer receiver : receivers) {
                    receiver.connection.sendDisguisedChatMessage(Component.literal(payload.content()), ChatType.bind(ChatType.CHAT, context.player()));
                }
            }
        });
    }
}
