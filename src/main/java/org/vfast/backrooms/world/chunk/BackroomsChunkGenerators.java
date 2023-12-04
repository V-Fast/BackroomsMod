package org.vfast.backrooms.world.chunk;

import org.vfast.backrooms.BackroomsMod;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class BackroomsChunkGenerators {

    public static void registerChunkGenerators() {
        registerChunkGenerator("level_zero_chunk_generator", LevelZeroChunkGenerator.CODEC);
        registerChunkGenerator("level_one_chunk_generator", LevelOneChunkGenerator.CODEC);
    }

    public static <C extends ChunkGenerator, D extends Codec<C>> D registerChunkGenerator(String id, D chunkGeneratorCodec) {
        return Registry.register(Registries.CHUNK_GENERATOR, new Identifier(BackroomsMod.ID, id), chunkGeneratorCodec);
    }
}
