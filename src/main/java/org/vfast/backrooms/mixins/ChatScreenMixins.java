package org.vfast.backrooms.mixins;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vfast.backrooms.network.LimitedChatPacket;
import org.vfast.backrooms.world.BackroomsLevels;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixins extends Screen {
    protected ChatScreenMixins(Component title) {
        super(title);
    }

    @Redirect(method = "handleChatInput(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;sendChat(Ljava/lang/String;)V"))
    private void overridePacket(ClientPacketListener instance, String content) {
        if (this.minecraft.level.dimension() == BackroomsLevels.LEVEL_0) {
            ClientPlayNetworking.send(new LimitedChatPacket(content));
        } else {
            this.minecraft.player.connection.sendChat(content);
        }
    }
}
