package com.lumination.backrooms.world.dimensions;

import com.lumination.backrooms.BackroomsMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class BackroomDimensions {
    public static final RegistryKey<World> LVL0_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY,
            new Identifier(BackroomsMod.MOD_ID, "level_0"));
    public static final RegistryKey<DimensionType> BR_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, LVL0_DIMENSION_KEY.getRegistry());

    public static void registerDims() {
        BackroomsMod.print("Registered custom dimensions");
    }
}

