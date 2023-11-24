package com.lumination.backrooms;

import com.lumination.backrooms.blocks.BackroomsBlocks;
import com.lumination.backrooms.blocks.entity.BackroomsBlockEntities;
import com.lumination.backrooms.blocks.interactable.Radio;
import com.lumination.backrooms.entities.BackroomsEntities;
import com.lumination.backrooms.items.BackroomsItems;
import com.lumination.backrooms.sounds.BackroomsSounds;
import com.lumination.backrooms.world.biome.BackroomsBiomes;
import com.lumination.backrooms.world.BackroomsDimensions;
import com.lumination.backrooms.world.chunk.BackroomsChunkGenerators;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.event.api.EntityWorldChangeEvents;
import org.quiltmc.qsl.entity.event.api.ServerEntityTickCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class BackroomsMod implements ModInitializer {

	public static final String MOD_ID = "backrooms";

	public static String NAME = "The Backrooms";

	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

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

	@Override
	public void onInitialize(ModContainer mod) {
		BackroomsBlocks.registerModBlock();
		BackroomsBlockEntities.registerBlockEntities();
		BackroomsItems.registerModItems();
		BackroomsSounds.registerSoundEvents();
		BackroomsBiomes.registerBiomes();
		BackroomsChunkGenerators.registerChunkGenerators();
		BackroomsEntities.registerMobs();
		registerEvents();
		BackroomsMod.LOGGER.info("Initialized Backrooms");
	}

	public void registerEvents() {
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
		ServerEntityTickCallback.EVENT.register((entity, isPassengerTick) -> {
			Random rand = entity.getWorld().getRandom();
			if (entity.isInsideWall() && rand.nextBetween(1, 50) == 50) {
				LimlibTravelling.travelTo(entity, entity.getServer().getWorld(BackroomsDimensions.LEVEL_ZERO_KEY), new TeleportTarget(
								Vec3d.of(new Vec3i(rand.nextBetween(entity.getBlockX()-200, entity.getBlockX()+200), 2, rand.nextBetween(entity.getBlockZ()-200, entity.getBlockZ()+200))),
								Vec3d.ZERO, 0.0f, 0.0f),
						/* TODO add sound effect */ null, 5.0f, 1.0f);
			}
		});
	}

	public static List<Radio.RadioRecord> getRecords() {
		return records;
	}
}
