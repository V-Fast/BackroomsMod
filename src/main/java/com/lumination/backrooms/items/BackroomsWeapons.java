package com.lumination.backrooms.items;

import jdk.jfr.Experimental;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BackroomsWeapons {
    public static class ModSword extends SwordItem {
        public ModSword(float attackDamage, float attackSpeed, int durability, Settings settings) {
            super(new ModMaterial(durability, attackDamage - 1.0f), 0, attackSpeed - 4.0f, settings.maxCount(1));
        }
    }

    public static class ModAxe extends AxeItem {
        public ModAxe(float attackDamage, float attackSpeed, int durability, Settings settings) {
            super(new ModMaterial(durability, attackDamage - 1.0f), 0, attackSpeed - 4.0f, settings.maxCount(1));
        }
    }

    /**
     * @apiNote Does not work properly
     */
    @Experimental
    public static class ModThrowable extends Item {
        private static ModProjectile.ProjectileInfo projectileInfo;

        public ModThrowable(int damage, boolean dropOnHit, Settings settings) {
            super(settings);
            setProjectileInfo(new ModProjectile.ProjectileInfo(damage, this.getDefaultStack(), dropOnHit));
        }

        public static void setProjectileInfo(ModProjectile.ProjectileInfo info) {
            projectileInfo = info;
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClient) {
                ModProjectile modProjectile = new ModProjectile(world, user, projectileInfo);
                modProjectile.setItem(itemStack);
                modProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(modProjectile);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return TypedActionResult.success(itemStack, world.isClient());
        }
    }
}
