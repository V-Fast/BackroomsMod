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
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH -> {
                return Block.createCuboidShape(2, 0, 6.5d, 14, 8, 10.5d);
            }
            case SOUTH -> {
                return Block.createCuboidShape(2, 0, 5.5d, 14, 8, 9.5d);
            }
            case EAST -> {
                return Block.createCuboidShape(5.5d, 0, 2, 9.5d, 8, 14);
            }
            case WEST -> {
                return Block.createCuboidShape(6.5d, 0, 2, 10.5d, 8, 14);
            }
        }
        return Block.createCuboidShape(2, 0, 5.5d, 14, 8, 9.5d);
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

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        if (nbtCompound != null && nbtCompound.contains("Record")) {
            world.setBlockState(pos, (BlockState)state.with(RECORD, 0), 2);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.isSneaking()) {
                state = (BlockState)state.with(RECORD, 0);
                world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                world.setBlockState(pos, state, 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
                world.getPlayers().forEach(player1 -> player1.sendMessage(Text.literal(""), true));
                
                this.stopRecords(state, world, pos, player);
            } else {
                this.switchRecord(state, world, pos, player, hand, hit);
            }
        }

        return ActionResult.success(!world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        //tooltip.add(Text.empty());
        //tooltip.add(Text.literal("This block does not work properly").formatted(Formatting.RED));
    }

    public void switchRecord(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        List<RadioRecord> allRecords = BackroomsMod.getRecords();
        RadioRecord record = null;
        Identifier oldRecord = null;
        if (blockEntity instanceof RadioEntity) {
            RadioEntity radioEntity = (RadioEntity) blockEntity;
            int currentId = radioEntity.getRecordId();
            if (currentId != 0) oldRecord = allRecords.get(currentId).sound.getId();
            currentId = scroll(currentId + 1, 1, allRecords.size() - 1);
            radioEntity.setRecord(currentId);
            radioEntity.startPlaying();
            world.setBlockState(pos, (BlockState)state.with(RECORD, currentId), 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));

            record = allRecords.get(currentId);

            if (currentId != 0) {
                MinecraftServer server = world.getServer();
                StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(oldRecord, null);
                server.getPlayerManager().getPlayerList().forEach(player1 -> player1.networkHandler.sendPacket(stopSoundS2CPacket));
                world.playSound(null, pos, record.sound, SoundCategory.RECORDS, 0.75f, 1f);
            }
        }

        if (!world.isClient) {
            RadioRecord finalRecord = record;
            world.getServer().getPlayerManager().broadcast(Text.translatable("record.nowPlaying", Text.translatable(finalRecord.name).getString()).formatted(Formatting.YELLOW), true);

            if (player != null) {
                player.incrementStat(Stats.PLAY_RECORD);
            }
        }
    }

    public void stopRecords(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RadioEntity) {
                world.setBlockState(pos, (BlockState)state.with(RECORD, 0), 2);
                Identifier soundId = BackroomsMod.getRecords().get(((RadioEntity) blockEntity).getRecordId()).sound.getId();
                StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(soundId, null);
                ((RadioEntity) blockEntity).setRecord(0);

                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                serverPlayerEntity.networkHandler.sendPacket(stopSoundS2CPacket);
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