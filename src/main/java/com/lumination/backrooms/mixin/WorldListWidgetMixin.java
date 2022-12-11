package com.lumination.backrooms.mixin;

import com.lumination.backrooms.client.BackroomsRPC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldListWidget.WorldEntry.class)
public class WorldListWidgetMixin {

    @Inject(at = @At("HEAD"), method = "play")
    public void changeRpc(CallbackInfo ci) {
        BackroomsRPC.customLabelRpc("Playing Singleplayer", 1, 1);
    }
}
