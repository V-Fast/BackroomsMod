package org.vfast.backrooms.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.interfaces.GameRendererGetter;
import org.vfast.backrooms.items.BackroomsComponents;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixins implements AutoCloseable, TrackedWaypoint.Projector, GameRendererGetter {
    @Unique
    private static final Identifier VHS_SHADER = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "vhs");

    @Shadow
    @Final
    private ScreenEffectRenderer screenEffectRenderer;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Override
    public ScreenEffectRenderer getScreenEffectRenderer() {
        return this.screenEffectRenderer;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickVhs(CallbackInfo ci) {
        LocalPlayer player = this.minecraft.player;
        if (player == null) return;

        boolean filming = player.getMainHandItem().getOrDefault(BackroomsComponents.VHS_COMPONENT, false)
                || player.getOffhandItem().getOrDefault(BackroomsComponents.VHS_COMPONENT, false);

        // setActivePostEffects clears the list it is given, which is the player's own list: always pass a copy
        List<Identifier> effects = new ArrayList<>(player.getActivePostEffects());
        if (filming && !effects.contains(VHS_SHADER)) {
            effects.add(VHS_SHADER);
            player.setActivePostEffects(effects);
        } else if (!filming && effects.remove(VHS_SHADER)) {
            player.setActivePostEffects(effects);
        }
    }
}
