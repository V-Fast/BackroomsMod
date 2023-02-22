package com.lumination.backrooms.blocks.interactable;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.entity.ModBlockEntities;
import com.lumination.backrooms.blocks.entity.RadioEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Radio extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty RECORD;

    public Radio(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(RECORD, 0));
    }

    //TODO: Fix shape
    private static VoxelShape SHAPE = Block.createCuboidShape(2, 0, 4, 12, 8, 6.5d);

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
        builder.add(RECORD);
        builder.add(FACING);
    }

    static {
        RECORD = IntProperty.of("record", 0, BackroomsMod.getRecords().size());
    }


    /* BLOCK ENTITY */

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RadioEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.RADIO, RadioEntity::tick);
    }

    /* JUKEBOX CODE */

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        if (nbtCompound != null && nbtCompound.contains("Record")) {
            world.setBlockState(pos, (BlockState)state.with(RECORD, 0), 2);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isSneaking()) {
            this.stopRecords(world, pos, player);

            state = (BlockState)state.with(RECORD, 0);
            world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
            world.setBlockState(pos, state, 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
            world.getPlayers().forEach(player1 -> player1.sendMessage(Text.literal(""), true));
            return ActionResult.success(world.isClient);
        } else {
            this.switchRecord(state, world, pos, player, hand, hit);
            return ActionResult.success(world.isClient);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.empty());
        tooltip.add(Text.literal("This block does not work properly").formatted(Formatting.RED));
    }

    public void switchRecord(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            RadioRecord record = null;
            if (blockEntity instanceof RadioEntity) {
                RadioEntity radioEntity = (RadioEntity) blockEntity;
                radioEntity.setRecord(scroll(radioEntity.getRecordId() + 1, 1, BackroomsMod.getRecords().size() - 1));
                radioEntity.startPlaying();
                world.setBlockState(pos, (BlockState)state.with(RECORD, radioEntity.getRecordId()), 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));

                record = BackroomsMod.getRecords().get(radioEntity.getRecordId());
            }

            //world.syncWorldEvent(null, WorldEvents.MUSIC_DISC_PLAYED, pos, 1);
            RadioRecord finalRecord = record;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), record.sound, SoundCategory.RECORDS, 1f, 1f, false);
            world.getPlayers().forEach(player1 -> player1.sendMessage(Text.translatable("record.nowPlaying", Text.translatable(finalRecord.name).getString()).formatted(Formatting.YELLOW), true));

            if (player != null) {
                player.incrementStat(Stats.PLAY_RECORD);
            }
        }
    }

    public void stopRecords(World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RadioEntity) {
                ((RadioEntity) blockEntity).setRecord(0);
                world.syncWorldEvent(1010, pos, 0);
            }
        }
    }

    private static int scroll(int value, int min, int max) {
        if (value > max || value < min) {
            if (value > max) {
                value = min;
            } else {
                value = max;
            }
        }
        return value;
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            // stop records
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public static class RadioRecord {
        public String name;
        public SoundEvent sound;

        public RadioRecord(String name, SoundEvent record) {
            this.name = name;
            this.sound = record;
        }
    }
}