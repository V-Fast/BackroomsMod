package org.vfast.backrooms;

import org.vfast.backrooms.client.hud.CameraRecordHud;
import org.vfast.backrooms.client.settings.BackroomsSettings;
import org.vfast.backrooms.entity.BackroomsEntities;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

@ClientOnly
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();

    @Override
    public void onInitializeClient(ModContainer mod) {
        // Client Only
        QuiltLoader.getModContainer(BackroomsMod.MOD_ID).ifPresent(modContainer -> {
            ResourceLoader.registerBuiltinResourcePack(new Identifier(BackroomsMod.MOD_ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
        });
        BackroomsSettings.loadConfig();
        BackroomsEntities.registerRenderers();
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);
        BackroomsMod.LOGGER.info("Initialized Client Backrooms");
    }
}