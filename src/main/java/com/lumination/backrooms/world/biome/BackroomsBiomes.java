package com.lumination.backrooms.world.biome;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.world.BackroomsDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class BackroomsBiomes {

    public static final RegistryKey<Biome> LEVEL_ZERO_BIOME = get(BackroomsDimensions.LEVEL_ZERO_ID);
    public static final RegistryKey<Biome> LEVEL_ONE_BIOME = get(BackroomsDimensions.LEVEL_ONE_ID);

    public static RegistryKey<Biome> get(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }

    public static void registerBiomes() {
        BackroomsMod.LOGGER.debug("Registered Biomes");
    }
}
