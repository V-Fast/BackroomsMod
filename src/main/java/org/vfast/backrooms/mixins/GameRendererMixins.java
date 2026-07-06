package org.vfast.backrooms.mixins;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.vfast.backrooms.interfaces.GameRendererGetter;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixins implements AutoCloseable, TrackedWaypoint.Projector, GameRendererGetter {
    @Shadow
    @Final
    private ScreenEffectRenderer screenEffectRenderer;

    @Override
    public ScreenEffectRenderer getScreenEffectRenderer() {
        return this.screenEffectRenderer;
    }
}
