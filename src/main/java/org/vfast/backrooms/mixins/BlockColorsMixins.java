package org.vfast.backrooms.mixins;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.blocks.BackroomsBlocks;

import java.util.List;

@Mixin(BlockColors.class)
public class BlockColorsMixins {
    @Inject(method = "createDefault", at = @At(value = "RETURN"), cancellable = true)
    private static void addBlockColors(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors colors = cir.getReturnValue();
        colors.register(List.of(BlockTintSources.grassBlock()), BackroomsBlocks.FAKE_BLOCK);

        cir.setReturnValue(colors);
    }
}
