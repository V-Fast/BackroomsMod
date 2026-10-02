package org.vfast.backrooms.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.jspecify.annotations.Nullable;
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
        InteractionHand usedHand = this.minecraft.player.getUsedItemHand();
        ItemStack stack = this.minecraft.player.getItemInHand(usedHand);

        List<Identifier> currEffects = this.minecraft.player.getActivePostEffects();
        if (stack.getOrDefault(BackroomsComponents.VHS_COMPONENT, false)) {
            currEffects.add(VHS_SHADER);
        } else currEffects.remove(VHS_SHADER);

        this.minecraft.player.setActivePostEffects(currEffects);
    }
}
