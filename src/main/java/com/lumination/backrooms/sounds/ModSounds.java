package com.lumination.backrooms.sounds;

import com.lumination.backrooms.BackroomsMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent LIGHT_BUZZING = registerSoundEvent("light_buzzing");
    public static SoundEvent CAMERA_CLICK = registerSoundEvent("camera_click");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(BackroomsMod.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void registerSoundEvents() {
        BackroomsMod.print("Registering ModSounds for " + BackroomsMod.MOD_ID);
    }
}
