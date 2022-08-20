package com.lumination.backrooms.mixin;

// Code from https://github.com/Parzivail-Modding-Team/HereBeNoDragons/blob/master/src/main/java/com/parzivail/herebenodragons/mixin/LevelPropertiesMixin.java

import com.lumination.backrooms.BackroomsMod;
import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
    @Shadow
    @Final
    private Lifecycle lifecycle;

    @Inject(method = "getLifecycle()Lcom/mojang/serialization/Lifecycle;", at = @At("HEAD"), cancellable = true)
    private void getLifecycle(CallbackInfoReturnable<Lifecycle> cir)
    {
        if (lifecycle == Lifecycle.experimental())
        {
            BackroomsMod.print("Suppressing EXPERIMENTAL level lifecycle");
            cir.setReturnValue(Lifecycle.stable());
            cir.cancel();
        }
    }
}
