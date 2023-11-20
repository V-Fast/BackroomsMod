package com.lumination.backrooms;

import com.lumination.backrooms.blocks.BackroomsBlocks;
import com.lumination.backrooms.blocks.entity.BackroomsBlockEntities;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.entities.BackroomsEntities;
import com.lumination.backrooms.items.BackroomsItems;
import com.lumination.backrooms.sounds.BackroomsSounds;
import com.lumination.backrooms.world.biome.BackroomsBiomes;
import com.lumination.backrooms.world.dimensions.BackroomsDimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.event.api.EntityWorldChangeEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class BackroomsMod implements ModInitializer {

	public static final String MOD_ID = "backrooms";

	public static final Logger LOGGER = LoggerFactory.getLogger("The Backrooms");

	private static final String VERSION_ID = QuiltLoader
			.getModContainer(BackroomsMod.MOD_ID)
			.orElseThrow()
			.metadata()
			.version()
			.toString();

	public static final String SETTINGS_NAME = "the_backrooms";

	private static final List<Radio.RadioRecord> records = Arrays.asList(
			new Radio.RadioRecord(Text.translatable("item.backrooms.halls_tape.desc").getString(), BackroomsSounds.HALLS),
			new Radio.RadioRecord(Text.translatable("item.backrooms.duet_tape.desc").getString(), BackroomsSounds.DUET),
			new Radio.RadioRecord(Text.translatable("item.backrooms.drifting_tape.desc").getString(), BackroomsSounds.DRIFTING),
			new Radio.RadioRecord(Text.translatable("item.backrooms.no_time_to_explain_tape.desc").getString(), BackroomsSounds.NO_TIME_TO_EXPLAIN),
			new Radio.RadioRecord(Text.translatable("item.backrooms.tell_me_you_know_tape.desc").getString(), BackroomsSounds.TELL_ME_YOU_KNOW),
			new Radio.RadioRecord(Text.translatable("item.backrooms.orbit_tape.desc").getString(), BackroomsSounds.ORBIT),
			new Radio.RadioRecord(Text.translatable("item.backrooms.slingshot_tape.desc").getString(), BackroomsSounds.SLINGSHOT),
			new Radio.RadioRecord(Text.translatable("item.backrooms.thalassophobia_tape.desc").getString(), BackroomsSounds.THALASSOPHOBIA ),
			new Radio.RadioRecord(Text.translatable("item.backrooms.no_surprises_tape.desc").getString(), BackroomsSounds.NO_SURPRISES),
			new Radio.RadioRecord(Text.translatable("item.backrooms.its_just_a_burning_memory_tape.desc").getString(), BackroomsSounds.BURNING_MEMORY),
			new Radio.RadioRecord(Text.translatable("item.backrooms.government_funding_tape.desc").getString(), BackroomsSounds.GOVERNMENT_FUNDING )
	);

	public static String NAME = "The Backrooms";

	@Override
	public void onInitialize(ModContainer mod) {
		BackroomsBlocks.registerModBlock();
		BackroomsBlockEntities.registerBlockEntities();
		BackroomsItems.registerModItems();
		BackroomsSounds.registerSoundEvents();
		BackroomsBiomes.init();
		BackroomsEntities.registerMobs();
		EntityWorldChangeEvents.AFTER_PLAYER_WORLD_CHANGE.register((player, origin, destination) -> {
			if (destination == player.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY)) {
				StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StatusEffectInstance.INFINITE, 1, true, false, false);
				player.addStatusEffect(effect);
				player.setSpawnPoint(BackroomsDimensions.LEVEL_ZERO_KEY,
						player.getBlockPos(), // TODO make position randomized
						0.0f, true, false);
			} else if (origin == player.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY)){
				player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
			}
		});
		BackroomsMod.LOGGER.info("Initialized Backrooms");
	}

	public static void changeName(String name) {
		NAME = name;
	}

	public static List<Radio.RadioRecord> getRecords() {
		return records;
	}
}
