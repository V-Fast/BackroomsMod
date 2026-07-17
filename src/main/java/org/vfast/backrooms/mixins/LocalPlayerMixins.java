package org.vfast.backrooms.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.blocks.entity.TextSignBlockEntity;
import org.vfast.backrooms.client.gui.TextSignEditScreen;
import org.vfast.backrooms.interfaces.GuiOpener;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.List;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixins extends AbstractClientPlayer implements GuiOpener {
    @Shadow
    @Final
    protected Minecraft minecraft;

    private LocalPlayerMixins(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "canStartSprinting", at = @At(value = "HEAD"), cancellable = true)
    private void forceStopSprint(CallbackInfoReturnable<Boolean> cir) {
        assert this.minecraft.level != null;

        if (this.minecraft.level.dimension() == BackroomsLevels.LEVEL_0 && !this.getAbilities().invulnerable) {
            this.setSprinting(false);
            cir.setReturnValue(false);
        }
    }

    @Override
    public void openTextSignEdit(TextSignBlockEntity blockEntity, BlockPos pos, boolean isFront) {
        TextSignEditScreen editScreen = new TextSignEditScreen(blockEntity, pos, isFront);
        this.minecraft.setScreen(editScreen);
    }
}
