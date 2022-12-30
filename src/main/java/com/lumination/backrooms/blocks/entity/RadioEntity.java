package com.lumination.backrooms.blocks.entity;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.blocks.interactable.TapePlayer;
import com.lumination.backrooms.items.interactables.MusicTape;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class RadioEntity extends BlockEntity implements Clearable {
    private int recordId;
    private int ticksThisSecond;
    private long tickCount;
    private long recordStartTick;
    private boolean isPlaying;

    public RadioEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RADIO, pos, state);
        this.recordId = 0;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Record", 10)) {
            this.recordId = nbt.getInt("Record");
        }

        this.isPlaying = nbt.getBoolean("IsPlaying");
        this.recordStartTick = nbt.getLong("RecordStartTick");
        this.tickCount = nbt.getLong("TickCount");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.recordId == -1) {
            nbt.putInt("Record", this.recordId);

        }

        nbt.putBoolean("IsPlaying", this.isPlaying);
        nbt.putLong("RecordStartTick", this.recordStartTick);
        nbt.putLong("TickCount", this.tickCount);
    }

    public void startPlaying() {
        this.recordStartTick = this.tickCount;
        this.isPlaying = true;
    }

    public void clear() {
        this.recordId = -1;
        this.isPlaying = false;
    }

    public void setRecord(int recordId) {
        this.recordId = recordId;
    }

    public static void tick(World world, BlockPos pos, BlockState state, RadioEntity blockEntity) {
        ++blockEntity.ticksThisSecond;
        if (isPlayingRecord(state, blockEntity)) {
            if (isSongFinished(blockEntity)) {
                world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                blockEntity.isPlaying = false;
            } else if (hasSecondPassed(blockEntity)) {
                blockEntity.ticksThisSecond = 0;
                world.emitGameEvent(GameEvent.JUKEBOX_PLAY, pos, GameEvent.Emitter.of(state));
            }
        }

        ++blockEntity.tickCount;
    }

    private static boolean isPlayingRecord(BlockState state, RadioEntity blockEntity) {
        return state.get(Radio.RECORD) != 0 && blockEntity.isPlaying;
    }

    private static boolean isSongFinished(RadioEntity blockEntity) {
        return blockEntity.tickCount >= blockEntity.recordStartTick;
    }

    private static boolean hasSecondPassed(RadioEntity blockEntity) {
        return blockEntity.ticksThisSecond >= 20;
    }

    public int getRecordId() {
        return recordId;
    }
}
