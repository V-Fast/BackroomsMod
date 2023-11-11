package com.lumination.backrooms.world.chunk;

import com.google.common.collect.Lists;
import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.world.biome.BackroomBiomes;
import com.lumination.backrooms.world.dimensions.BackroomDimensions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.maze.DepthFirstMazeSolver;
import net.ludocrypt.limlib.api.world.maze.DilateMaze;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.RectangularMazeGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class LevelZeroChunkGenerator extends AbstractNbtChunkGenerator {

    public static final Codec<LevelZeroChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeSource;
        })).apply(instance, instance.stable(LevelZeroChunkGenerator::new));
    });

    public LevelZeroChunkGenerator(BiomeSource source) {
        super(source, BackroomDimensions.LEVEL_ZERO_ID);
    }

    @Override
    public void storeStructures(ServerWorld world) {
        store("level_0", world, 0, 3);
    }

    @Override
    public int getChunkDistance() {
        return 2;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor,
                                                  ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager,
                                                  ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk,
            ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        Random random;
        if (chunk.getPos().x != 0 && chunk.getPos().z != 0) { // Avoids the weird repeating patterns
           random = Random.create(chunkRegion.getSeed() + ((long) (chunk.getPos().x) * (chunk.getPos().z)));
        } else {
            random = Random.create(chunkRegion.getSeed() + ((long) (chunk.getPos().x+2) * (chunk.getPos().z-3)));
        }
        BlockPos start = chunk.getPos().getStartPos();
        generateRandomPiece(chunkRegion, start, random);
        generateRandomPiece(chunkRegion, start.add(8, 0, 0), random);
        generateRandomPiece(chunkRegion, start.add(0, 0, 8), random);
        generateRandomPiece(chunkRegion, start.add(8, 0, 8), random);
        return CompletableFuture.completedFuture(chunk);
    }

    /*public void generateNextPieces(ChunkRegion region, BlockPos pos, List<BlockPos> alreadyGenerated, int type, BlockRotation rotation) {
        switch (type) {
            case 0: // crossroad

                break;

            case 1: // end
                break;

            case 2: // hall
                break;

            case 3: // turn
                break;
        }
    } */

    // Assumes a piece hasn't generated there yet.
    public void generateRandomPiece(ChunkRegion region, BlockPos pos, Random random) {
        BlockRotation rotation = BlockRotation.random(random);
        int num = random.nextBetween(0, 9);
        int type = 0;
        if (num == 3 || num == 4 || num == 5) {
            type = 2;
        }
        if (num == 6 || num == 7 || num == 8) {
            type = 3;
        }
        if (num == 9) {
            type = 1;
        }
        generateNbt(region, pos, "level_0_"+type, rotation);
    }

    @Override
    protected Identifier getBarrelLootTable() {
        return LootTables.SPAWN_BONUS_CHEST;
    }

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
