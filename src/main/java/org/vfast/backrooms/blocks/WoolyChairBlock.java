package org.vfast.backrooms.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoolyChairBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<WoolyChairBlock> CODEC = simpleCodec(WoolyChairBlock::new);

    public WoolyChairBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<WoolyChairBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction currentDirection = state.getValue(FACING);
        return WoolyChairShapes.fromDirection(currentDirection).shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    enum WoolyChairShapes {
        NORTH(Shapes.or(
                Block.box(3, 4, 1, 13, 5, 11),
                Block.box(1, 0, 0, 3, 7, 1),
                Block.box(13, 0, 0, 15, 7, 1),
                Block.box(1, 0, 12, 3, 5, 13),
                Block.box(13, 0, 12, 15, 5, 13),
                Block.box(0, 0, 0, 4, 10, 16),
                Block.box(12, 0, 0, 16, 10, 16),
                Block.box(0, 0, 13, 16, 16, 16))),

        SOUTH(Shapes.or(
                Block.box(3, 4, 5, 13, 5, 15),
                Block.box(1, 0, 15, 3, 7, 16),
                Block.box(13, 0, 15, 15, 7, 16),
                Block.box(1, 0, 3, 3, 5, 4),
                Block.box(13, 0, 3, 15, 5, 4),
                Block.box(12, 0, 0, 16, 10, 16),
                Block.box(0, 0, 0, 4, 10, 16),
                Block.box(0, 0, 0, 16, 16, 3))),

        WEST(Shapes.or(
                Block.box(1, 4, 3, 11, 5, 13),
                Block.box(0, 0, 13, 1, 7, 15),
                Block.box(0, 0, 1, 1, 7, 3),
                Block.box(12, 0, 13, 13, 5, 15),
                Block.box(12, 0, 1, 13, 5, 3),
                Block.box(0, 0, 12, 16, 10, 16),
                Block.box(0, 0, 0, 16, 10, 4),
                Block.box(13, 0, 0, 16, 16, 16))),

        EAST(Shapes.or(
                Block.box(5, 4, 3, 15, 5, 13),
                Block.box(15, 0, 1, 16, 7, 3),
                Block.box(15, 0, 13, 16, 7, 15),
                Block.box(3, 0, 1, 4, 5, 3),
                Block.box(3, 0, 13, 4, 5, 15),
                Block.box(0, 0, 0, 16, 10, 4),
                Block.box(0, 0, 12, 16, 10, 16),
                Block.box(0, 0, 0, 3, 16, 16)));

        private final VoxelShape shape;

        WoolyChairShapes(VoxelShape shape) {
            this.shape = shape;
        }

        public static WoolyChairShapes fromDirection(Direction facing) {
            return switch (facing) {
                case SOUTH -> SOUTH;
                case WEST -> WEST;
                case EAST -> EAST;
                default -> NORTH;
            };
        }
    }
}
