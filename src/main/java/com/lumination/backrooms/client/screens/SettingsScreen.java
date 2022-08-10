package com.lumination.backrooms.client.screens;


import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.levels.Backroom;
import com.lumination.backrooms.mixin.TitleScreenMixin;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsScreen {
    public Screen screen;
    public ConfigBuilder builder = ConfigBuilder.create();

    private void styleBuilder(@Nullable Screen parent) {
        // on click save
        builder.setSavingRunnable(() -> {
            BackroomsSettings.saveConfig();
        });

        // styling
        builder
                .setDoesConfirmSave(true)
                .setTitle(Text.translatable("mod.backrooms.name"))
                .setShouldListSmoothScroll(true)
                .setShouldTabsSmoothScroll(true);

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("mod.backrooms.name"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.backrooms.disable_spawning"), BackroomsSettings.canSpawnMobs())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.backrooms.disable_spawning.tooltip"))
                .setSaveConsumer(newValue -> {
                    BackroomsSettings.setSpawnMobs(newValue); // had to do a "setSpawnMobs"
                })
                .build());

        if (parent != null) {
            builder.setParentScreen(parent);
        }
    }

    public Screen getScreen(@Nullable Screen parent) {
        styleBuilder(parent);
        return builder.build();
    }

    /**
     * Shows the screen directly, with no interruption
     * @param parent
     */
    public void show(Screen parent) {
        styleBuilder(parent);

        // convert
        this.screen = builder.build();
        MinecraftClient.getInstance().setScreen(this.screen);
    }
}
