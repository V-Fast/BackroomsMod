package org.vfast.backrooms.interfaces;

import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface Suffocator {
    void setSuffocating(@Nullable BlockState state);
}
