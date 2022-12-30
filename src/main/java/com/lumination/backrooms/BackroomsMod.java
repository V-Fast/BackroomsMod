package com.lumination.backrooms;

import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.sounds.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class BackroomsMod implements ModInitializer {
	public static final String MOD_ID = "backrooms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String SETTINGS_NAME = "the_backrooms";
	private static final List<Radio.RadioRecord> records = Arrays.asList(
			null,
			new Radio.RadioRecord(Text.translatable("item.backrooms.halls_tape.desc").getString(), ModSounds.HALLS),
			new Radio.RadioRecord(Text.translatable("item.backrooms.duet_tape.desc").getString(), ModSounds.DUET),
			new Radio.RadioRecord(Text.translatable("item.backrooms.drifting_tape.desc").getString(), ModSounds.DRIFTING),
			new Radio.RadioRecord(Text.translatable("item.backrooms.no_time_to_explain_tape.desc").getString(), ModSounds.NO_TIME_TO_EXPLAIN),
			new Radio.RadioRecord(Text.translatable("item.backrooms.tell_me_you_know_tape.desc").getString(), ModSounds.TELL_ME_YOU_KNOW),
			new Radio.RadioRecord(Text.translatable("item.backrooms.orbit.desc").getString(), ModSounds.ORBIT),
			new Radio.RadioRecord(Text.translatable("item.backrooms.slingshot.desc").getString(), ModSounds.SLINGSHOT),
			new Radio.RadioRecord(Text.translatable("item.backrooms.thalassophobia_tape.desc").getString(), ModSounds.THALASSOPHOBIA ),
			new Radio.RadioRecord(Text.translatable("item.backrooms.no_surprises_tape.desc").getString(), ModSounds.NO_SURPRISES),
			new Radio.RadioRecord(Text.translatable("item.backrooms.its_just_a_burning_memory_tape.desc").getString(), ModSounds.BURNING_MEMORY),
			new Radio.RadioRecord(Text.translatable("item.backrooms.government_funding_tape.desc").getString(), ModSounds.GOVERNMENT_FUNDING )
	);

	public static String NAME = "The Backrooms";

	@Override
	public void onInitialize() {
		print("Initialized Main Backrooms");
	}

	public static void print(String s) {
		LOGGER.info("[" + NAME + "] " + s);
	}

	public static void changeName(String name) {
		NAME = name;
	}

	public static List<Radio.RadioRecord> getRecords() {
		return records;
	}
}
