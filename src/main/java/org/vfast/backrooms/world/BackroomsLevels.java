package org.vfast.backrooms.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.vfast.backrooms.BackroomsMod;

import java.util.List;

public class BackroomsLevels {
    private static final List<ResourceKey<Level>> BACKROOM_LEVELS;
    public static final ResourceKey<Level> LEVEL_0 = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(BackroomsMod.ID, "level_0"));

    public static boolean isBackrooms(ResourceKey<Level> level) {
        return BACKROOM_LEVELS.contains(level);
    }

    static {
        BACKROOM_LEVELS = List.of(BackroomsLevels.LEVEL_0);
    }
}
