package org.vfast.backrooms.entity.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsDamageTypes {
    public static final RegistryKey<DamageType> LOW_SANITY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(BackroomsMod.ID, "low_sanity"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}
