package com.lumination.backrooms.world.chunk;

import com.lumination.backrooms.utils.SeedGenerator;
import com.lumination.backrooms.world.BackroomsDimensions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class LevelOneChunkGenerator extends AbstractNbtChunkGenerator {

    public static final Codec<LevelOneChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeSource;
        }), NbtGroup.CODEC.fieldOf("nbt_group").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.nbtGroup;
        })).apply(instance, instance.stable(LevelOneChunkGenerator::new));
    });

    public LevelOneChunkGenerator(BiomeSource source) {
        this(source, getNbtGroup());
    }

    public LevelOneChunkGenerator(BiomeSource source, NbtGroup nbtGroup) {
        super(source, nbtGroup);
    }

    public static NbtGroup getNbtGroup() {
        return NbtGroup.Builder.create(BackroomsDimensions.LEVEL_ONE_ID).build();
    }

    @Override
    public int getChunkDistance() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor,
                                                  ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager,
                                                  ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk,
            ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        Random random = SeedGenerator.getFromPos(chunkRegion, chunk);
        BlockPos start = chunk.getPos().getStartPos();
        return CompletableFuture.completedFuture(chunk);
    }

    // Assumes a piece hasn't generated there yet.
    public void generateRandomPiece(ChunkRegion region, BlockPos pos, Random random) {
    }

    @Override
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        super.modifyStructure(region, pos, state, blockEntityNbt);
    }

    /*@Override
    protected Identifier getContainerLootTable(LootableContainerBlockEntity container) {
        return Random.create(LimlibHelper.blockSeed(container.getPos())).nextBoolean() ? new Identifier("backrooms", "chests/level_0") : LootTables.SPAWN_BONUS_CHEST;
    }*/

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 96;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }

}

