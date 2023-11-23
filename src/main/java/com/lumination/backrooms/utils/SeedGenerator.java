package com.lumination.backrooms.utils;

import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

public class SeedGenerator {
    public static Random getFromPos(ChunkRegion region, Chunk chunk, BlockPos pos) {
        if (chunk.getPos().x != 0 && chunk.getPos().z != 0) { // Avoids the weird repeating patterns
            return Random.create(region.getSeed() + ((long) (chunk.getPos().x) * (chunk.getPos().z)) + LimlibHelper.blockSeed(pos));
        } else {
            return Random.create(region.getSeed() + ((long) (chunk.getPos().x + 2) * (chunk.getPos().z - 3)) + LimlibHelper.blockSeed(pos));
        }
    }

    public static Random getFromPos(ChunkRegion region, Chunk chunk) {
        if (chunk.getPos().x != 0 && chunk.getPos().z != 0) { // Avoids the weird repeating patterns
            return Random.create(region.getSeed() + ((long) (chunk.getPos().x) * (chunk.getPos().z)));
        } else {
            return Random.create(region.getSeed() + ((long) (chunk.getPos().x + 2) * (chunk.getPos().z - 3)));
        }
    }
}
