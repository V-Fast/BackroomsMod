package com.lumination.backrooms.sounds;

import com.lumination.backrooms.BackroomsMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BackroomsSounds {
    public static SoundEvent LIGHT_BUZZING = registerSoundEvent("light_buzzing");
    public static SoundEvent CAMERA_CLICK = registerSoundEvent("camera_click");

    // Music Tapes
    public static SoundEvent HALLS = registerSoundEvent("halls");
    public static SoundEvent COMPLEX = registerSoundEvent("the_complex");
    public static SoundEvent GOVERNMENT_FUNDING = registerSoundEvent("government_funding");
    public static SoundEvent INSTANCES = registerSoundEvent("instances");
    public static SoundEvent WARM_NIGHTS = registerSoundEvent("warm_nights");
    public static SoundEvent SNOW_WORLD = registerSoundEvent("snow_world");
    public static SoundEvent TITLE_SCREEN = registerSoundEvent("title_screen");
    public static SoundEvent AUDITORY_GUIDEPOST = registerSoundEvent("auditory_guidepost");
    public static SoundEvent NOT_YOUR_DECISION = registerSoundEvent("not_your_decision");
    public static SoundEvent SEALED_AWAY = registerSoundEvent("sealed_away");
    public static SoundEvent CLIFFS_OF_DOVER = registerSoundEvent("the_white_cliffs_of_dover");
    public static SoundEvent THALASSOPHOBIA = registerSoundEvent("thalassophobia");
    public static SoundEvent BURNING_MEMORY = registerSoundEvent("its_just_a_burning_memory");
    public static SoundEvent DUET = registerSoundEvent("duet");
    public static SoundEvent EMPTY_BLISS = registerSoundEvent("empty_bliss_beyond_this_world");
    public static SoundEvent GLIMPSES_OF_HOPE = registerSoundEvent("glimpses_of_hope_in_trying_times");
    public static SoundEvent GROSS = registerSoundEvent("gross");
    public static SoundEvent HAVE_MANY_DAYS = registerSoundEvent("we_dont_have_many_days");
    public static SoundEvent AFTERNOON_DRIFTING = registerSoundEvent("late_afternoon_drifting");
    public static SoundEvent MY_HEART_BREAKS = registerSoundEvent("does_it_matter_how_my_heart_breaks");
    public static SoundEvent THE_VIOLIN = registerSoundEvent("the_violin");
    public static SoundEvent WORLD_FADES_AWAY = registerSoundEvent("place_in_the_world_fades_away");
    public static SoundEvent AHIRU_NO_SENTAKU = registerSoundEvent("ahiru_no_sentaku");
    public static SoundEvent NO_SURPRISES = registerSoundEvent("no_surprises");

    // Radio
    public static SoundEvent SLINGSHOT = registerSoundEvent("slingshot");
    public static SoundEvent ORBIT = registerSoundEvent("orbit");
    public static SoundEvent DRIFTING = registerSoundEvent("drifting");
    public static SoundEvent TELL_ME_YOU_KNOW = registerSoundEvent("tell_me_you_know");
    public static SoundEvent NO_TIME_TO_EXPLAIN = registerSoundEvent("no_time_to_explain");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(BackroomsMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSoundEvents() {
        BackroomsMod.print("Registering ModSounds for " + BackroomsMod.MOD_ID);
    }
}
