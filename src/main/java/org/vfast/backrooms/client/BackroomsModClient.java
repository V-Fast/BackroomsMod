package org.vfast.backrooms.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.entity.BackroomsBlockEntities;
import org.vfast.backrooms.blocks.entity.render.TextSignBlockEntityRenderer;

public class BackroomsModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.initRenderers();
    }

    private void initRenderers() {
        BlockEntityRenderers.register(BackroomsBlockEntities.TEXT_SIGN_ENTITY, TextSignBlockEntityRenderer::new);
        BackroomsMod.LOGGER.info("[BackroomsMod] Text Sign renderer initialized");
    }
}
