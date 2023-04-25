package com.lumination.backrooms.levels;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class Backroom {
    private String name;
    public int level;
    public SafetyLevels safety = SafetyLevels.UNKNOWN;
    private boolean overworld = false;

    public Backroom(int level, @Nullable SafetyLevels safety, @Nullable String name) {
        this.level = level;

        if (safety != null) {
            this.safety = safety;
        }
        if (name != null) {
            this.name = name;
        }
    }

    public Backroom(int level) {
        this.level = level;
        this.safety = SafetyLevels.UNKNOWN;
        this.name = null;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getName() {
        if (this.name != null) {
            return this.name;
        } else {
            return getLevelString();
        }
    }

    public final String getLevelString() {
        return String.format("Level %s", this.level);
    }

    public final void setSafetyLevel(SafetyLevels safety) {
        this.safety = safety;
    }

    public final Text getLevelDetails() {
        return Text.translatable(String.format("levels.backrooms.level_%s.details", this.level));
    }

    public final void isBackroom(boolean bool) {
        this.overworld = !bool;
    }

    public final boolean isBackroom() {
        return !this.overworld;
    }
}
