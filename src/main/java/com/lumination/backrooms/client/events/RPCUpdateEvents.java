package com.lumination.backrooms.client.events;

import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class RPCUpdateEvents implements ClientPlayConnectionEvents.Join, ClientPlayConnectionEvents.Init, ClientPlayConnectionEvents.Disconnect, ClientTickEvents.StartTick, ClientTickEvents.EndTick {
    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), handler.getPlayerList().size());
        BackroomsModClient.setStartDate();
    }

    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        Discord.setPresence("On the title screen", "", "mod");
        BackroomsModClient.setStartDate();
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        Discord.setPresence("On the title screen", "", "mod");
        BackroomsModClient.setStartDate();
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
        BackroomsModClient.setStartDate();
    }

    @Override
    public void onPlayInit(ClientPlayNetworkHandler handler, MinecraftClient client) {
        BackroomsRPC.customLabelRpc("Playing on " + handler.getServerInfo().name, handler.getPlayerList().size(), handler.getPlayerList().size());
    }
}
