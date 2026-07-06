package org.vfast.backrooms.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsLevels {
    public static final ResourceKey<Level> LEVEL_0 = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(BackroomsMod.ID, "level_0"));
}
