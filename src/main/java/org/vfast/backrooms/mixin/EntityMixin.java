package org.vfast.backrooms.mixin;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.sounds.BackroomsSounds;
import org.vfast.backrooms.world.BackroomsDimensions;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
    @Shadow
    @Final
    protected Random random;
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Entity This = ((Entity)(Object)this);
        if (!(This.getWorld() instanceof ServerWorld world)) return;
        if (world == world.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY)) return;
        if ((This.isInsideWall() && random.nextBetween(1, 50) == 50) || (This.isPlayer() && !(((ServerPlayerEntity)This).isCreative() || This.isSpectator()) && random.nextBetween(1, 36000) == 36000)) {
            LimlibTravelling.travelTo(This, world.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY), new TeleportTarget(
                            Vec3d.of(new Vec3i(random.nextBetween(This.getBlockX()-200, This.getBlockX()+200), 2, random.nextBetween(This.getBlockZ()-200, This.getBlockZ()+200))),
                            Vec3d.ZERO, 0.0f, 0.0f),
                    BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
        }
    }
}
