package org.vfast.backrooms.mixins;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.world.BackroomsLevels;

@Mixin(Player.class)
public abstract class PlayerMixins extends Avatar implements ContainerUser {
    protected PlayerMixins(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "shouldShowName", at = @At(value = "RETURN"), cancellable = true)
    private void hideName(CallbackInfoReturnable<Boolean> cir) {
        ResourceKey<Level> level = this.level().dimension();
        cir.setReturnValue(level != BackroomsLevels.LEVEL_0);
    }
}
