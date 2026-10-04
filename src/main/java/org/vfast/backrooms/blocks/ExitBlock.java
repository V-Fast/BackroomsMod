package org.vfast.backrooms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.vfast.backrooms.utils.ShapeUtils;

public class ExitBlock extends FaceAttachedHorizontalDirectionalBlock {
    public ExitBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape defaultShape = Block.box(0, 0, 3.75, 16, 0.5, 12.25);
        
        ShapeUtils.AttachDirection from = new ShapeUtils.AttachDirection(AttachFace.FLOOR, Direction.NORTH);
        ShapeUtils.AttachDirection to = new ShapeUtils.AttachDirection(state.getValue(FACE), state.getValue(FACING));

        return ShapeUtils.rotate(defaultShape, from, to);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }
}
