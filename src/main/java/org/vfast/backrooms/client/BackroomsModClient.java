package org.vfast.backrooms.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Blocks;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.BackroomsBlocks;
import org.vfast.backrooms.blocks.entity.BackroomsBlockEntities;
import org.vfast.backrooms.blocks.entity.render.TextSignBlockEntityRenderer;

import java.util.List;

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
