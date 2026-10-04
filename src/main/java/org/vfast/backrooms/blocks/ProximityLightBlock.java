package org.vfast.backrooms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.blocks.entity.BackroomsBlockEntities;
import org.vfast.backrooms.blocks.entity.ProximityLightBlockEntity;

public class ProximityLightBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final double PROXIMITY_RANGE = 12.0;

    public ProximityLightBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, BackroomsBlockEntities.PROXI_LIGHT_ENTITY, ProximityLightBlockEntity::tick);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @org.jspecify.annotations.Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            boolean isLit = state.getValue(LIT);
            if (isLit != level.hasNeighborSignal(pos)) {
                if (!isLit) {
                    level.setBlock(pos, state.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof ProximityLightBlockEntity plbe && !level.isClientSide()) {
            return state.getValueOrElse(LIT, false) ? 15 : 0;
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ProximityLightBlockEntity(worldPosition, blockState);
    }

    private double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
