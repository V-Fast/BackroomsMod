package org.vfast.backrooms.network;

import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsNetworking {
    public static final Identifier SANITY_SYNC_ID = new Identifier(BackroomsMod.ID, "sanity_sync");

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SANITY_SYNC_ID, SanitySyncDataS2CPacket::receive);
    }
}
