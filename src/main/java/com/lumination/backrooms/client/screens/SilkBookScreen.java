package com.lumination.backrooms.client.screens;


import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.interactables.SilkenBook.Word;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SilkBookScreen extends Screen {

    private final Word word;

    public SilkBookScreen(Word word) {
        super(Text.translatable("item.backrooms.silken_book"));
        if (word == null) {
            word = Word.getWordByCode(0);
        }
        this.word = word;
    }

    public SilkBookScreen() {
        super(Text.translatable("item.backrooms.silken_book"));
        this.word = Word.getWordByCode(0);
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(
            ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
                assert this.client != null;
                this.client.setScreen(null);
            }).dimensions(this.width / 2 - 100, 196, 200, 20).build()
        );
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier bookTexture = new Identifier(BackroomsMod.MOD_ID, "textures/screen/silken_book.png");
        Identifier wordTexture = this.word.getTexture();
        int i = (this.width - 192) / 2;

        this.renderImage(bookTexture);
        context.drawTexture(bookTexture, i, 2, 0, 0, 192, 192);

        this.renderImage(wordTexture);
        context.drawTexture(wordTexture, i, 2, 0, 1, 192, 192);
    }

    private void renderImage(Identifier texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
