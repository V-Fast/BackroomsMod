package org.vfast.backrooms.block.entity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;

public class TapePlayerEntity extends BlockEntity implements Clearable {
    private ItemStack record = ItemStack.EMPTY;

    public TapePlayerEntity(BlockPos pos, BlockState state) {
        super(BackroomsBlockEntities.TAPE_PLAYER_BLOCK_ENTITY, pos, state);
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        if (nbt.contains("RecordItem", 10)) {
            ItemStack stack = ItemStack.fromNbt(wrapper, nbt.getCompound("RecordItem")).orElse(ItemStack.EMPTY);
            this.setRecord(stack);
        }
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.writeNbt(nbt, wrapper);
        if (!this.getRecord().isEmpty()) {
            nbt.put("RecordItem", this.getRecord().encode(wrapper));
        }
    }

    public void setRecord(ItemStack stack) {
        this.record = stack;
        this.markDirty();
    }

    public ItemStack getRecord() {
        return this.record;
    }

    @Override
    public void clear() {
        this.record = ItemStack.EMPTY;
        this.markRemoved();
    }
}
