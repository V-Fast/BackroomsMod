package org.vfast.backrooms.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VentBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final MapCodec<VentBlock> CODEC = simpleCodec(VentBlock::new);

    private static final VoxelShape[] FLOOR_SHAPES = new VoxelShape[] {
        Block.box(1, 0, 4.5, 15, 0.25, 11.5), // s
        Block.box(4.5, 0, 1, 11.5, 0.25, 15), // w
        Block.box(1, 0, 4.5, 15, 0.25, 11.5), // n
        Block.box(4.5, 0, 1, 11.5, 0.25, 15)  // e
    };

    private static final VoxelShape[] WALL_SHAPES = new VoxelShape[] {
        Block.box(1, 4.5, 0, 15, 11.5, 0.25),
        Block.box(15.75, 4.5, 1, 16, 11.5, 15),
        Block.box(1, 4.5, 15.75, 15, 11.5, 16),
        Block.box(0, 4.5, 1, 0.25, 11.5, 15)
    };

    private static final VoxelShape[] CEILING_SHAPES = new VoxelShape[] {
        Block.box(1, 15.75, 4.5, 15, 16, 11.5),
        Block.box(4.5, 15.75, 1, 11.5, 16, 15),
        Block.box(1, 15.75, 4.5, 15, 16, 11.5),
        Block.box(4.5, 15.75, 1, 11.5, 16, 15)
    };

    public VentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
    }

    @Override
    protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACE) == AttachFace.FLOOR) {
            return FLOOR_SHAPES[horizontalIndex(state.getValue(FACING))];
        } else if (state.getValue(FACE) == AttachFace.CEILING) {
            return CEILING_SHAPES[horizontalIndex(state.getValue(FACING))];
        }
        return WALL_SHAPES[horizontalIndex(state.getValue(FACING))];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    private static int horizontalIndex(Direction direction) {
        return switch (direction) {
            case SOUTH -> 0;
            case WEST -> 1;
            case NORTH -> 2;
            default -> 3;
        };
    }
}
