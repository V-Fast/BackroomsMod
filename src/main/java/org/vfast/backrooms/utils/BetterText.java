package org.vfast.backrooms.utils;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class BetterText {
    private Text text;

    public BetterText(String string, TextType type) {
        if (type.isTranslation) {
            this.text = Text.translatable(string);
        } else {
            this.text = Text.literal(string);
        }
    }

    public BetterText(String string) {
        this.text = Text.translatable(string);
    }

    public Text withColor(int rgbColor) {
        return this.text.getWithStyle(this.text.getStyle().withColor(rgbColor)).get(0);
    }

    public Text withColor(TextColor color) {
        return this.text.getWithStyle(this.text.getStyle().withColor(color)).get(0);
    }

    public Text withColor(Formatting color) {
        return this.text.getWithStyle(this.text.getStyle().withColor(color)).get(0);
    }

    public enum TextType {
        LITERAL(false),
        TRANSLATION(true);

        public final boolean isTranslation;

        TextType(boolean isTranslation) {
            this.isTranslation = isTranslation;
        }
    }
}
