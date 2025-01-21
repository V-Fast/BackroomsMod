package org.vfast.backrooms.block.interactable;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.dimensions.BackroomsDimensions;

import java.util.Set;

import static net.minecraft.block.LeavesBlock.WATERLOGGED;
import static org.vfast.backrooms.dimensions.BackroomsDimensions.*;

public class ExitBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public ExitBlock(Settings settings) {

        super(settings);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if(entity.getServer() != null){

            if(entity.getWorld().getRegistryKey() == LEVEL_ZERO_WORLD_KEY){
                ServerWorld serverWorld = entity.getServer().getWorlds().iterator().next();
                entity.teleport(serverWorld.getServer().getWorld(LEVEL_ONE_WORLD_KEY),
                        entity.getX(), 21, entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
                if(entity instanceof ServerPlayerEntity player) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false));
                }

            } else if(entity.getWorld().getRegistryKey() == LEVEL_ONE_WORLD_KEY){
                ServerWorld serverWorld = entity.getServer().getWorlds().iterator().next();
                entity.teleport(serverWorld.getServer().getWorld(LEVEL_TWO_WORLD_KEY),
                        entity.getX(), 21, entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
                if(entity instanceof ServerPlayerEntity player) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false));
                }
    }
    }
        super.onEntityCollision(state, world, pos, entity);
    }
}