package org.vfast.backrooms.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.entity.BackroomsBlockEntities;
import org.vfast.backrooms.blocks.entity.render.TextSignBlockEntityRenderer;
import org.vfast.backrooms.client.render.entity.StillLifeRenderer;
import org.vfast.backrooms.entity.BackroomsEntities;

import java.util.List;

public class BackroomsModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.initRenderers();
    }

    private void initRenderers() {
        BlockEntityRenderers.register(BackroomsBlockEntities.TEXT_SIGN_ENTITY, TextSignBlockEntityRenderer::new);
        BackroomsMod.LOGGER.info("[BackroomsMod] Text Sign renderer initialized");

        BackroomsModelLayers.registerModelLayers();
        EntityRendererRegistry.register(BackroomsEntities.STILL_LIFE, StillLifeRenderer::new);
        BackroomsMod.LOGGER.info("[BackroomsMod] Still Life renderer initialized");
    }
}
