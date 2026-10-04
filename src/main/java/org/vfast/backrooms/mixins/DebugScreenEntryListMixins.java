package org.vfast.backrooms.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.gui.components.debug.DebugScreenEntryList;
import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vfast.backrooms.world.BackroomsLevels;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(DebugScreenEntryList.class)
public class DebugScreenEntryListMixins {
    @Shadow
    @Final
    private List<Identifier> currentlyEnabled;
    @Shadow
    private boolean isOverlayVisible;
    @Unique
    private static final List<Identifier> FORBIDDEN_ENTRIES;

    @Redirect(method = "getCurrentlyEnabled", at = @At(value = "INVOKE", target = "Ljava/util/List;copyOf(Ljava/util/Collection;)Ljava/util/List;"))
    private List<Identifier> removePosition(Collection<? extends Identifier> coll) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && !BackroomsLevels.isBackrooms(level.dimension())) {
            return List.copyOf(this.currentlyEnabled.stream().filter(FORBIDDEN_ENTRIES::contains).toList());
        }
        return List.copyOf(this.currentlyEnabled);
    }

    @Redirect(method = "rebuildCurrentList", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private void removeRebuiltPosition(Map<Identifier, DebugScreenEntryStatus> instance, BiConsumer<Identifier, DebugScreenEntryStatus> action) {
        boolean isReducedDebugInfo = Minecraft.getInstance().showOnlyReducedInfo();
        ClientLevel level = Minecraft.getInstance().level;
        instance.forEach((key, value) -> {
            boolean showInOverlay = value == DebugScreenEntryStatus.ALWAYS_ON || this.isOverlayVisible && value == DebugScreenEntryStatus.IN_OVERLAY;
            boolean isNotBkrm = level != null && !(BackroomsLevels.isBackrooms(level.dimension()) && FORBIDDEN_ENTRIES.contains(key));
            if (showInOverlay && isNotBkrm) {
                DebugScreenEntry debug = DebugScreenEntries.getEntry(key);
                if (debug != null && debug.isAllowed(isReducedDebugInfo)) {
                    this.currentlyEnabled.add(key);
                }
            }
        });
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
