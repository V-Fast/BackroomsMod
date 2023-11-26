package org.vfast.backrooms.client.huds;

import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.client.settings.BackroomsSettings;
import org.vfast.backrooms.items.BackroomsItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class CameraRecordHud implements HudRenderCallback {
    private static Identifier recordHud;
    private static Identifier vhsHud;
    private boolean visible = false;

    public void updateVisible() {
        boolean v = false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        Hand hand = player.getActiveHand();
        ItemStack itemStack = player.getStackInHand(hand);
        boolean holdingItem = itemStack.getItem() == BackroomsItems.CAMERA;
        if (holdingItem && !itemStack.isEmpty() && itemStack.hasNbt()) {
            assert itemStack.getNbt() != null;
            if (itemStack.getNbt().getBoolean("records") && !player.isSpectator()) v = true;
        }
        this.visible = v;
    }

    public boolean getVisible() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        Hand hand = player.getActiveHand();
        if (player.isSpectator() && player.getStackInHand(hand).getItem() != BackroomsItems.CAMERA) return false;

        return this.visible;
    }

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (!this.getVisible()) return;
        int width = 0;
        int height = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            width = client.getWindow().getScaledWidth();
            height = client.getWindow().getScaledHeight();
        }

        this.registerHud();
        this.renderOverlay(BackroomsSettings.canShowRecord() ? recordHud : vhsHud, width, height);
    }

    private void renderOverlay(Identifier texture, int width, int height) {
        this.renderImage(texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, width, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(width, height, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(width, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
    }

    private void renderImage(Identifier texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    public void registerHud() {
        CameraRecordHud.recordHud = new Identifier(BackroomsMod.MOD_ID, "textures/hud/vhs_record.png");
        CameraRecordHud.vhsHud = new Identifier(BackroomsMod.MOD_ID, "textures/hud/vhs.png");
    }
}
