package org.vfast.backrooms.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsConfig {

    public static ConfigClassHandler<BackroomsConfig> HANDLER = ConfigClassHandler.createBuilder(BackroomsConfig.class)
            .id(new Identifier(BackroomsMod.ID, "config_handler"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(BackroomsMod.ID+".json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // not needed, pretty print by default
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Whether to show the Experimental Settings screen when loading a world (client only)")
    public boolean showExperimentalSettingsScreen = false;

    @SerialEntry(comment = "Whether to show the \"Record\" gui when using the Camera (client only)")
    public boolean showRecord = true;

    @SerialEntry(comment = "The time it takes (in minutes) for a player to loose a single level of sanity while in the Backrooms")
    public int looseSanitySpeed = 5;

    @SerialEntry(comment = "The time it takes (in minutes) for a player to regain a single level of sanity while out of the Backrooms")
    public int recoverSanitySpeed = 1;

    public Screen getScreen(@Nullable Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.backrooms.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.backrooms.main"))
                        .tooltip(Text.translatable("config.backrooms.main.tooltip"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("config.backrooms.main.show_experimental_settings_screen"))
                                .description(OptionDescription.of(Text.translatable("config.backrooms.main.show_experimental_settings_screen.description")))
                                .binding(false, () -> this.showExperimentalSettingsScreen, newVal -> this.showExperimentalSettingsScreen = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.backrooms.main.visual_effects"))
                                .description(OptionDescription.of(Text.translatable("config.backrooms.main.visual_effects.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.backrooms.main.visual_effects.show_record"))
                                        .description(OptionDescription.of(Text.translatable("config.backrooms.main.visual_effects.show_record.description")))
                                        .binding(true, () -> this.showRecord, newVal -> this.showRecord = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.backrooms.main.sanity"))
                                .description(OptionDescription.of(Text.translatable("config.backrooms.main.sanity.description")))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.backrooms.main.sanity.loose_sanity_speed"))
                                        .description(OptionDescription.of(Text.translatable("config.backrooms.main.sanity.loose_sanity_speed.description")))
                                        .binding(5, () -> this.looseSanitySpeed, newVal -> this.looseSanitySpeed = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 120)
                                                .step(1)
                                                .formatValue(val -> Text.literal(val + " mins")))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.backrooms.main.sanity.recover_sanity_speed"))
                                        .description(OptionDescription.of(Text.translatable("config.backrooms.main.sanity.recover_sanity_speed.description")))
                                        .binding(1, () -> this.recoverSanitySpeed, newVal -> this.recoverSanitySpeed = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 60)
                                                .step(1)
                                                .formatValue(val -> Text.literal(val + " mins")))
                                        .build())
                                .build())
                        .build())
                .build().generateScreen(parent);
    }
}