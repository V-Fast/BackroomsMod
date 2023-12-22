package org.vfast.backrooms.world;

import com.google.common.base.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.registry.*;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.world.biome.BackroomsBiomes;
import org.vfast.backrooms.world.biome.LevelOneBiome;
import org.vfast.backrooms.world.biome.LevelZeroBiome;
import org.vfast.backrooms.world.chunk.LevelOneChunkGenerator;
import org.vfast.backrooms.world.chunk.LevelZeroChunkGenerator;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
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

    public static final Identifier LEVEL_ZERO_ID = new Identifier(BackroomsMod.ID, "level_0");
    public static final Identifier LEVEL_ONE_ID = new Identifier(BackroomsMod.ID, "level_1");

    public static final Supplier<DimensionType> levelDimType = () -> new DimensionType(OptionalLong.of(14000), false, true, false, true,
            1, true, false, 0, 16, 16, TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft", "infiniburn_overworld")), Identifier.of("minecraft", "the_nether"),
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
                            new LevelOneChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomsBiomes.LEVEL_ONE_BIOME)
                                            .get())
                            )));

    public static final RegistryKey<World> LEVEL_ZERO_KEY = RegistryKey.of(RegistryKeys.WORLD, LEVEL_ZERO_ID);
    public static final RegistryKey<World> LEVEL_ONE_KEY = RegistryKey.of(RegistryKeys.WORLD, LEVEL_ONE_ID);

    public static boolean isInBackrooms(Entity entity) {
        return entity.getWorld() == entity.getServer().getWorld(LEVEL_ZERO_KEY) || entity.getWorld() == entity.getServer().getWorld(LEVEL_ONE_KEY);
    }

    @Override
    public void registerHooks() {
        Registry.register(LimlibWorld.LIMLIB_WORLD, LEVEL_ZERO_ID, LEVEL_ZERO);
        Registry.register(LimlibWorld.LIMLIB_WORLD, LEVEL_ONE_ID, LEVEL_ONE);

        LimlibRegistryHooks.hook(RegistryKeys.BIOME, (infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            registry.add(BackroomsBiomes.LEVEL_ZERO_BIOME, LevelZeroBiome.create(features, carvers), Lifecycle.stable());
            registry.add(BackroomsBiomes.LEVEL_ONE_BIOME, LevelOneBiome.create(features, carvers), Lifecycle.stable());
        });
    }
}

