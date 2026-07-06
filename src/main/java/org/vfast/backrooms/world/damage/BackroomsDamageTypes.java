package org.vfast.backrooms.world.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsDamageTypes {
    public static final ResourceKey<DamageType> NYCTOPHOBIA = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(BackroomsMod.ID, "nyctophobia"));
}
