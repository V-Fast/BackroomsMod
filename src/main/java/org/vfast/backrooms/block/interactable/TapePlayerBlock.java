package org.vfast.backrooms.block.interactable;

import com.mojang.serialization.MapCodec;
import org.vfast.backrooms.block.entity.BackroomsBlockEntities;
import org.vfast.backrooms.block.entity.TapePlayerBlockEntity;
import org.vfast.backrooms.item.interactable.MusicTape;
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
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TapePlayerBlock extends BlockWithEntity {
    public static final MapCodec<TapePlayerBlock> CODEC = createCodec(TapePlayerBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_RECORD;

    public TapePlayerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HAS_RECORD, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 6, 14);

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
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
        return new TapePlayerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BackroomsBlockEntities.TAPE_PLAYER_BLOCK_ENTITY, TapePlayerBlockEntity::tick);
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
        if (blockEntity instanceof TapePlayerBlockEntity tapePlayerEntity) {
            tapePlayerEntity.setRecord(stack.copy());
            tapePlayerEntity.startPlaying();
            world.setBlockState(pos, (BlockState)state.with(HAS_RECORD, true), 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, state));
        }
    }

    private void removeRecord(World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TapePlayerBlockEntity tapePlayerEntity) {
                ItemStack itemStack = tapePlayerEntity.getRecord();
                if (!itemStack.isEmpty()) {
                    MusicTape record = (MusicTape) itemStack.getItem();
                    MinecraftServer server = world.getServer();
                    StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(record.getSound().getId(), null);
                    server.getPlayerManager().getPlayerList().forEach(player1 -> player1.networkHandler.sendPacket(stopSoundS2CPacket));
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

    private void throwItem(TapePlayerBlockEntity tapePlayerEntity, World world, ItemStack itemStack, BlockPos pos) {
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
        if (blockEntity instanceof TapePlayerBlockEntity) {
            Item item = ((TapePlayerBlockEntity)blockEntity).getRecord().getItem();
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