package org.vfast.backrooms.interfaces;

import net.minecraft.core.BlockPos;
import org.vfast.backrooms.blocks.entity.TextSignBlockEntity;

public interface GuiOpener {
    void openTextSignEdit(TextSignBlockEntity blockEntity, BlockPos pos, boolean isFront);
}
