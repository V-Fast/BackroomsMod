package org.vfast.backrooms.block.entity;

import org.vfast.backrooms.block.interactable.RadioBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class RadioBlockEntity extends BlockEntity implements Clearable {
    private int recordId;
    private int ticksThisSecond;
    private long tickCount;
    private long recordStartTick;
    private boolean isPlaying;

    public RadioBlockEntity(BlockPos pos, BlockState state) {
        super(BackroomsBlockEntities.RADIO_BLOCK_ENTITY, pos, state);
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
        this.recordId = 0;
        this.isPlaying = false;
    }

    public void setRecord(int recordId) {
        this.recordId = recordId;
    }

    public static void tick(World world, BlockPos pos, BlockState state, RadioBlockEntity blockEntity) {
        blockEntity.ticksThisSecond++;
        if (isPlayingRecord(state, blockEntity)) {
            if (isSongFinished(blockEntity)) {
                world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                blockEntity.isPlaying = false;
            } else if (hasSecondPassed(blockEntity)) {
                blockEntity.ticksThisSecond = 0;
                world.emitGameEvent(GameEvent.JUKEBOX_PLAY, pos, GameEvent.Emitter.of(state));
            }
        }

        blockEntity.tickCount++;
    }

    private static boolean isPlayingRecord(BlockState state, RadioBlockEntity blockEntity) {
        return state.get(RadioBlock.RECORD) != 0 && blockEntity.isPlaying;
    }

    private static boolean isSongFinished(RadioBlockEntity blockEntity) {
        return blockEntity.tickCount >= blockEntity.recordStartTick;
    }

    private static boolean hasSecondPassed(RadioBlockEntity blockEntity) {
        return blockEntity.ticksThisSecond >= 20;
    }

    public int getRecordId() {
        return recordId;
    }
}
