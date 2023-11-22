package com.lumination.backrooms.world;

import com.google.common.base.Supplier;
import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.world.biome.BackroomsBiomes;
import com.lumination.backrooms.world.biome.LevelOneBiome;
import com.lumination.backrooms.world.biome.LevelZeroBiome;
import com.lumination.backrooms.world.chunk.LevelZeroChunkGenerator;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.OptionalLong;

public class BackroomsDimensions implements LimlibRegistrar {

    public static final Identifier LEVEL_ZERO_ID = new Identifier(BackroomsMod.MOD_ID, "level_0");
    public static final Identifier LEVEL_ONE_ID = new Identifier(BackroomsMod.MOD_ID, "level_1");

    public static final Supplier<DimensionType> levelDimType = () -> new DimensionType(OptionalLong.of(14000), false, true, false, true,
            1, true, false, 0, 96, 16, TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft", "infiniburn_overworld")), Identifier.of("minecraft", "the_nether"),
            0.0f, new DimensionType.MonsterSettings(false, false, ConstantIntProvider.ZERO, 0));

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
                                            .getOptional(BackroomsBiomes.LEVEL_ZERO_BIOME)
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
                                            .getOptional(BackroomsBiomes.LEVEL_ONE_BIOME)
                                            .get())
                            )));

    public static final RegistryKey<World> LEVEL_ZERO_KEY = RegistryKey.of(RegistryKeys.WORLD, LEVEL_ZERO_ID);
    public static final RegistryKey<World> LEVEL_ONE_KEY = RegistryKey.of(RegistryKeys.WORLD, LEVEL_ONE_ID);

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

            registry.add(BackroomsBiomes.LEVEL_ZERO_BIOME, LevelZeroBiome.create(features, carvers), Lifecycle.stable());
            registry.add(BackroomsBiomes.LEVEL_ONE_BIOME, LevelOneBiome.create(features, carvers), Lifecycle.stable());
        });
    }
}

