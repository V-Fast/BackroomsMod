package com.lumination.backrooms.mixin;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.screens.SettingsScreen;
import com.lumination.backrooms.utils.BetterText;
import com.lumination.backrooms.utils.Color;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Unique
    private ClickableWidget settingsButton;

    protected TitleScreenMixin(Text title) {
        super(title);
        BackroomsModClient.setStartDate();
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    public void init(CallbackInfo ci) throws IOException {
        assert this.client != null;

        BackroomsMod.changeName(Text.translatable("mod.backrooms.name").getString());
        Discord.setPresence("On the title screen", "", "mod");

        int l = this.height / 4 + 48;
        this.settingsButton = this.addDrawableChild(ButtonWidget.builder(new BetterText("A", BetterText.TextType.LITERAL).withColor(Color.Backrooms.blue), (button) -> {
            this.client.setScreen(new SettingsScreen().getScreen(this));
        }).dimensions(this.width / 2 + 104, l + 48, 20, 20).build());

        this.settingsButton.active = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
        if (!this.settingsButton.active) {
            this.settingsButton.setTooltip(Tooltip.of(Text.translatable("mod.yacl.disabled")));
        }
    }
}
