package org.vfast.backrooms.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.network.chat.Component;

public class InvisiScreen extends Screen {
    private LevelLoadTracker loadTracker;

    public InvisiScreen(LevelLoadTracker levelLoadTracker) {
        super(Component.empty());
        this.loadTracker = levelLoadTracker;
    }

    @Override
    public void tick() {
        if (this.loadTracker.isLevelReady()) {
            this.onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
