package com.lumination.backrooms.client.huds;

import com.lumination.backrooms.BackroomsMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CameraRecordHud extends DrawableHelper {
    private static final Identifier RECORD_HUD = new Identifier(BackroomsMod.MOD_ID + "textures/hud/vhs.png");

    private boolean visible = false;
    private final MinecraftClient client;
    private int gameWidth;
    private int gameHeight;
    private float recScale;

    public CameraRecordHud(MinecraftClient client) {
        this.client = client;
    }

    public void render(MatrixStack matrices) {
        this.gameWidth = this.client.getWindow().getScaledWidth();
        this.gameHeight = this.client.getWindow().getScaledHeight();
        RenderSystem.enableBlend();
        float frameDuration = this.client.getLastFrameDuration();
        this.recScale = MathHelper.lerp(0.5F * frameDuration, this.recScale, 1.125F);
        if (visible && this.client.options.getPerspective().isFirstPerson() && !this.client.player.isSpectator()) {
            this.recordHud(this.recScale);
        }
    }

    // minecraft code
    public void recordHud(float scale) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, RECORD_HUD);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = (float)Math.min(this.gameWidth, this.gameHeight);
        float h = Math.min((float)this.gameWidth / f, (float)this.gameHeight / f) * scale;
        float i = f * h;
        float j = f * h;
        float k = ((float)this.gameWidth - i) / 2.0F;
        float l = ((float)this.gameHeight - j) / 2.0F;
        float m = k + i;
        float n = l + j;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex((double)k, (double)n, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex((double)m, (double)n, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex((double)m, (double)l, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex((double)k, (double)l, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0D, (double)this.gameHeight, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, (double)this.gameHeight, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0D, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0D, (double)l, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, (double)l, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, 0.0D, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0D, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)k, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)k, (double)l, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0D, (double)l, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)m, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, (double)n, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)this.gameWidth, (double)l, -90.0D).color(0, 0, 0, 255).next();
        bufferBuilder.vertex((double)m, (double)l, -90.0D).color(0, 0, 0, 255).next();
        tessellator.draw();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public void setVisible(boolean visibility) {
        visible = visibility;
    }

    public boolean getVisible() {
        return visible;
    }
}
