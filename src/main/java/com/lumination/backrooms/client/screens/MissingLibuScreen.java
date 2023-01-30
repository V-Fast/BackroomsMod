package com.lumination.backrooms.client.screens;

import com.lumaa.libu.update.ModrinthMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class MissingLibuScreen extends Screen {
    private final Text notice;
    private MultilineText noticeLines;

    public MissingLibuScreen(Text title) {
        super(title);
        this.notice = Text.translatable("mod.libu.missing");
    }

    @Override
    protected void init() {
        super.init();
        this.noticeLines = MultilineText.create(this.textRenderer, this.notice, this.width - 50);
        int var10000 = this.noticeLines.count();
        Objects.requireNonNull(this.textRenderer);
        int i = var10000 * 9;
        int j = MathHelper.clamp(90 + i + 12, this.height / 6 + 96, this.height - 24);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.PROCEED, (button) -> {
            ModrinthMod libu = new ModrinthMod(Text.translatable("mod.libu.name").getString(), "libu", "1.0.0");

            libu.openMod();
            MinecraftClient.getInstance().close();
        })
        .dimensions((this.width - 150) / 2, j + 15, 150, 20)
        .build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawTitle(matrices);
        int i = this.width / 2 - this.noticeLines.getMaxWidth() / 2;
        this.noticeLines.drawWithShadow(matrices, i, 70, this.getLineHeight(), 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void drawTitle(MatrixStack matrices) {
        drawTextWithShadow(matrices, this.textRenderer, this.title, 25, 30, 16777215);
    }

    protected int getLineHeight() {
        Objects.requireNonNull(this.textRenderer);
        return 9 * 2;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
