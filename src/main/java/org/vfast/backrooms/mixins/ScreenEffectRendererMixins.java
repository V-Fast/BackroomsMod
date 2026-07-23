package org.vfast.backrooms.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    @Final
    private Minecraft minecraft;

    @Shadow
    private static void submitBlockSprite(TextureAtlasSprite sprite, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int color) {}

    @Unique
    @Nullable
    private BlockState suffocatingState;

    @Inject(method = "submit", at = @At(value = "HEAD"))
    private void suffocatingIn(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui, CallbackInfo ci) {
        if (this.suffocatingState != null) {
            PoseStack poseStack = new PoseStack();
            BlockStateModelSet blockStateModelSet = this.minecraft.getModelManager().getBlockStateModelSet();
            TextureAtlasSprite sprite = blockStateModelSet.getParticleMaterial(this.suffocatingState).sprite();
            submitBlockSprite(sprite, poseStack, submitNodeCollector, -15132391);
        }
    }

    @Unique
    public void setSuffocating(@Nullable BlockState state) {
        this.suffocatingState = state;
    }
}
