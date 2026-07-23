package org.vfast.backrooms.blocks.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.vfast.backrooms.blocks.TvBlock;

public class TvBlockEntity extends BlockEntity {
    private int lastPoweredTick;

    public TvBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BackroomsBlockEntities.CRT_TV_ENTITY, worldPosition, blockState);
        this.lastPoweredTick = 0;
    }

    public int getLastPoweredTick() {
        return this.lastPoweredTick;
    }

    public int addPoweredTick() {
        this.lastPoweredTick = Math.min(this.lastPoweredTick + 1, TvBlock.SOUND_DURATION);
        this.markUpdated();
        return this.lastPoweredTick;
    }

    public int resetPoweredTick() {
        this.lastPoweredTick = 0;
        this.markUpdated();
        return 0;
    }

    public boolean isComplete() {
        return this.lastPoweredTick >= TvBlock.SOUND_DURATION - 1 || this.lastPoweredTick <= 0;
    }

    private void markUpdated() {
        this.setChanged();
        assert this.level != null;
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("song_tick", Codec.INT, this.lastPoweredTick);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.lastPoweredTick = input.read("song_tick", Codec.INT).orElse(-1);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
