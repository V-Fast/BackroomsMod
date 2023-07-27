package com.lumination.backrooms.utils;

import net.minecraft.util.math.MathHelper;

public class Color {
    public static final int white = fromRGB(255, 255, 255);
    public static final int black = fromRGB(0, 0, 0);
    /**
     * The branded color of Lumaa
     */
    public static final int brand = fromRGB(248, 189, 74);

    public static int fromRGB(int r, int g, int b) {
        return ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
    }

    /**
     * Colors used in <a href="https://modrinth.com/mod/backrooms">The Backrooms Mod</a>
     */
    public static class Backrooms {
        public static final int blue = fromRGB(39, 54, 242);
    }

    public record RGB(int r, int g, int b) {

        public RGB(int r, int g, int b) {
            this.r = MathHelper.clamp(r, 0, 255);
            this.g = MathHelper.clamp(g, 0, 255);
            this.b = MathHelper.clamp(b, 0, 255);
        }

        public static RGB zero() {
            return new RGB(0, 0, 0);
        }
    }
}
