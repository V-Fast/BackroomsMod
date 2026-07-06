package org.vfast.backrooms.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.BackroomsBlocks;
import org.vfast.backrooms.client.gui.InvisiScreen;
import org.vfast.backrooms.interfaces.GameRendererGetter;
import org.vfast.backrooms.interfaces.Suffocator;
import org.vfast.backrooms.world.BackroomsLevels;

@Mixin(ClientPacketListener.class)
public abstract class ImmersiveDimensions extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {
    @Shadow
    private @Nullable LevelLoadTracker levelLoadTracker;

    protected ImmersiveDimensions(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
        super(minecraft, connection, cookie);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void clearSuffocation(CallbackInfo ci) {
        if (this.levelLoadTracker == null) {
            this.simulateSuffocation((BlockState) null);
        } else if (this.levelLoadTracker.serverProgress() >= 1.0) {
            this.simulateSuffocation((BlockState) null);
        }
    }

    @Inject(method = "startWaitingForNewLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"), cancellable = true)
    private void startImmersion(LocalPlayer player, ClientLevel level, LevelLoadingScreen.Reason reason, CallbackInfo ci) {
        this.fullyImmersed(level, reason, ci);
        this.minecraft.setScreen(new InvisiScreen(this.levelLoadTracker));
    }

    @Inject(method = "startWaitingForNewLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;update(Lnet/minecraft/client/multiplayer/LevelLoadTracker;Lnet/minecraft/client/gui/screens/LevelLoadingScreen$Reason;)V"))
    private void updateImmersion(LocalPlayer player, ClientLevel level, LevelLoadingScreen.Reason reason, CallbackInfo ci) {
        this.fullyImmersed(level, reason, ci);
    }

    @Inject(method = "determineLevelLoadingReason", at = @At(value = "HEAD"), cancellable = true)
    private void levelReason(boolean playerDied, ResourceKey<Level> dimensionKey, ResourceKey<Level> oldDimensionKey, CallbackInfoReturnable<LevelLoadingScreen.Reason> cir) {
        if (!playerDied && (oldDimensionKey == Level.OVERWORLD && dimensionKey == BackroomsLevels.LEVEL_0)) {
            cir.setReturnValue(LevelLoadingScreen.Reason.NETHER_PORTAL); // as long as it's something else than OTHER
        }
    }

    @Unique
    private void fullyImmersed(ClientLevel level, LevelLoadingScreen.Reason reason, CallbackInfo ci) {
        assert this.levelLoadTracker != null;

        if (BackroomsLevels.LEVEL_0.equals(level.dimension()) && reason != LevelLoadingScreen.Reason.OTHER) {
            BackroomsMod.LOGGER.info("[BackroomsMod+ImmersiveDimensions] Prevented screen change");
            this.simulateSuffocation(BackroomsBlocks.MOIST_SILK);
            if (this.levelLoadTracker.isLevelReady()) {
                this.simulateSuffocation((BlockState) null);
            }
            ci.cancel();
        }
    }

    @Unique
    private void simulateSuffocation(@Nullable BlockState blockState) {
        GameRenderer renderer = this.minecraft.gameRenderer;
        ScreenEffectRenderer screenRenderer = ((GameRendererGetter) renderer).getScreenEffectRenderer();
        if (blockState != null) {
            TextureAtlasSprite suffocateSprite = this.minecraft.getModelManager().getBlockStateModelSet().getParticleMaterial(blockState).sprite();
            ((Suffocator) screenRenderer).setSuffocating(suffocateSprite);
        } else {
            ((Suffocator) screenRenderer).setSuffocating(null);
        }
    }

    @Unique
    private void simulateSuffocation(@Nullable Block block) {
        this.simulateSuffocation(block != null ? block.defaultBlockState() : null);
    }
}
