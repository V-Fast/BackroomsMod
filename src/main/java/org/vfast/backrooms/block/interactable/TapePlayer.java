package org.vfast.backrooms.block.interactable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.vfast.backrooms.block.entity.TapePlayerEntity;
import org.vfast.backrooms.item.MusicTape;

public class TapePlayer extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_RECORD = Properties.HAS_RECORD;

    public TapePlayer(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(HAS_RECORD, false));
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock()) && !world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            assert blockEntity instanceof TapePlayerEntity;
            ItemStack recordStack = ((TapePlayerEntity) blockEntity).getRecord();
            if (recordStack.getItem() instanceof MusicTape) {
                ((MusicTape) recordStack.getItem()).stop((ServerWorld) world);
            }
            throwItem(world, recordStack, pos);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (stack.getItem() instanceof MusicTape && !state.get(HAS_RECORD)) {
                state = (BlockState)state.with(HAS_RECORD, true);
                ((MusicTape) stack.getItem()).play((ServerWorld) world, pos);
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof TapePlayerEntity) {
                    ItemStack copyStack = stack.copy();
                    ((TapePlayerEntity) blockEntity).setRecord(copyStack);
                }
                stack.decrementUnlessCreative(1, player);
                world.setBlockState(pos, state);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
                player.increaseStat(Stats.PLAY_RECORD, 1);

                return ItemActionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(HAS_RECORD) && !world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TapePlayerEntity) {
                ItemStack recordStack = ((TapePlayerEntity) blockEntity).getRecord();
                ((TapePlayerEntity) blockEntity).clear();
                state = (BlockState)state.with(HAS_RECORD, false);

                if (recordStack.getItem() instanceof MusicTape) {
                    ((MusicTape) recordStack.getItem()).stop((ServerWorld) world);
                }

                world.setBlockState(pos, state);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
                if (player != null) {
                    player.swingHand(Hand.MAIN_HAND);
                    int freeSlot = player.getStackInHand(player.getActiveHand()).isEmpty() ? player.getInventory().selectedSlot : player.getInventory().getEmptySlot();
                    if (freeSlot >= 0) {
                        ((TapePlayerEntity) blockEntity).clear();
                        ItemStack itemStack2 = recordStack.copy();
                        player.getInventory().setStack(freeSlot, itemStack2);
                    } else {
                        throwItem(world, recordStack, pos);
                    }
                } else {
                    throwItem(world, recordStack, pos);
                }
                ((TapePlayerEntity) blockEntity).clear();
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 6, 14);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    private void throwItem(World world, ItemStack itemStack, BlockPos pos) {
        float f = 0.7F; // max force of the throw
        double d = (double)(world.random.nextFloat() * f) + 0.15000000596046448D;
        double e = (double)(world.random.nextFloat() * f) + 0.06000000238418579D + 0.5D;
        double g = (double)(world.random.nextFloat() * f) + 0.15000000596046448D;
        ItemStack itemStack2 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + g, itemStack2);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_RECORD, FACING);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TapePlayerEntity(pos, state);
    }
}
