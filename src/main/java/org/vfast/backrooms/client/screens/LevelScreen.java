package org.vfast.backrooms.client.screens;

import org.vfast.backrooms.levels.Backroom;
import org.vfast.backrooms.utils.Color;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class LevelScreen extends Screen {
    public Text info;
    public Backroom level;
    private MultilineText text = MultilineText.EMPTY;

    public LevelScreen(Backroom level) {
        super(Text.translatable("mod.backrooms.name"));
        this.level = level;
        if (this.level.isBackroom()) {
            this.info = Text.translatable(String.format("levels.backrooms.level_%s.details", level.level));
        } else {
            this.info = Text.translatable("levels.backrooms.overworld.details");
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.level.isBackroom()) {
            this.info = level.getLevelDetails();
        } else {
            this.info = Text.translatable("levels.backrooms.overworld.details");
        }
        this.renderBackgroundTexture(context);
        context.getMatrices().push();
        context.getMatrices().scale(2.0F, 2.0F, 2.0F);
        context.drawCenteredTextWithShadow(this.textRenderer, this.level.getName(), this.width / 2 / 2, 30, Color.white);
        context.getMatrices().pop();

        this.text = MultilineText.create(this.textRenderer, this.info, this.width / 2);
        this.text.drawCenterWithShadow(context, this.width / 2, this.height / 2 + 30, 20, Color.white);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
