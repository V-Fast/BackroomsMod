package org.vfast.backrooms.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ModProjectile extends ThrownItemEntity {
    private static ProjectileInfo projectileInfo;

    public ModProjectile(World world, LivingEntity owner, ProjectileInfo info) {
        super(EntityType.SNOWBALL, owner, world);
        setItem(info.item);
    }

    public static void setProjectileInfo(ProjectileInfo projectileInfo) {
        ModProjectile.projectileInfo = projectileInfo;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(this.getWorld().getDamageSources().thrown(this, this.getOwner()), projectileInfo.damage);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            if (projectileInfo.dropOnHit) {
                this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), projectileInfo.item));
            }
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    public static class ProjectileInfo {
        public int damage;
        public ItemStack item;
        public boolean dropOnHit;

        public ProjectileInfo(int damage, ItemStack itemStack, boolean dropOnHit) {
            this.damage = damage;
            this.item = itemStack;
            this.dropOnHit = dropOnHit;
        }
    }
}

