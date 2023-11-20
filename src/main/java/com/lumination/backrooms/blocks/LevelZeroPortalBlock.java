package com.lumination.backrooms.blocks;

import com.lumination.backrooms.sounds.BackroomsSounds;
import com.lumination.backrooms.world.dimensions.BackroomsDimensions;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.random.RandomGenerator;

public class LevelZeroPortalBlock extends Block {
    public LevelZeroPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World fromWorld, BlockPos pos, Entity entity) {
        if (!fromWorld.isClient() && entity.canUsePortals()) {
            RandomGenerator rand = RandomGenerator.getDefault();
            ServerWorld overworld = fromWorld.getServer().getOverworld();
            ServerWorld level_zero = fromWorld.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY);
            if (fromWorld != level_zero) {
                LimlibTravelling.travelTo(entity, level_zero, new TeleportTarget(
                                new Vec3d(rand.nextDouble(entity.getX()-200, entity.getX()+200.0d), 2.0d, rand.nextDouble(entity.getZ()-200, entity.getZ()+200)),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        /* TODO add sound effect */ null, 5.0f, 1.0f);
            } else {
                LimlibTravelling.travelTo(entity, overworld, new TeleportTarget(
                                overworld.getSpawnPos().toCenterPos(),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        /* TODO add sound effect */ null, 5.0f, 1.0f);
            }
        }
    }
}
