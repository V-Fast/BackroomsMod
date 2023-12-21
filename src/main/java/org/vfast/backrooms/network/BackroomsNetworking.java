package org.vfast.backrooms.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsNetworking {
    public static final Identifier SANITY_SYNC_ID = new Identifier(BackroomsMod.ID, "sanity_sync");

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SANITY_SYNC_ID, SanitySyncDataS2CPacket::receive);
    }
}
