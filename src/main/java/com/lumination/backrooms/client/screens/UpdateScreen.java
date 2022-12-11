package com.lumination.backrooms.client.screens;

import com.lumination.backrooms.BackroomsModClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class UpdateScreen extends Screen {
    private final Text notice = Text.translatable("info.backrooms.update.description", BackroomsModClient.versionName, BackroomsModClient.latestVersion.get("name").toString().replace("\"", ""));
    private MultilineText noticeLines;
    private final Text buttonText = Text.translatable("info.backrooms.update.button");
    private final boolean shouldCloseOnEsc = true;

    public UpdateScreen() {
        super(Text.translatable("info.backrooms.update.title").formatted(Formatting.BOLD));
    }

    @Override
    protected void init() {
        super.init();
        this.noticeLines = MultilineText.create(this.textRenderer, this.notice, this.width - 50);
        int var10000 = this.noticeLines.count();
        Objects.requireNonNull(this.textRenderer);
        int i = var10000 * 9;
        int j = MathHelper.clamp(90 + i + 12, this.height / 6 + 96, this.height - 24);
        this.addDrawableChild(new ButtonWidget((this.width - 150) / 2, j, 150, 20, this.buttonText, (button) -> {
            Util.getOperatingSystem().open("https://modrinth.com/mod/backrooms/version/" + BackroomsModClient.latestVersion.get("version_number").toString().replace("\"", ""));
        }));
        this.addDrawableChild(new ButtonWidget((this.width - 150) / 2, j + 30, 150, 20, ScreenTexts.NO, (button) -> {
            this.client.setScreen(new TitleScreen());
        }));
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
        return this.shouldCloseOnEsc;
    }
}