package org.vfast.backrooms.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.util.GameOptionsAccessor;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin implements GameOptionsAccessor {
    private SimpleOption<Boolean> defaultPanorama;
    private static final Text DEFAULT_PANORAMA_TOOLTIP = Text.translatable("tooltip.default_panorama.option");

    @Inject(method = "load", at = @At("HEAD"))
    private void defaultPanoramaInit(CallbackInfo ci) {
        this.defaultPanorama = SimpleOption.ofBoolean("options.defaultPanorama", SimpleOption.constantTooltip(DEFAULT_PANORAMA_TOOLTIP), false);
    }

    public SimpleOption<Boolean> getDefaultPanorama() { return this.defaultPanorama; }
}
