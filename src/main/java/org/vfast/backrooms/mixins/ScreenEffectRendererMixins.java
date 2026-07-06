package org.vfast.backrooms.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.interfaces.Suffocator;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixins implements Suffocator {

    @Shadow
    @Final
    private MultiBufferSource bufferSource;

    @Unique
    @Nullable
    private TextureAtlasSprite suffocatingSprite;

    @Inject(method = "renderScreenEffect", at = @At(value = "HEAD"))
    private void suffocatingIn(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui, CallbackInfo ci) {
        if (this.suffocatingSprite != null) {
            this.suffocateEffect(this.suffocatingSprite, new PoseStack());
        }
    }

    @Override
    public void setSuffocating(@Nullable final TextureAtlasSprite sprite) {
        this.suffocatingSprite = sprite;
    }

    private void suffocateEffect(final TextureAtlasSprite sprite, final PoseStack poseStack) {
        int color = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.1F);
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        Matrix4f pose = poseStack.last().pose();
        VertexConsumer builder = this.bufferSource.getBuffer(RenderTypes.blockScreenEffect(sprite.atlasLocation()));
        builder.addVertex(pose, -1.0F, -1.0F, -0.5F).setUv(u1, v1).setColor(color);
        builder.addVertex(pose, 1.0F, -1.0F, -0.5F).setUv(u0, v1).setColor(color);
        builder.addVertex(pose, 1.0F, 1.0F, -0.5F).setUv(u0, v0).setColor(color);
        builder.addVertex(pose, -1.0F, 1.0F, -0.5F).setUv(u1, v0).setColor(color);
    }
}
