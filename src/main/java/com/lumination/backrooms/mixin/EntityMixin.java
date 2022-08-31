package com.lumination.backrooms.mixin;

import com.lumination.backrooms.BackroomsMod;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // TODO: Finish Clipping functionality

    @Shadow public abstract BlockPos getBlockPos();
    @Shadow public abstract World getWorld();
    @Shadow public abstract boolean isPlayer();
    @Shadow public abstract double getY();
    @Shadow public abstract double getX();
    @Shadow public abstract double getZ();
    @Shadow public abstract ChunkPos getChunkPos();

    public boolean isFullySuffocating() {
        BlockPos feet = this.getBlockPos();
        BlockPos head = feet.up(1);
        return this.getWorld().getBlockState(feet).getBlock() != Blocks.AIR && this.getWorld().getBlockState(head).getBlock() != Blocks.AIR;
    }

    public void clipOut(RegistryKey<World> dimWorld) {
        ServerWorld destination = ((ServerWorld) this.getWorld()).getServer().getWorld(dimWorld);

        // destination.getWorldChunk(getChunkPos()).getWorld() - particularly this function
    }
}
