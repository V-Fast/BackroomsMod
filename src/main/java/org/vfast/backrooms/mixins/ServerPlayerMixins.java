package org.vfast.backrooms.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vfast.backrooms.blocks.BackroomsBlocks;
import org.vfast.backrooms.interfaces.DarknessDamage;
import org.vfast.backrooms.interfaces.Noclippable;
import org.vfast.backrooms.world.BackroomsGameRules;
import org.vfast.backrooms.world.BackroomsLevels;
import org.vfast.backrooms.world.damage.BackroomsDamageTypes;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixins extends Player implements DarknessDamage, Noclippable {
    @Shadow
    public abstract ServerLevel level();

    @Unique
    private int lastNoclipTick = 0;

    @Unique
    private boolean lookingForNoclip = false;

    @Unique
    private int darknessTick = 0;

    @Unique
    private int oldDuration = -1;

    @Unique
    private static final int MINIMUM_LIGHT = 1;

    public ServerPlayerMixins(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void backroomsTick(CallbackInfo ci) {
        boolean fullyImmersed = this.level().getGameRules().get(BackroomsGameRules.FULL_IMMERSION);
        ResourceKey<Level> levelKey = this.level().dimension();

        boolean canNoclip = !this.lookingForNoclip && this.tickCount >= this.lastNoclipTick + Noclippable.NOCLIP_TICKS && !this.getAbilities().invulnerable;

        if (levelKey == BackroomsLevels.LEVEL_0 && fullyImmersed) {
            this.tickDarkness();
            this.tickFood();
        } else if (levelKey == Level.OVERWORLD && fullyImmersed && canNoclip) {
            this.lookingForNoclip = true;
            this.tickNoclip();
        }
    }

    @Unique
    private void tickNoclip() {
        BlockPos replacer = this.lookAround(this.level(), this.blockPosition());
        assert replacer != null;

        ServerLevel level = this.level();
        level.setBlockAndUpdate(replacer, BackroomsBlocks.FAKE_BLOCK.defaultBlockState());
        level.setBlockAndUpdate(replacer.below(1), BackroomsBlocks.FAKE_BLOCK.defaultBlockState());
        level.setBlockAndUpdate(replacer.below(2), BackroomsBlocks.FAKE_BLOCK.defaultBlockState());

        this.lookingForNoclip = false;
        this.lastNoclipTick = this.tickCount;
    }

    @Override
    @Unique
    public void tickDarkness() {
        if (this.getBlockLight(this) <= ServerPlayerMixins.MINIMUM_LIGHT && !this.getAbilities().invulnerable) {
            this.darknessTick++;
            boolean shouldWarn = this.darknessTick >= DarknessDamage.TICK_PREVENT;
            boolean shouldAttack = this.darknessTick >= DarknessDamage.TICK_ATTACK;
            if (shouldWarn) {
                this.prevent();
                if (shouldAttack) {
                    this.darknessTick = DarknessDamage.TICK_ATTACK;
                    this.attackEntity();
                }
            }
        } else {
            this.darknessTick = Math.max(this.darknessTick - 1, 0);
        }
    }

    @Override
    @Unique
    public void prevent() {
        int effectDuration = this.getDarknessDuration();

        if (this.hasEffect(MobEffects.DARKNESS) && this.oldDuration != effectDuration) {
            this.oldDuration = effectDuration;
            this.removeEffect(MobEffects.DARKNESS);
        }

        MobEffectInstance mobEffect = new MobEffectInstance(MobEffects.DARKNESS, effectDuration * 20, 0, false, false, false);
        this.addEffect(mobEffect);
    }

    @Override
    @Unique
    public void performAttack() {
        ResourceKey<Level> levelKey = this.level().dimension();

        MinecraftServer server = this.level().getServer();
        assert server != null;

        ServerLevel serverLevel = server.getLevel(levelKey);
        assert serverLevel != null;

        DamageSource levelDamage = new DamageSource(
                this.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(BackroomsDamageTypes.NYCTOPHOBIA.identifier()).orElseThrow()
        );

        Vec3 lastMove = this.getDeltaMovement();
        this.hurtServer(serverLevel, levelDamage, 3.0f);
        this.setDeltaMovement(lastMove);
        this.darknessTick = this.darknessTick - DarknessDamage.ATTACK_TICK_RATE;
    }

    @Unique
    private int getDarknessDuration() {
        float preventTick = this.darknessTick - DarknessDamage.TICK_PREVENT;
        assert preventTick >= 0;

        float maxTick = DarknessDamage.TICK_ATTACK - DarknessDamage.TICK_PREVENT;
        float duration = ((preventTick / maxTick) * 65 + 65) / 20;
        return Math.round(duration);
    }

    @Unique
    private void tickFood() {
        this.foodData.setFoodLevel(20);
    }
}
