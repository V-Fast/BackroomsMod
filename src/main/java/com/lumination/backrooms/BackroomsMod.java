package com.lumination.backrooms;

import com.lumination.backrooms.client.settings.BackroomsSettings;
import com.lumination.backrooms.utils.ModRegisteries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsMod implements ModInitializer {
	public static final String MOD_ID = "backrooms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String SETTINGS_NAME = "the_backrooms";

	public static String NAME = "The Backrooms";

	@Override
	public void onInitialize() {
		ModRegisteries.registerMod();

		print("Initialized Backrooms");
	}

	public static void print(String s) {
		LOGGER.info("[" + NAME + "] " + s);
	}

	public static void changeName(String name) {
		NAME = name;
	}
}
