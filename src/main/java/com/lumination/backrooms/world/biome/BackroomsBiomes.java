package com.lumination.backrooms.world.biome;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.world.chunk.LevelZeroChunkGenerator;
import com.lumination.backrooms.world.dimensions.BackroomsDimensions;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class BackroomsBiomes {

    public static final RegistryKey<Biome> LEVEL_ZERO_BIOME = get(BackroomsDimensions.LEVEL_ZERO_ID);
    public static final RegistryKey<Biome> LEVEL_ONE_BIOME = get(BackroomsDimensions.LEVEL_ONE_ID);

    public static void init() {
        get("level_zero_chunk_generator", LevelZeroChunkGenerator.CODEC);
        // get("level_one_chunk_generator", LevelOneChunkGenerator.CODEC);
    }

    public static RegistryKey<Biome> get(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }

    public static <C extends ChunkGenerator, D extends Codec<C>> D get(String id, D chunkGeneratorCodec) {
        return Registry.register(Registries.CHUNK_GENERATOR, new Identifier(BackroomsMod.MOD_ID, id), chunkGeneratorCodec);
    }
}
