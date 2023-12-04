package org.vfast.backrooms.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsConfig {

    public static ConfigClassHandler<BackroomsConfig> HANDLER = ConfigClassHandler.createBuilder(BackroomsConfig.class)
            .id(new Identifier(BackroomsMod.ID, "config_handler"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(QuiltLoader.getConfigDir().resolve(BackroomsMod.ID+".json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // not needed, pretty print by default
                    .setJson5(true)
                    .build())
            .build();

    @ClientOnly
    @SerialEntry(comment = "Whether to show the \"Record\" gui when using the Camera.")
    public boolean showRecord;

    private static BackroomsConfig instance;

    public static BackroomsConfig getInstance() {
        if (instance == null) {
            instance = new BackroomsConfig();
        }
        return instance;
    }

    public Screen getScreen(@Nullable Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.backrooms.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.backrooms.main"))
                        .tooltip(Text.translatable("config.backrooms.main.tooltip"))
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
                        .build())
                .build().generateScreen(parent);
    }
}