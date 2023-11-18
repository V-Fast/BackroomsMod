package com.lumination.backrooms.world.chunk;

import com.lumination.backrooms.blocks.BackroomsBlocks;
import com.lumination.backrooms.entities.BackroomsEntities;
import com.lumination.backrooms.entities.mod.BacteriaEntity;
import com.lumination.backrooms.world.dimensions.BackroomsDimensions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class LevelZeroChunkGenerator extends AbstractNbtChunkGenerator {

    public static final Codec<LevelZeroChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeSource;
        })).apply(instance, instance.stable(LevelZeroChunkGenerator::new));
    });

    public LevelZeroChunkGenerator(BiomeSource source) {
        super(source, BackroomsDimensions.LEVEL_ZERO_ID);
    }

    @Override
    public void storeStructures(ServerWorld world) {
        store("level_0", world, 0, 3);
    }

    // TODO Look into this
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
        generateRandomPiece(chunkRegion, start.add(0, 1, 0), random);
        generateRandomPiece(chunkRegion, start.add(8, 1, 0), random);
        generateRandomPiece(chunkRegion, start.add(0, 1, 8), random);
        generateRandomPiece(chunkRegion, start.add(8, 1, 8), random);
        decorateChunk(world, chunk, random);
        return CompletableFuture.completedFuture(chunk);
    }

    // Assumes a piece hasn't generated there yet.
    public void generateRandomPiece(ChunkRegion region, BlockPos pos, Random random) {
        BlockRotation rotation = BlockRotation.random(random);
        int num = random.nextBetween(0, 9);
        int type = 0;
        if (num == 4 || num == 5) {
            type = 2;
        }
        if (num == 7 || num == 8) {
            type = 3;
        }
        if (num == 9) {
            type = 1;
        }
        generateNbt(region, pos, "level_0_"+type, rotation);
    }

    public void decorateChunk(ServerWorld world, Chunk chunk, Random random) {
        BlockPos start = chunk.getPos().getStartPos();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 7; k++) {
                    BlockPos pos = start.add(i, k, j);
                    BlockState block = chunk.getBlockState(pos);
                    if (pos.getY() == 0 || pos.getY() == 6) {
                        chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
                    }
                    if (block.isOf(BackroomsBlocks.MOIST_SILK)) {
                        if (random.nextBetween(0, 19) == 19) {
                            chunk.setBlockState(pos, BackroomsBlocks.MOIST_SILK_PLANKS.getDefaultState(), false);
                        }
                    }
                    if (block.isAir() && pos.getY() == 2) {
                        if (random.nextBetween(0, 5000) == 5000) {
                            block = Blocks.CHEST.getDefaultState().rotate(BlockRotation.random(random));
                            chunk.setBlockState(pos, block, false);
                            ChestBlockEntity chest = new ChestBlockEntity(pos, block);
                            chest.setCustomName(Text.literal("Level 0 Chest"));
                            chest.setLootTable(new Identifier("backrooms", "chests/level_0"), random.nextLong());
                            chunk.setBlockEntity(chest);

                            BacteriaEntity bacteria = new BacteriaEntity(BackroomsEntities.BACTERIA, world);
                            bacteria.setPosition(pos.getX(), pos.getY(), pos.getZ());
                            world.spawnEntity(bacteria);

                            // For debug purposes
                            //BackroomsMod.LOGGER.debug("Chest spawned at "+pos.getX()+" "+pos.getY()+" "+pos.getZ());
                        }
                    }
                }
            }
        }
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
