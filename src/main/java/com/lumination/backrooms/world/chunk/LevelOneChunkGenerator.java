package com.lumination.backrooms.world.chunk;

import com.lumination.backrooms.blocks.BackroomsBlocks;
import com.lumination.backrooms.utils.RngUtils;
import com.lumination.backrooms.world.BackroomsDimensions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
        return NbtGroup.Builder.create(BackroomsDimensions.LEVEL_ONE_ID)
                .with("8x8",
                        "pillar",
                        "empty",
                        "wall",
                        "corner")
                .build();
    }

    @Override
    public int getPlacementRadius() {
        return 1;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor,
                                                  ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager,
                                                  ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk,
            ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        Random random = RngUtils.getFromPos(chunkRegion, chunk);
        BlockPos start = chunk.getPos().getStartPos();
        generateRandomPiece(chunkRegion, start, random);
        return CompletableFuture.completedFuture(chunk);
    }

    // Assumes a piece hasn't generated there yet.
    public void generateRandomPiece(ChunkRegion region, BlockPos pos, Random random) {
        int num = random.nextBetween(1, 100);
        Manipulation manipulation = Manipulation.random(random);
        String name = "pillar";
        if (num > 50 && num <= 75) {
            name = "wall";
            generateNbt(region, RngUtils.getRandomChunkAround(pos, random), nbtGroup.nbtId("8x8", "wall"));
            generateNbt(region, RngUtils.getRandomChunkAround(pos, random), nbtGroup.nbtId("8x8", "corner"));
        }
        if (num > 75) {
            name = "empty";
        }
        generateNbt(region, pos, nbtGroup.nbtId("8x8", name));
    }

    @Override
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        super.modifyStructure(region, pos, state, blockEntityNbt);

        Random random = RngUtils.getFromPos(region, region.getChunk(pos), pos);

        if (random.nextBetween(1, 50) == 50 && state.isOf(BackroomsBlocks.SCRATCHED_CONCRETE)) { // Generate wet "puddles"
            List<BlockPos> puddle = RngUtils.generatePuddle(pos, random);
            for (BlockPos puddleBlock : puddle) {
                BlockState puddleBlockState = region.getBlockState(puddleBlock);
                if (!puddleBlockState.isAir() && !puddleBlockState.isOf(Blocks.BEDROCK)) {
                    region.setBlockState(puddleBlock, BackroomsBlocks.STAINED_CONCRETE.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
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

