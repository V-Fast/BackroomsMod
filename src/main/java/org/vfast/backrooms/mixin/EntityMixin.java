package org.vfast.backrooms.mixin;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.sounds.BackroomsSounds;
import org.vfast.backrooms.world.BackroomsDimensions;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Random rand = ((Entity)(Object)this).getWorld().getRandom();
        if ((((Entity)(Object)this).isInsideWall() && rand.nextBetween(1, 50) == 50) || (rand.nextBetween(1, 36000) == 36000 && ((Entity)(Object)this).isPlayer() && ((Entity)(Object)this).getWorld() != ((Entity)(Object)this).getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY) && !(((ServerPlayerEntity)((Entity)(Object)this)).isCreative() || ((ServerPlayerEntity)((Entity)(Object)this)).isSpectator()))) {
            LimlibTravelling.travelTo(((Entity)(Object)this), ((Entity)(Object)this).getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY), new TeleportTarget(
                            Vec3d.of(new Vec3i(rand.nextBetween(((Entity)(Object)this).getBlockX()-200, ((Entity)(Object)this).getBlockX()+200), 2, rand.nextBetween(((Entity)(Object)this).getBlockZ()-200, ((Entity)(Object)this).getBlockZ()+200))),
                            Vec3d.ZERO, 0.0f, 0.0f),
                    BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
        }
    }
}
