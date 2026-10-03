package org.vfast.backrooms.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
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
    private static void submitBlockSprite(Identifier atlasLocation, float u0, float v0, float u1, float v1, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int color) {
    }

    @Unique
    @Nullable
    private BlockState suffocatingState;

    @Inject(method = "submit", at = @At(value = "HEAD"))
    private void suffocatingIn(float partialTicks, SubmitNodeCollector submitNodeCollector, PlayerRenderState playerRenderState, CameraRenderState cameraRenderState, boolean hideGui, CallbackInfo ci) {
        if (this.suffocatingState != null) {
            PoseStack poseStack = new PoseStack();
            BlockStateModelSet blockStateModelSet = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
            TextureAtlasSprite sprite = blockStateModelSet.getParticleMaterial(this.suffocatingState).sprite();
            submitBlockSprite(sprite.atlasLocation(), sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), poseStack, submitNodeCollector, -15132391);
        }
    }

    @Unique
    public void setSuffocating(@Nullable BlockState state) {
        this.suffocatingState = state;
    }
}
