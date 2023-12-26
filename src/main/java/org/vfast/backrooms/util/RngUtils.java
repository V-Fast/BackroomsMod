package org.vfast.backrooms.util;

import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class RngUtils {
    public static Random getFromPos(ChunkRegion region, Chunk chunk, BlockPos pos) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;
        if (x == 0) {
            x = 23;
        }
        if (z == 0) {
            z = -5;
        }
        return new Xoroshiro128PlusPlusRandom(region.getSeed() + ((long) x * z) + LimlibHelper.blockSeed(pos));
    }

    public static Random getFromPos(ChunkRegion region, Chunk chunk) {
        return getFromPos(region, chunk, chunk.getPos().getStartPos());
    }

    /**
     * Gets random chunk in a 1-chunk radius around the specified position.
     * @param pos The position of the chunk.
     * @param random The random generator.
     * @return The position of a random chunk adjacent to the specified one.
     */
    public static BlockPos getRandomChunkAround(BlockPos pos, Random random) {
        List<Integer> possiblePos = new ArrayList<>(List.of(16, 0, -16));
        Vec3i diff = Vec3i.ZERO;
        while (diff.getX() == 0 && diff.getZ() == 0) {
            diff = new Vec3i(possiblePos.get(random.nextBetween(0, 2)), 0, possiblePos.get(random.nextBetween(0, 2)));
        }
        return pos.add(diff);
    }

    public static List<BlockPos> generatePuddle(BlockPos pos, Random random) {
        List<BlockPos> blocks = new ArrayList<>();
        List<Integer> possiblePos = new ArrayList<>(List.of(1, 0, -1));

        blocks.add(pos);
        while (random.nextBetween(1, 3) != 3) {
            List<BlockPos> blocksToAdd = new ArrayList<>();
            for (BlockPos block : blocks) {
                BlockPos newBlock = block.add(possiblePos.get(random.nextBetween(0, 2)), possiblePos.get(random.nextBetween(0, 2)), possiblePos.get(random.nextBetween(0, 2)));
                if (!blocks.contains(newBlock) && !blocksToAdd.contains(newBlock))
                    blocksToAdd.add(newBlock);
            }
            blocks.addAll(blocksToAdd);
        }
        return blocks;
    }
}
