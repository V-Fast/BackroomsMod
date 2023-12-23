package org.vfast.backrooms.mixin;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.sound.BackroomsSounds;
import org.vfast.backrooms.util.accessor.IEntityDataAccessor;
import org.vfast.backrooms.world.BackroomsDimensions;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput, IEntityDataAccessor {
    @Unique
    private NbtCompound persistentData;
    @Unique
    private static final String NBT_ID = BackroomsMod.ID+".data";

    @Override
    public NbtCompound backrooms$getPersistentData() {
        if (persistentData == null) {
            persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    protected void writeCustomData(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if(persistentData != null) {
            nbt.put(NBT_ID, persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    protected void readCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(NBT_ID, NbtElement.COMPOUND_TYPE)) {
            persistentData = nbt.getCompound(NBT_ID);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (!((Entity) (Object) this).getWorld().isClient()) {
            Random rand = ((Entity) (Object) this).getWorld().getRandom();
            if (((Entity) (Object) this).isInsideWall() && rand.nextBetween(1, 50) == 50) {
                LimlibTravelling.travelTo(((Entity) (Object) this), ((Entity) (Object) this).getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY), new TeleportTarget(
                                Vec3d.of(new Vec3i(rand.nextBetween(((Entity) (Object) this).getBlockX() - 200, ((Entity) (Object) this).getBlockX() + 200), 2, rand.nextBetween(((Entity) (Object) this).getBlockZ() - 200, ((Entity) (Object) this).getBlockZ() + 200))),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
            }
        }
    }
}
