package org.vfast.backrooms.client.screen;

import org.vfast.backrooms.client.settings.BackroomsSettings;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

// Download Yet Another Config Lib here: https://modrinth.com/mod/yacl

@ClientOnly
public class SettingsScreen {
    public YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();

    private void styleBuilder() {

        // On Click Save
        builder.save(BackroomsSettings::saveConfig);

        // Styling & Actions
        builder.title(Text.translatable("mod.backrooms.name"))
            .category(ConfigCategory.createBuilder()
                .name(Text.translatable("option.backrooms.general"))
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