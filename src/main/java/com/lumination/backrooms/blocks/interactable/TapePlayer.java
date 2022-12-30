package com.lumination.backrooms.blocks.interactable;

import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.blocks.entity.TapePlayerEntity;
import com.lumination.backrooms.items.interactables.MusicTape;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TapePlayer extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_RECORD;

    public TapePlayer(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(HAS_RECORD, false));
    }

    private static VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 6, 14);

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
        builder.add(new Property[]{HAS_RECORD});
        builder.add(FACING);
    }

    static {
        HAS_RECORD = Properties.HAS_RECORD;
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

    /* JUKEBOX CODE */

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        if (nbtCompound != null && nbtCompound.contains("RecordItem")) {
            world.setBlockState(pos, (BlockState)state.with(HAS_RECORD, true), 2);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if ((Boolean)state.get(HAS_RECORD)) {
            this.removeRecord(world, pos, player.getInventory().getEmptySlot() != -1 ? player : null);

            state = (BlockState)state.with(HAS_RECORD, false);
            world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
            world.setBlockState(pos, state, 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    public void setRecord(@Nullable Entity user, WorldAccess world, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TapePlayerEntity) {
            TapePlayerEntity tapePlayerEntity = (TapePlayerEntity)blockEntity;
            tapePlayerEntity.setRecord(stack.copy());
            tapePlayerEntity.startPlaying();
            world.setBlockState(pos, (BlockState)state.with(HAS_RECORD, true), 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, state));
        }
    }

    private void removeRecord(World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TapePlayerEntity) {
                TapePlayerEntity tapePlayerEntity = (TapePlayerEntity)blockEntity;
                ItemStack itemStack = tapePlayerEntity.getRecord();
                if (!itemStack.isEmpty()) {
                    world.syncWorldEvent(1010, pos, 0);
                    if (player != null) {
                        int freeSlot = player.getStackInHand(player.getActiveHand()).isEmpty() ? player.getInventory().selectedSlot : player.getInventory().getEmptySlot();
                        tapePlayerEntity.clear();
                        ItemStack itemStack2 = itemStack.copy();
                        player.getInventory().setStack(freeSlot, itemStack2);
                    } else {
                        throwItem(tapePlayerEntity, world, itemStack, pos);
                    }
                }
            }
        }
    }

    private void throwItem(TapePlayerEntity tapePlayerEntity, World world, ItemStack itemStack, BlockPos pos) {
        tapePlayerEntity.clear();
        float f = 0.7F;
        double d = (double)(world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        double e = (double)(world.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.5D;
        double g = (double)(world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        ItemStack itemStack2 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + g, itemStack2);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            this.removeRecord(world, pos, null);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TapePlayerEntity) {
            Item item = ((TapePlayerEntity)blockEntity).getRecord().getItem();
            if (item instanceof MusicTape) {
                return ((MusicTape)item).getComparatorOutput();
            }
        }
        return 0;
    }

    private boolean isEmpty(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}