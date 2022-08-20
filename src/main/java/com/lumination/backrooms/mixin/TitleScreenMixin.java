package com.lumination.backrooms.mixin;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.screens.SettingsScreen;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    public void init(CallbackInfo ci) {
        BackroomsMod.changeName(Text.translatable("mod.backrooms.name").getString());

        int l = this.height / 4 + 48;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 104, l + 48, 20, 20, Text.literal("A").formatted(Formatting.BLUE), (button) -> {
            this.client.setScreen(new SettingsScreen().getScreen(this));
        }));
    }
}
