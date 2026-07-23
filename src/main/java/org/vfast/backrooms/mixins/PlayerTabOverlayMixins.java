package org.vfast.backrooms.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixins {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract void setVisible(boolean visible);

    @Inject(method = "getPlayerInfos", at = @At("RETURN"), cancellable = true)
    private void getNearbyPlayerInfos(CallbackInfoReturnable<List<PlayerInfo>> cir) {
        boolean isInBackrooms = BackroomsLevels.isBackrooms(this.minecraft.level.dimension());
        this.setVisible(!isInBackrooms);

        if (isInBackrooms) {
            cir.setReturnValue(List.of());
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
