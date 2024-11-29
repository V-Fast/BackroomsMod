package org.vfast.backrooms.block.interactable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Set;

import static org.vfast.backrooms.dimensions.BackroomsDimensions.LEVEL_ZERO_WORLD_KEY;

public class EntranceBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public EntranceBlock(Settings settings) {

        super(settings);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity.getServer() != null){
            ServerWorld serverWorld = entity.getServer().getWorlds().iterator().next();
            entity.teleport(serverWorld.getServer().getWorld(LEVEL_ZERO_WORLD_KEY),
                    entity.getX(), 15, entity.getZ(), Set.of(), entity.getYaw(), entity.getPitch());
        }
        super.onEntityCollision(state, world, pos, entity);
    }


}