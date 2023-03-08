package com.lumination.backrooms.client.screens;

import com.lumination.backrooms.client.BackroomsRPC;
import com.lumination.backrooms.client.Discord;
import com.lumination.backrooms.client.settings.BackroomsSettings;
import dev.isxander.yacl.api.*;
import dev.isxander.yacl.gui.controllers.ActionController;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

// Download Yet Another Config Lib here: https://modrinth.com/mod/yacl

@Environment(EnvType.CLIENT)
public class SettingsScreen {
    public YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
    private static boolean restarted = false;

    private void styleBuilder() {
        // on click save
        builder.save(() -> {
            BackroomsSettings.saveConfig();
            if (!BackroomsSettings.hasDiscordPresence() && Discord.isInitialized()) {
                Discord.shutdown();
            } else if (BackroomsSettings.hasDiscordPresence() && !Discord.isInitialized()) {
                Discord.initDiscord();
                BackroomsRPC.loadingRpc();
            }
        });

        Option restartRPC = ButtonOption.createBuilder()
                .name(Text.translatable("option.backrooms.restart_rpc"))
                .tooltip(Text.translatable("option.backrooms.restart_rpc.tooltip"))
                .action(((yaclScreen, buttonOption) -> {
                    Discord.restart();
                    restarted = !restarted;
                    buttonOption.setAvailable(!restarted); // if restarted once than false
                }))
                .controller(ActionController::new)
                .available(BackroomsSettings.hasDiscordPresence() && !restarted)
                .build();
        Option discordLabel = Option.createBuilder(String.class)
                .controller(StringController::new)
                .name(Text.translatable("option.backrooms.discord_label"))
                .tooltip(Text.translatable("option.backrooms.discord_label.tooltip"))
                .binding("By Lumaa", () -> BackroomsSettings.discordLabel(), newVal -> BackroomsSettings.setDiscordLabel(newVal))
                .available(BackroomsSettings.hasDiscordPresence())
                .build();
        Option hasDiscordRPC = Option.createBuilder(boolean.class)
                .controller(TickBoxController::new)
                .name(Text.translatable("option.backrooms.enable_discord"))
                .tooltip(Text.translatable("option.backrooms.enable_discord.tooltip"))
                .listener((opt, newVal) -> {
                    discordLabel.setAvailable(newVal);
                    restartRPC.setAvailable(newVal && !restarted);
                })
                .binding(true, () -> BackroomsSettings.hasDiscordPresence(), newVal -> BackroomsSettings.setDiscordPresence(newVal))
                .build();

        // styling & actions
        builder
                .title(Text.translatable("mod.backrooms.name"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("option.backrooms.general"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Discord"))
                                .collapsed(false)
                                .option(hasDiscordRPC)
                                .option(restartRPC)
                                .option(discordLabel)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("GUI"))
                                .collapsed(false)
                                .option(Option.createBuilder(boolean.class)
                                        .controller(BooleanController::new)
                                        .binding(true, () -> BackroomsSettings.canShowRecord(), newVal -> BackroomsSettings.setShowRecord(newVal))
                                        .name(Text.translatable("option.backrooms.disable_record"))
                                        .tooltip(Text.translatable("option.backrooms.disable_record.tooltip"))
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