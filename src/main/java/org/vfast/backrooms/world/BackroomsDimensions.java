package org.vfast.backrooms.world;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.registry.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.world.biome.BackroomsBiomes;
import org.vfast.backrooms.world.biome.LevelOneBiome;
import org.vfast.backrooms.world.biome.LevelRunBiome;
import org.vfast.backrooms.world.biome.LevelZeroBiome;
import org.vfast.backrooms.world.chunk.LevelOneChunkGenerator;
import org.vfast.backrooms.world.chunk.LevelRunChunkGenerator;
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

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class BackroomsDimensions implements LimlibRegistrar {

    public static final List<Level> levels = new ArrayList<>();
    public static final Supplier<DimensionType> levelDimType = () -> new DimensionType(OptionalLong.of(14000), false, true, false, true,
            1, true, false, 0, 16, 16, TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft", "infiniburn_overworld")), Identifier.of("minecraft", "the_nether"),
            0.0f, new DimensionType.MonsterSettings(false, false, ConstantIntProvider.ZERO, 0));

    public static final Level LEVEL_ZERO = new Level(new Identifier(BackroomsMod.ID, "level_0"),
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey
                                            .of(RegistryKeys.DIMENSION_TYPE, new Identifier(BackroomsMod.ID, "level_0")))
                                    .get(),
                            new LevelZeroChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomsBiomes.LEVEL_ZERO_BIOME)
                                            .get())
                            )));

    public static final Level LEVEL_ONE = new Level(new Identifier(BackroomsMod.ID, "level_1"),
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey
                                            .of(RegistryKeys.DIMENSION_TYPE, new Identifier(BackroomsMod.ID, "level_1")))
                                    .get(),
                            new LevelOneChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomsBiomes.LEVEL_ONE_BIOME)
                                            .get())
                            )));

    public static final Level LEVEL_RUN = new Level(new Identifier(BackroomsMod.ID, "level_run"),
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey
                                            .of(RegistryKeys.DIMENSION_TYPE, new Identifier(BackroomsMod.ID, "level_run")))
                                    .get(),
                            new LevelRunChunkGenerator(
                                    new FixedBiomeSource(registry
                                            .get(RegistryKeys.BIOME)
                                            .getOptional(BackroomsBiomes.LEVEL_RUN_BIOME)
                                            .get())
                            )));

    public static boolean isInBackrooms(Entity entity) {
        return isLevel((ServerWorld) entity.getWorld());
    }

    public static boolean isLevel(ServerWorld world) {
        MinecraftServer server = world.getServer();

        for (Level level : levels) {
            if (level.getWorld(server) == world) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Level getLevel(Identifier id) {
        for (Level level : levels) {
            if (level.id.equals(id)) {
                return level;
            }
        }
        return null;
    }

    @Override
    public void registerHooks() {
        levels.forEach((level) -> {Registry.register(LimlibWorld.LIMLIB_WORLD, level.id, level);});

        LimlibRegistryHooks.hook(RegistryKeys.BIOME, (infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            registry.add(BackroomsBiomes.LEVEL_ZERO_BIOME, LevelZeroBiome.create(features, carvers), Lifecycle.stable());
            registry.add(BackroomsBiomes.LEVEL_ONE_BIOME, LevelOneBiome.create(features, carvers), Lifecycle.stable());
            registry.add(BackroomsBiomes.LEVEL_RUN_BIOME, LevelRunBiome.create(features, carvers), Lifecycle.stable());
        });
    }

    public static class Level extends LimlibWorld {
        public final Identifier id;

        public final RegistryKey<World> key;

        public Level(Identifier id, Function<RegistryProvider, DimensionOptions> dimensionOptionsSupplier) {
            super(levelDimType, dimensionOptionsSupplier);
            this.id = id;
            key = RegistryKey.of(RegistryKeys.WORLD, id);
            levels.add(this);
        }

        public ServerWorld getWorld(MinecraftServer server) {
            return server.getWorld(key);
        }
    }
}

