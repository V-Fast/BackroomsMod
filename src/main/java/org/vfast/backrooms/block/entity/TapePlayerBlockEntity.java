package org.vfast.backrooms.block.entity;

import org.vfast.backrooms.block.interactable.TapePlayerBlock;
import org.vfast.backrooms.item.interactable.MusicTape;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TapePlayerBlockEntity extends BlockEntity implements Clearable {
    private ItemStack record;
    private int ticksThisSecond;
    private long tickCount;
    private long recordStartTick;
    private boolean isPlaying;

    public TapePlayerBlockEntity(BlockPos pos, BlockState state) {
        super(BackroomsBlockEntities.TAPE_PLAYER_BLOCK_ENTITY, pos, state);
        this.record = ItemStack.EMPTY;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("RecordItem", 10)) {
            this.setRecord(ItemStack.fromNbt(nbt.getCompound("RecordItem")));
        }

        this.isPlaying = nbt.getBoolean("IsPlaying");
        this.recordStartTick = nbt.getLong("RecordStartTick");
        this.tickCount = nbt.getLong("TickCount");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.getRecord().isEmpty()) {
            nbt.put("RecordItem", this.getRecord().writeNbt(new NbtCompound()));
        }

        nbt.putBoolean("IsPlaying", this.isPlaying);
        nbt.putLong("RecordStartTick", this.recordStartTick);
        nbt.putLong("TickCount", this.tickCount);
    }

    public ItemStack getRecord() {
        return this.record;
    }

    public void setRecord(ItemStack stack) {
        this.record = stack;
        this.markDirty();
    }

    public void startPlaying() {
        this.recordStartTick = this.tickCount;
        this.isPlaying = true;
    }

    public void clear() {
        this.setRecord(ItemStack.EMPTY);
        this.isPlaying = false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, TapePlayerBlockEntity blockEntity) {
        blockEntity.ticksThisSecond++;
        if (isPlayingRecord(state, blockEntity)) {
            Item var5 = blockEntity.getRecord().getItem();
            if (var5 instanceof MusicTape musicDiscItem) {
                if (isSongFinished(blockEntity, musicDiscItem)) {
                    world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                    blockEntity.isPlaying = false;
                } else if (hasSecondPassed(blockEntity)) {
                    blockEntity.ticksThisSecond = 0;
                    world.emitGameEvent(GameEvent.JUKEBOX_PLAY, pos, GameEvent.Emitter.of(state));
                }
            }
        }

        blockEntity.tickCount++;
    }

    private static boolean isPlayingRecord(BlockState state, TapePlayerBlockEntity blockEntity) {
        return state.get(TapePlayerBlock.HAS_RECORD) && blockEntity.isPlaying;
    }

    private static boolean isSongFinished(TapePlayerBlockEntity blockEntity, MusicTape musicDisc) {
        return blockEntity.tickCount >= blockEntity.recordStartTick + (long)musicDisc.getSongLengthInTicks();
    }

    private static boolean hasSecondPassed(TapePlayerBlockEntity blockEntity) {
        return blockEntity.ticksThisSecond >= 20;
    }
}
