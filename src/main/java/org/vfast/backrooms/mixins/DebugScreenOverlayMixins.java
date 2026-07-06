package org.vfast.backrooms.mixins;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixins {
    @Unique
    private static final List<Identifier> FORBIDDEN_ENTRIES;

    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/debug/DebugScreenEntries;getEntry(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/gui/components/debug/DebugScreenEntry;"))
    private DebugScreenEntry removePosition(Identifier id) {
        if (DebugScreenOverlayMixins.FORBIDDEN_ENTRIES.contains(id)) {
            return null;
        }
        return DebugScreenEntries.getEntry(id);
    }

    static {
        FORBIDDEN_ENTRIES = List.of(
                DebugScreenEntries.PLAYER_POSITION,
                DebugScreenEntries.PLAYER_SECTION_POSITION,
                DebugScreenEntries.LOOKING_AT_ENTITY,
                DebugScreenEntries.LOOKING_AT_BLOCK_STATE,
                DebugScreenEntries.LOOKING_AT_BLOCK_TAGS,
                DebugScreenEntries.LOOKING_AT_ENTITY_TAGS,
                DebugScreenEntries.LOOKING_AT_FLUID_STATE,
                DebugScreenEntries.LOOKING_AT_FLUID_TAGS
        );
    }
}
