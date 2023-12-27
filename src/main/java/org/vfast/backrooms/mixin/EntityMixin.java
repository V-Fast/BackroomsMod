package org.vfast.backrooms.mixin;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Shadow public abstract World getWorld();

    @Shadow public abstract boolean isInsideWall();

    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Shadow public abstract int getBlockX();

    @Shadow public abstract int getBlockZ();

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
        World world = getWorld();
        if (!world.isClient()) {
            MinecraftServer server = getServer();
            Random rand = world.getRandom();
            if (isInsideWall() && rand.nextBetween(1, 50) == 50) {
                LimlibTravelling.travelTo(((Entity) (Object) this), BackroomsDimensions.LEVEL_ZERO.getWorld(server), new TeleportTarget(
                                Vec3d.of(new Vec3i(rand.nextBetween(getBlockX() - 200, getBlockX() + 200), 2, rand.nextBetween(getBlockZ() - 200, getBlockZ() + 200))),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        BackroomsSounds.CAMERA_CLICK, 5.0f, 1.0f);
            }

            if (world == BackroomsDimensions.LEVEL_RUN.getWorld(server) && getBlockZ() >= 805) {
                LimlibTravelling.travelTo(((Entity) (Object) this), server.getOverworld(), new TeleportTarget(
                                Vec3d.of(server.getOverworld().getSpawnPos()),
                                Vec3d.ZERO, 0.0f, 0.0f),
                        null, 0.0f, 0.0f);
            }
        }
    }
}
