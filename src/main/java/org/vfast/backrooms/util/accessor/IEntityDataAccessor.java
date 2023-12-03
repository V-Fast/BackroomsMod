package org.vfast.backrooms.util.accessor;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataAccessor {
    default NbtCompound backrooms$getPersistentData() {
        return null;
    }
}
