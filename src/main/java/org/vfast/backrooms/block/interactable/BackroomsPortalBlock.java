package org.vfast.backrooms.block.interactable;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.block.entity.BackroomsPortalBlockEntity;
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

public class BackroomsPortalBlock extends BlockWithEntity {
    public static final MapCodec<BackroomsPortalBlock> CODEC = createCodec(BackroomsPortalBlock::new);
    public BackroomsPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BackroomsPortalBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World fromWorld, BlockPos pos, Entity entity) {
        if (!fromWorld.isClient()) {
            Random rand = fromWorld.getRandom();
            MinecraftServer server = fromWorld.getServer();
            ServerWorld overworld = server.getOverworld();
            ServerWorld level = BackroomsDimensions.getLevel(((BackroomsPortalBlockEntity)fromWorld.getBlockEntity(pos)).getLevel()).getWorld(server);
            if (fromWorld != level) {
                if (level == BackroomsDimensions.LEVEL_RUN.getWorld(server)) {
                    BackroomsDimensions.moveToLevelRun(entity);
                } else {
                    LimlibTravelling.travelTo(entity, level, new TeleportTarget(
                                    Vec3d.of(new Vec3i(rand.nextBetween(entity.getBlockX() - 200, entity.getBlockX() + 200), 2, rand.nextBetween(entity.getBlockZ() - 200, entity.getBlockZ() + 200))),
                                    Vec3d.ZERO, 0.0f, 0.0f),
                            BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
                }
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
