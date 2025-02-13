package org.vfast.backrooms.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.util.GameOptionsAccessor;

@Environment(EnvType.CLIENT)
@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin extends GameOptionsScreen {
    public AccessibilityOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "getOptions", at = @At("RETURN"))
    private static void injectDefaultPanorama(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        if (gameOptions instanceof GameOptionsAccessor) {
            cir.setReturnValue(new SimpleOption[]{gameOptions.getNarrator(), gameOptions.getShowSubtitles(), gameOptions.getHighContrast(), gameOptions.getAutoJump(), gameOptions.getMenuBackgroundBlurriness(), gameOptions.getTextBackgroundOpacity(), gameOptions.getBackgroundForChatOnly(), gameOptions.getChatOpacity(), gameOptions.getChatLineSpacing(), gameOptions.getChatDelay(), gameOptions.getNotificationDisplayTime(), gameOptions.getBobView(), gameOptions.getSneakToggled(), gameOptions.getSprintToggled(), gameOptions.getDistortionEffectScale(), gameOptions.getFovEffectScale(), gameOptions.getDarknessEffectScale(), gameOptions.getDamageTiltStrength(), gameOptions.getGlintSpeed(), gameOptions.getGlintStrength(), gameOptions.getHideLightningFlashes(), gameOptions.getMonochromeLogo(), gameOptions.getPanoramaSpeed(), gameOptions.getHideSplashTexts(), gameOptions.getNarratorHotkey(), ((GameOptionsAccessor) gameOptions).getDefaultPanorama()});
        } else {
            cir.getReturnValue();
        }
    }
}
