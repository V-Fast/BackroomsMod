package com.lumination.backrooms.client.huds;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.items.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CameraRecordHud implements HudRenderCallback {
    private static Identifier RECORD_HUD;
    private static Identifier VHS_HUD;
    private boolean visible = false;

    public void updateVisible() {
        boolean v = false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Hand hand = player.getActiveHand();
        ItemStack itemStack = player.getStackInHand(hand);
        boolean holdingItem = itemStack.getItem() == ModItems.CAMERA;
        if (holdingItem &&
                itemStack.hasNbt() &&
                itemStack.getNbt().getBoolean("records") &&
                !player.isSpectator()
        ) v = true;
        visible = v;
    }

    public boolean getVisible() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Hand hand = player.getActiveHand();
        if (player.isSpectator() && player.getStackInHand(hand).getItem() != ModItems.CAMERA) return false;

        return visible;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!getVisible()) return;
        int width = 0;
        int height = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            width = client.getWindow().getScaledWidth();
            height = client.getWindow().getScaledHeight();
        }

        this.registerHud();
        this.renderOverlay(BackroomsSettings.canShowRecord() ? RECORD_HUD : VHS_HUD, 1f, width, height);
    }

    // minecraft code
    // weird hud bug when opening another hud
    private void renderOverlay(Identifier texture, float opacity, int width, int height) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        RenderSystem.setShaderTexture(0, texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, width, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(width, height, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(width, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
    }

    public void registerHud() {
        this.RECORD_HUD = new Identifier(BackroomsMod.MOD_ID, "textures/hud/vhs_record.png");
        this.VHS_HUD = new Identifier(BackroomsMod.MOD_ID, "textures/hud/vhs.png");
    }
}
