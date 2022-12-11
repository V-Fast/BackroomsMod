package com.lumination.backrooms.client.screens;


import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.interactables.SilkenBook.Word;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SilkBookScreen extends Screen {
    private Word word;

    public SilkBookScreen(Word word) {
        super(Text.translatable("item.backrooms.silken_book"));
        this.word = word;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (button) -> {
            this.client.setScreen((Screen)null);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Identifier wordTexture = this.word.getTexture();
        Identifier bookTexture = new Identifier(BackroomsMod.MOD_ID, "textures/screen/silken_book.png");
        int i = (this.width - 192) / 2;

        renderImage(bookTexture);
        this.drawTexture(matrices, i, 2, 0, 0, 192, 192);

        renderImage(wordTexture);
        this.drawTexture(matrices, i, 2, 0, 1, 192, 192);
    }

    private void renderImage(Identifier texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
