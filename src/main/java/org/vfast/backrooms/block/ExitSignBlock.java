package org.vfast.backrooms.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ExitSignBlock extends HorizontalFacingBlock {
    public static final MapCodec<ExitSignBlock> CODEC = createCodec(ExitSignBlock::new);

    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(7, 7, 0.5, 10, 16, 15.5);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(6, 7, 0.5, 9, 16, 15.5);
    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(0.5, 7, 7, 15.5, 16, 10);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(0.5, 7, 6, 15.5, 16, 9);
    protected ExitSignBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getShape(BlockState state) {
        switch (state.get(FACING)) {
            case WEST -> {
                return SHAPE_WEST;
            }
            case EAST -> {
                return SHAPE_EAST;
            }
            case NORTH -> {
                return SHAPE_NORTH;
            }
            case SOUTH -> {
                return SHAPE_SOUTH;
            }
            default -> {
                return null; // This shouldn't happen
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }
}
