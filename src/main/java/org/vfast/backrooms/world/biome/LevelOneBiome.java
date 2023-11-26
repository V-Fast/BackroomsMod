package org.vfast.backrooms.world.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class LevelOneBiome {

    public static Biome create(RegistryEntryLookup<PlacedFeature> features, RegistryEntryLookup<ConfiguredCarver<?>> carvers) {
        Biome.Builder biome = new Biome.Builder();

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder generationSettings = new GenerationSettings.LookupBackedBuilder(features, carvers);

        BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder();
        biomeEffects.skyColor(10138336);
        biomeEffects.waterColor(10663127);
        biomeEffects.waterFogColor(329011);
        biomeEffects.fogColor(3223857);
        biomeEffects.grassColor(13818488);
        //biomeEffects.loopSound(RegistryEntry.of(ModSounds.SEALED_AWAY));
        //biomeEffects.moodSound(BiomeMoodSound.CAVE);
        BiomeEffects effects = biomeEffects.build();

        biome.spawnSettings(spawnSettings.build());
        biome.generationSettings(generationSettings.build());
        biome.effects(effects);

        biome.precipitation(false);

        biome.temperature(0.8F);
        biome.downfall(0.0F);

        return biome.build();
    }

}
