package org.vfast.backrooms.mixin;

import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.vfast.backrooms.config.BackroomsConfig;

@Mixin(IntegratedServerLoader.class)
public abstract class IntegratedServerLoaderMixin {
    @ModifyVariable(method = "start(Lnet/minecraft/world/level/storage/LevelStorage$Session;Lcom/mojang/serialization/Dynamic;ZZLjava/lang/Runnable;)V",
            at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private boolean modifyCanShowBackupPrompt(boolean canShowBackupPrompt) {
        return BackroomsConfig.HANDLER.instance().showExperimentalSettingsScreen && canShowBackupPrompt;
    }


}
