package org.vfast.backrooms.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.vfast.backrooms.blocks.entity.TextSignBlockEntity;
import org.vfast.backrooms.blocks.interfaces.CeilingSupportSign;
import org.vfast.backrooms.client.gui.TextSignEditScreen;

public class TextSignBlock extends BaseEntityBlock implements CeilingSupportSign {
    public static final MapCodec<TextSignBlock> CODEC = simpleCodec(TextSignBlock::new);

    public static final EnumProperty<Direction> ROTATION = BlockStateProperties.HORIZONTAL_FACING;

    protected TextSignBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<TextSignBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable TextSignBlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new TextSignBlockEntity(worldPosition, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        TextSignBlock.TextDirection direction = this.getFacingText(hitResult, state);
        if (level.isClientSide() && direction != TextDirection.NONE && level.getBlockEntity(pos) instanceof TextSignBlockEntity blockEntity) {
            TextSignEditScreen editScreen = new TextSignEditScreen(blockEntity, pos, this.getFacingText(hitResult, state) == TextDirection.FRONT);
            Minecraft.getInstance().setScreen(editScreen);
            player.swing(InteractionHand.MAIN_HAND);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
    }

    private TextSignBlock.TextDirection getFacingText(BlockHitResult hitResult, BlockState state) {
        Direction currentDirection = hitResult.getDirection();
        if (currentDirection == state.getValue(ROTATION)) {
            return TextDirection.FRONT;
        } else if (currentDirection == state.getValue(ROTATION).getOpposite()) {
            return TextDirection.BACK;
        } else {
            return TextDirection.NONE;
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.canSurvive(level, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return this.updateShape(state, level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(ROTATION);
        return switch (direction) {
            case EAST, WEST -> Block.box(7.75, 0, 0, 8.25, 16, 16);
            default -> Block.box(0, 0, 7.75, 16, 16, 8.25);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(ROTATION, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT, FRONT_BACK -> state.setValue(ROTATION, state.getValue(ROTATION).getOpposite());
            default -> super.mirror(state, mirror);
        };
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> state.setValue(ROTATION, state.getValue(ROTATION).getClockWise());
            case COUNTERCLOCKWISE_90 -> state.setValue(ROTATION, state.getValue(ROTATION).getCounterClockWise());
            case CLOCKWISE_180 -> state.setValue(ROTATION, state.getValue(ROTATION).getOpposite());
            default -> super.rotate(state, rotation);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    private enum TextDirection {
        FRONT,
        BACK,
        NONE
    }
}
