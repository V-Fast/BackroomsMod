package org.vfast.backrooms.sounds;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsSounds {
    public static final Holder<SoundEvent> LIGHT_BUZZING = registerSound("light_buzzing");
    public static final Holder<SoundEvent> SHALLOW_BUZZING = registerSound("shallow_buzzing");
    public static final Holder<SoundEvent> MONSTER_NOISE = registerSound("monster_noise");
    public static final Holder<SoundEvent> NOCLIP = registerSound("noclip");
    public static final Holder<SoundEvent> NOCLIP_SMALL = registerSound("noclip_small");

    private static Holder<SoundEvent> registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(BackroomsMod.ID, id);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void registerSounds() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Sounds initialized");
    }
}
