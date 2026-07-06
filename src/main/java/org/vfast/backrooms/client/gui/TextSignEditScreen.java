package org.vfast.backrooms.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.blocks.entity.TextSignBlockEntity;

public class TextSignEditScreen extends AbstractTextScreen<TextSignBlockEntity> {
    public TextSignEditScreen(TextSignBlockEntity blockEntity, BlockPos pos, boolean isFrontText) {
        Identifier texture = Identifier.fromNamespaceAndPath(BackroomsMod.ID, "textures/item/ceiling_sign.png");
        super(blockEntity, pos, isFrontText, texture, Component.translatable("sign.edit"));
    }

    @Override
    public void renderText(GuiGraphicsExtractor graphics) {
        float width = this.font.width(this.text);
        float denominator = width > 10 ? width / 8.2f : width / 6.0f;

        graphics.pose().translate(0.0f, 108.0f + denominator * 3.5f);
        graphics.pose().scale(7.5f / denominator, 7.5f / denominator);
        graphics.text(this.font, this.text, -this.font.width(this.text) / 2, 0, 0xff000000, false);
    }

    @Override
    public float getYOffset() {
        return 0;
    }
}
