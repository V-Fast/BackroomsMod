package org.vfast.backrooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import org.vfast.backrooms.client.huds.CameraRecordHud;
import org.vfast.backrooms.client.settings.BackroomsSettings;
import org.vfast.backrooms.entities.BackroomsEntities;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud camHud = new CameraRecordHud();

    @Override
    public void onInitializeClient() {
        // Client Only
        FabricLoader.getInstance().getModContainer(BackroomsMod.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(BackroomsMod.MOD_ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
        });
        BackroomsSettings.loadConfig();
        BackroomsEntities.registerRenderers();
        HudRenderCallback.EVENT.register(BackroomsModClient.camHud);
        BackroomsMod.LOGGER.info("Initialized Client Backrooms");
    }
}