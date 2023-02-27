package com.lumination.backrooms.client.screens;

import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SettingsScreen {
    public YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();

    private void styleBuilder() {
        // on click save
        builder.save(() -> {
            BackroomsSettings.saveConfig();
            if (!BackroomsSettings.hasDiscordPresence()) {
                Discord.shutdown();
            } else {
                Discord.initDiscord();
                BackroomsRPC.loadingRpc();
            }
        });

        // styling & actions
        builder
                .title(Text.translatable("mod.backrooms.name"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("option.backrooms.general"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Discord"))
                                .collapsed(false)
                                .option(Option.createBuilder(boolean.class)
                                        .controller(TickBoxController::new)
                                        .name(Text.translatable("option.backrooms.enable_discord"))
                                        .tooltip(Text.translatable("option.backrooms.enable_discord.tooltip"))
                                        .binding(true, () -> BackroomsSettings.hasDiscordPresence(), newVal -> BackroomsSettings.setDiscordPresence(newVal))
                                        .build())
                                .option(Option.createBuilder(String.class)
                                        .controller(StringController::new)
                                        .name(Text.translatable("option.backrooms.discord_label"))
                                        .tooltip(Text.translatable("option.backrooms.discord_label.tooltip"))
                                        .binding("By Lumaa", () -> BackroomsSettings.discordLabel(), newVal -> BackroomsSettings.setDiscordLabel(newVal))
                                        .available(BackroomsSettings.hasDiscordPresence())
                                        .build())
                                .build())
                        .build()
                )
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("option.backrooms.dev"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("option.backrooms.explain_errors"))
                                .tooltip(Text.translatable("option.backrooms.explain_errors.tooltip"))
                                .binding(false, () -> BackroomsSettings.explainsError(), newVal -> BackroomsSettings.setExplainError(newVal))
                                .controller(BooleanController::new)
                                .build())
                        .build());
    }

    public Screen getScreen(@Nullable Screen parent) {
        styleBuilder();
        return builder.build().generateScreen(parent);
    }
}