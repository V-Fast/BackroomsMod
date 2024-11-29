package org.vfast.backrooms.dimensions;

import com.mojang.serialization.Lifecycle;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.util.JSONUtil;


import java.io.IOException;

public class BackroomsDimensions {
    public static final RegistryKey<DimensionType> LEVEL_ZERO_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,  Identifier.of(BackroomsMod.ID, "dimension_type/level_0"));
    public static final RegistryKey<World> LEVEL_ZERO_WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("backrooms","level_0"));
    public static final RegistryKey<World> LEVEL_ONE_WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("backrooms","level_1"));

    private static DimensionType levelZeroType;

    public static void registerDimensions() {
        try {
            levelZeroType = JSONUtil.deserializeDataJson(DimensionType.CODEC, Identifier.of(BackroomsMod.ID, "dimension_type/level_0_type"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dimensions JSONs", e);
        }
    }

    public static void initPortal() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.IRON_BLOCK)
                .destDimID(Identifier.of(BackroomsMod.ID, "level_0"))
                .lightWithItem(Items.PAPER)
                .tintColor(181, 181, 97)
                .setPortalSearchYRange(0, 5)
                .registerPortal();
    }

    public static void registerDimensionTypes(MutableRegistry<DimensionType> registry) {
        RegistryEntryInfo stable = new RegistryEntryInfo(null, Lifecycle.stable()); // no experimental screen

        registry.add(LEVEL_ZERO_TYPE, levelZeroType, stable);
    }
}
