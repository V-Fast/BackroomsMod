package com.lumination.backrooms.mixin;

import com.lumaa.libu.Color;
import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.BackroomsModClient;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.screens.SettingsScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
        BackroomsModClient.setStartDate();
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    public void init(CallbackInfo ci) {
        BackroomsMod.changeName(Text.translatable("mod.backrooms.name").getString());
        Discord.setPresence("On the title screen", "", "mod");

        int l = this.height / 4 + 48;
        Text a = Text.literal("A");
        if (FabricLoader.getInstance().isModLoaded("libu")) {
            this.addDrawableChild(ButtonWidget.builder(a.getWithStyle(a.getStyle().withColor(Color.Backrooms.blue)).get(0), (button) -> {
                this.client.setScreen(new SettingsScreen().getScreen(this));
            })
                    .dimensions(this.width / 2 + 104, l + 48, 20, 20)
                    .build());
        }
    }
}
