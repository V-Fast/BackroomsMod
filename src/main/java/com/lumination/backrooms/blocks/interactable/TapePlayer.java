package com.lumination.backrooms.blocks.interactable;

import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.blocks.entity.TapePlayerEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TapePlayer extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty HAS_RECORD = Properties.HAS_RECORD;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private static VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 12, 6, 12);

    public TapePlayer(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState) this.stateManager.getDefaultState().with(HAS_RECORD, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
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
        builder.add(FACING);
    }


    /* BLOCK ENTITY */

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TapePlayerEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.TAPE_PLAYER, TapePlayerEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HAS_RECORD).booleanValue()) {
            this.removeRecord(world, pos);
            state = (BlockState) state.with(HAS_RECORD, false);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    public void setRecord(@Nullable Entity user, WorldAccess world, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof JukeboxBlockEntity)) {
            return;
        }
        ((JukeboxBlockEntity)blockEntity).setRecord(stack.copy());
        world.setBlockState(pos, (BlockState)state.with(HAS_RECORD, true), Block.NOTIFY_LISTENERS);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, state));
    }

    private void removeRecord(World world, BlockPos pos) {
        if (world.isClient) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof JukeboxBlockEntity)) {
            return;
        }
        JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity)blockEntity;
        ItemStack itemStack = jukeboxBlockEntity.getRecord();
        if (itemStack.isEmpty()) {
            return;
        }
        world.syncWorldEvent(WorldEvents.MUSIC_DISC_PLAYED, pos, 0);
        jukeboxBlockEntity.clear();
        float f = 0.7f;
        double d = (double)(world.random.nextFloat() * 0.7f) + (double)0.15f;
        double e = (double)(world.random.nextFloat() * 0.7f) + 0.06000000238418579 + 0.6;
        double g = (double)(world.random.nextFloat() * 0.7f) + (double)0.15f;
        ItemStack itemStack2 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + g, itemStack2);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        this.removeRecord(world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        Item item;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity && (item = ((JukeboxBlockEntity)blockEntity).getRecord().getItem()) instanceof MusicDiscItem) {
            return ((MusicDiscItem)item).getComparatorOutput();
        }
        return 0;
    }
}
