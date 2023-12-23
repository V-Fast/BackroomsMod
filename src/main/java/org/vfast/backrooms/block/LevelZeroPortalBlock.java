package org.vfast.backrooms.block;

import org.vfast.backrooms.sound.BackroomsSounds;
import org.vfast.backrooms.world.BackroomsDimensions;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class LevelZeroPortalBlock extends Block {
    public LevelZeroPortalBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World fromWorld, BlockPos pos, Entity entity) {
        if (!fromWorld.isClient()) {
            Random rand = fromWorld.getRandom();
            ServerWorld overworld = fromWorld.getServer().getOverworld();
            ServerWorld level_zero = BackroomsDimensions.LEVEL_ZERO.getWorld(fromWorld.getServer());
            if (fromWorld != level_zero) {
                LimlibTravelling.travelTo(entity, level_zero, new TeleportTarget(
                                Vec3d.of(new Vec3i(rand.nextBetween(entity.getBlockX()-200, entity.getBlockX()+200), 2, rand.nextBetween(entity.getBlockZ()-200, entity.getBlockZ()+200))),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
            } else {
                LimlibTravelling.travelTo(entity, overworld, new TeleportTarget(
                                overworld.getSpawnPos().toCenterPos(),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
            }
            if (rand.nextBetween(0, 2) == 2) {
                Vec3d explodePos = Vec3d.of(pos);
                fromWorld.removeBlock(pos, false);
                fromWorld.createExplosion(null, explodePos.x, explodePos.y, explodePos.z, 10.0f, World.ExplosionSourceType.BLOCK);
            }
        }
    }
}
