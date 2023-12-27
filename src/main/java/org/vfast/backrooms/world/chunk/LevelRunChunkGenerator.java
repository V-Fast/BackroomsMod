package org.vfast.backrooms.world.chunk;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.vfast.backrooms.util.RngUtils;
import org.vfast.backrooms.world.BackroomsDimensions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class LevelRunChunkGenerator extends AbstractNbtChunkGenerator {

    public static final Codec<LevelRunChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.biomeSource;
        }), NbtGroup.CODEC.fieldOf("nbt_group").stable().forGetter((chunkGenerator) -> {
            return chunkGenerator.nbtGroup;
        })).apply(instance, instance.stable(LevelRunChunkGenerator::new));
    });

    public LevelRunChunkGenerator(BiomeSource source) {
        this(source, getNbtGroup());
    }

    public LevelRunChunkGenerator(BiomeSource source, NbtGroup nbtGroup) {
        super(source, nbtGroup);
    }

    public static NbtGroup getNbtGroup() {
        return NbtGroup.Builder.create(BackroomsDimensions.LEVEL_RUN.id)
                .with("main",
                        "start",
                        "big_hallway",
                        "small_hallway",
                        "closet",
                        "exit")
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
        ChunkPos pos = chunk.getPos();
        final int maxChunkLength = 50;
        if (MathHelper.isMultipleOf(pos.x, 10) && pos.z >= 0 && pos.z <= maxChunkLength) {
            if (pos.z == 0) {
                generateNbt(chunkRegion, start.add(4, 0, 13), nbtGroup.nbtId("main", "start"), Manipulation.of(BlockRotation.CLOCKWISE_180));
            } else if (pos.z == maxChunkLength) {
                generateNbt(chunkRegion, start.add(4, 0, 0), nbtGroup.nbtId("main", "exit"), Manipulation.of(BlockRotation.CLOCKWISE_180));
            } else {
                BlockMirror mirror = random.nextBoolean() ? BlockMirror.FRONT_BACK : BlockMirror.NONE;
                generateNbt(chunkRegion, start.add(4, 0, 0), nbtGroup.nbtId("main", "big_hallway"), Manipulation.of(BlockRotation.CLOCKWISE_180, mirror));
                if (mirror == BlockMirror.NONE) {
                    if (random.nextBetween(1, 3) == 3) {
                        generateNbt(chunkRegion, start.add(11, 0, 1), nbtGroup.nbtId("main", "small_hallway"), Manipulation.of(BlockRotation.CLOCKWISE_180));
                    } else {
                        generateNbt(chunkRegion, start.add(11, 0, 1), nbtGroup.nbtId("main", "closet"));
                        generateNbt(chunkRegion, start.add(11, 0, 10), nbtGroup.nbtId("main", "closet"));
                    }
                    generateNbt(chunkRegion, start.add(0, 0, 10), nbtGroup.nbtId("main", "closet"), Manipulation.of(BlockRotation.CLOCKWISE_180));
                    generateNbt(chunkRegion, start.add(0, 0, 2), nbtGroup.nbtId("main", "closet"), Manipulation.of(BlockRotation.CLOCKWISE_180));
                } else {
                    if (random.nextBetween(1, 3) == 3) {
                        generateNbt(chunkRegion, start.add(0, 0, 1), nbtGroup.nbtId("main", "small_hallway"));
                    } else {
                        generateNbt(chunkRegion, start.add(0, 0, 10), nbtGroup.nbtId("main", "closet"), Manipulation.of(BlockRotation.CLOCKWISE_180));
                        generateNbt(chunkRegion, start.add(0, 0, 1), nbtGroup.nbtId("main", "closet"), Manipulation.of(BlockRotation.CLOCKWISE_180));
                    }
                    generateNbt(chunkRegion, start.add(11, 0, 2), nbtGroup.nbtId("main", "closet"));
                    generateNbt(chunkRegion, start.add(11, 0, 10), nbtGroup.nbtId("main", "closet"));
                }
            }
        }
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        super.modifyStructure(region, pos, state, blockEntityNbt);
    }

    /*@Override
    protected Identifier getContainerLootTable(LootableContainerBlockEntity container) {
        return Random.create(LimlibHelper.blockSeed(container.getPos())).nextBetween(1, 5) != 5 ? new Identifier("backrooms", "chests/level_0") : LootTables.SPAWN_BONUS_CHEST;
    }*/

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 16;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
    }
}