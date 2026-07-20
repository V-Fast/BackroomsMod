package org.vfast.backrooms.mixins;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixins {
    @Shadow
    protected abstract void openWorldLoadBundledResourcePack(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel);

    @Inject(method = "openWorldCheckWorldStemCompatibility", at = @At(value = "HEAD"))
    private void removeExperimental(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel, CallbackInfo ci) {
        this.openWorldLoadBundledResourcePack(worldAccess, worldStem, packRepository, onCancel);
    }
}
