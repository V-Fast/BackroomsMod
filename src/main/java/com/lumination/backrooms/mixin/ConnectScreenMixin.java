package com.lumination.backrooms.mixin;

import com.lumination.backrooms.client.BackroomsRPC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {
    protected ConnectScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "connect(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V")
    private static void changeRpc(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
        BackroomsRPC.customLabelRpc("Playing on " + info.name, 0, 0);
        //BackroomsRPC.customLabelRpc("Playing on " + info.name, Integer.parseInt(info.playerCountLabel.getString().split("/")[0]) + 1, Integer.parseInt(info.playerCountLabel.getString().split("/")[1]));
    }
}
