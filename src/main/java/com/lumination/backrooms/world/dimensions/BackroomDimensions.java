package com.lumination.backrooms.world.dimensions;

import com.google.common.base.Supplier;
import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.items.ModItems;
import com.lumination.backrooms.utils.Color;
import com.lumination.backrooms.world.biome.BackroomBiomes;
import com.lumination.backrooms.world.biome.LevelOneBiome;
import com.lumination.backrooms.world.biome.LevelZeroBiome;
import com.lumination.backrooms.world.chunk.LevelZeroChunkGenerator;
import com.mojang.serialization.Lifecycle;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.OptionalLong;

public class BackroomDimensions implements LimlibRegistrar {

    public static final Identifier LEVEL_ZERO_ID = new Identifier(BackroomsMod.MOD_ID, "level_0");
    public static final Identifier LEVEL_ONE_ID = new Identifier(BackroomsMod.MOD_ID, "level_1");

    public static final Supplier<DimensionType> levelDimType = () -> new DimensionType(OptionalLong.of(6000), false, true, false, true,
            1, true, false, 0, 96, 16, TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft", "infiniburn_overworld")), Identifier.of("minecraft", "overworld"),
            0.1f, new DimensionType.MonsterSettings(false, false, ConstantIntProvider.ZERO, 0));

    public static final LimlibWorld LEVEL_ZERO = new LimlibWorld(levelDimType,
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey
                                            .of(RegistryKeys.DIMENSION_TYPE, LEVEL_ZERO_ID))
                                    .get(),
                            new LevelZeroChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomBiomes.LEVEL_ZERO_BIOME)
                                            .get())
                            )));

    public static final LimlibWorld LEVEL_ONE = new LimlibWorld(levelDimType,
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey
                                            .of(RegistryKeys.DIMENSION_TYPE, LEVEL_ONE_ID))
                                    .get(),
                            new LevelZeroChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomBiomes.LEVEL_ONE_BIOME)
                                            .get())
                            )));

    public static void registerDims() {
        BackroomDimensions.level(LEVEL_ZERO_ID, Blocks.IRON_BLOCK, ModItems.SILK, 195, 180, 10);
        BackroomDimensions.level(LEVEL_ONE_ID, Blocks.GRAY_CONCRETE, ModItems.WRENCH, Color.RGB.zero());
        BackroomsMod.print("Registered custom dimensions and portals");
    }

    private static PortalLink level(Identifier dimId, Block frame, Item lighter, Color.RGB portalColor) {
        BackroomsMod.print("Registered custom portal of " + dimId.getNamespace());
        return CustomPortalBuilder
                .beginPortal()
                .destDimID(dimId)
                .frameBlock(frame)
                .lightWithItem(lighter)
                .tintColor(portalColor.r(), portalColor.g(), portalColor.b())
                .onlyLightInOverworld()
                .setPortalSearchYRange(0, 3)
                .registerPortal();
    }

    public static PortalLink level(Identifier dimId, Block frame, Item lighter, int r, int g, int b) {
        return BackroomDimensions.level(dimId, frame, lighter, new Color.RGB(r, g, b));
    }

    @Override
    public void registerHooks() {
        LimlibWorld.LIMLIB_WORLD.add(
                RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, LEVEL_ZERO_ID),
                LEVEL_ZERO,
                Lifecycle.stable()
        );
        LimlibWorld.LIMLIB_WORLD.add(
                RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, LEVEL_ONE_ID),
                LEVEL_ONE,
                Lifecycle.stable()
        );
        LimlibRegistryHooks.hook(RegistryKeys.BIOME, (infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            registry.add(BackroomBiomes.LEVEL_ZERO_BIOME, LevelZeroBiome.create(features, carvers), Lifecycle.stable());
            registry.add(BackroomBiomes.LEVEL_ONE_BIOME, LevelOneBiome.create(features, carvers), Lifecycle.stable());
        });
    }
}

