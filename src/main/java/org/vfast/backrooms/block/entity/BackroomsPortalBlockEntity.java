package org.vfast.backrooms.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.vfast.backrooms.world.BackroomsDimensions;

public class BackroomsPortalBlockEntity extends BlockEntity {

    private Identifier level;

    public BackroomsPortalBlockEntity(BlockPos pos, BlockState state) {
        super(BackroomsBlockEntities.BACKROOMS_PORTAL_BLOCK_ENTITY, pos, state);
        level = BackroomsDimensions.LEVEL_ZERO.id;
    }

    public Identifier getLevel() {
        return level;
    }

    public void setLevel(Identifier level) {
        this.level = level;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        // Save the current value of the number to the nbt
        nbt.putString("level", level.toString());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        level = new Identifier(nbt.getString("level"));
    }
}
