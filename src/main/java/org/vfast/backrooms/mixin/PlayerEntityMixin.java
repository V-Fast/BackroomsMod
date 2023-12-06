package org.vfast.backrooms.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vfast.backrooms.config.BackroomsConfig;
import org.vfast.backrooms.entity.damage.BackroomsDamageTypes;
import org.vfast.backrooms.item.BackroomsItems;
import org.vfast.backrooms.network.BackroomsNetworking;
import org.vfast.backrooms.util.accessor.IPlayerSanityAccessor;
import org.vfast.backrooms.world.BackroomsDimensions;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IPlayerSanityAccessor {

    @Unique
    private int sanityTimer = 0;
    @Unique
    private int sanityDamageTimer = 0;

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean isCreative();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        this.backrooms$getPersistentData().putInt("sanity", 10);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (!getWorld().isClient()) {
            if (BackroomsDimensions.isInBackrooms(this)) {
                sanityTimer++;
                if (sanityTimer >= BackroomsConfig.HANDLER.instance().looseSanitySpeed * 60 * 20) {
                    modifySanity(-1);
                    sanityTimer = 0;
                }
            } else {
                sanityTimer++;
                if (sanityTimer >= BackroomsConfig.HANDLER.instance().recoverSanitySpeed * 60 * 20) {
                    modifySanity(1);
                    sanityTimer = 0;
                }
            }
            if (getSanity() <= 3 && !this.isSpectator() && !this.isCreative()) {
                //((ServerPlayerEntity)(Object)this).sendMessage(Text.literal("Sanity: "+getSanity()));
                //((ServerPlayerEntity)(Object)this).sendMessage(Text.literal(String.valueOf(BackroomsConfig.HANDLER.instance().looseSanitySpeed)));
                //((ServerPlayerEntity)(Object)this).sendMessage(Text.literal(String.valueOf(sanityTimer)));
                sanityDamageTimer++;
                if (sanityDamageTimer >= 40) {
                    sanityDamageTimer = 0;
                    addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 0, false, false, false));
                    addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20, 0, false, false, false));
                    if (getSanity() <= 2) {
                        addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 20, 0, false, false, false));
                    }
                    if (getSanity() == 1) {
                        damage(BackroomsDamageTypes.of(getWorld(), BackroomsDamageTypes.LOW_SANITY), 1.0f);
                        addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20, 0, false, false, false));
                    }
                }

            }
        }
    }

    @Inject(method = "eatFood", at = @At("TAIL"))
    private void onEat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!getWorld().isClient()) {
            if (stack.isOf(BackroomsItems.ALMOND_WATER)) {
                modifySanity(1);
            }
            if (stack.isOf(BackroomsItems.COOKED_ALMOND_WATER)) {
                modifySanity(2);
            }
        }
    }

    @Override
    public void modifySanity(int amount) {
        if (!getWorld().isClient()) {
            NbtCompound nbt = this.backrooms$getPersistentData();
            int sanity = nbt.getInt("sanity");
            if (sanity + amount > 10) {
                sanity = 10;
            } else if (sanity + amount < 1) {
                sanity = 1;
            } else {
                sanity += amount;
            }
            nbt.putInt("sanity", sanity);
            syncSanity();
        }
    }

    @Override
    public int getSanity() {
        return this.backrooms$getPersistentData().getInt("sanity");
    }

    public void syncSanity() {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(getSanity());
        ServerPlayNetworking.send(((ServerPlayerEntity)(Object)this), BackroomsNetworking.SANITY_SYNC_ID, buffer);
    }


}
