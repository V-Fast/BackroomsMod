package com.lumination.backrooms.client.screens;

import com.lumination.backrooms.client.settings.BackroomsSettings;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

// Download Yet Another Config Lib here: https://modrinth.com/mod/yacl

@ClientOnly
public class SettingsScreen {
    public YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
    private static boolean restarted = false;

    private void styleBuilder() {

        // On Click Save
        builder.save(BackroomsSettings::saveConfig);
        Option<String> discordLabel = Option.createBuilder(String.class)
            .controller(StringControllerBuilder::create)
            .name(Text.translatable("option.backrooms.discord_label"))
            .description(OptionDescription.of(Text.translatable("option.backrooms.discord_label.tooltip")))
            .binding("By Lumaa", BackroomsSettings::discordLabel, BackroomsSettings::setDiscordLabel)
            .available(BackroomsSettings.hasDiscordPresence())
            .build();
        Option<Boolean> hasDiscordRPC = Option.createBuilder(boolean.class)
            .controller(TickBoxControllerBuilder::create)
            .name(Text.translatable("option.backrooms.enable_discord"))
            .description(OptionDescription.of(Text.translatable("option.backrooms.enable_discord.tooltip")))
            .listener((opt, newVal) -> discordLabel.setAvailable(newVal))
            .binding(true, BackroomsSettings::hasDiscordPresence, BackroomsSettings::setDiscordPresence)
            .build();

        // Styling & Actions
        builder.title(Text.translatable("mod.backrooms.name"))
            .category(ConfigCategory.createBuilder()
                .name(Text.translatable("option.backrooms.general"))
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Discord"))
                    .collapsed(false)
                    .option(hasDiscordRPC)
                    .option(discordLabel)
                    .build())
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("GUI"))
                    .collapsed(false)
                    .option(Option.createBuilder(boolean.class)
                        .controller(BooleanControllerBuilder::create)
                        .binding(true, BackroomsSettings::canShowRecord, BackroomsSettings::setShowRecord)
                        .name(Text.translatable("option.backrooms.disable_record"))
                        .description(OptionDescription.of(Text.translatable("option.backrooms.disable_record.tooltip")))
                        .build())
                    .build())
                .build()
            )
            .category(ConfigCategory.createBuilder()
                .name(Text.translatable("option.backrooms.dev"))
                .option(Option.createBuilder(boolean.class)
                    .name(Text.translatable("option.backrooms.explain_errors"))
                    .description(OptionDescription.of(Text.translatable("option.backrooms.explain_errors.tooltip")))
                    .binding(false, BackroomsSettings::explainsError, BackroomsSettings::setExplainError)
                    .controller(BooleanControllerBuilder::create)
                    .build())
                .build());
    }

    public Screen getScreen(@Nullable Screen parent) {
        styleBuilder();
        return builder.build().generateScreen(parent);
    }
}