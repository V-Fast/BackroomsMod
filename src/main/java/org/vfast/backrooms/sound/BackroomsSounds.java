package org.vfast.backrooms.sound;

import org.vfast.backrooms.BackroomsMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BackroomsSounds {
    public static SoundEvent LIGHT_BUZZING = registerSoundEvent("light_buzzing");
    public static SoundEvent CAMERA_CLICK = registerSoundEvent("camera_click");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(BackroomsMod.ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSoundEvents() {}
}
