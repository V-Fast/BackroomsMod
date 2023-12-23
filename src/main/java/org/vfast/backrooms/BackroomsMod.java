package org.vfast.backrooms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;
import org.vfast.backrooms.block.BackroomsBlocks;
import org.vfast.backrooms.block.entity.BackroomsBlockEntities;
import org.vfast.backrooms.block.interactable.RadioBlock;
import org.vfast.backrooms.config.BackroomsConfig;
import org.vfast.backrooms.entity.BackroomsEntities;
import org.vfast.backrooms.entity.BacteriaEntity;
import org.vfast.backrooms.item.BackroomsItems;
import org.vfast.backrooms.network.BackroomsNetworking;
import org.vfast.backrooms.sound.BackroomsSounds;
import org.vfast.backrooms.world.biome.BackroomsBiomes;
import org.vfast.backrooms.world.BackroomsDimensions;
import org.vfast.backrooms.world.chunk.BackroomsChunkGenerators;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

import java.util.Arrays;
import java.util.List;

public class BackroomsMod implements ModInitializer {

	public static final String ID = "backrooms";

	public static String NAME = "The Backrooms";

	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	private static final String VERSION_ID = FabricLoader.getInstance()
			.getModContainer(BackroomsMod.ID)
			.orElseThrow()
			.getMetadata()
			.getVersion()
			.toString();

	private static final List<RadioBlock.RadioRecord> records = Arrays.asList(
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.halls_tape.desc").getString(), BackroomsSounds.HALLS),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.duet_tape.desc").getString(), BackroomsSounds.DUET),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.drifting_tape.desc").getString(), BackroomsSounds.DRIFTING),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.no_time_to_explain_tape.desc").getString(), BackroomsSounds.NO_TIME_TO_EXPLAIN),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.tell_me_you_know_tape.desc").getString(), BackroomsSounds.TELL_ME_YOU_KNOW),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.orbit_tape.desc").getString(), BackroomsSounds.ORBIT),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.slingshot_tape.desc").getString(), BackroomsSounds.SLINGSHOT),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.thalassophobia_tape.desc").getString(), BackroomsSounds.THALASSOPHOBIA ),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.no_surprises_tape.desc").getString(), BackroomsSounds.NO_SURPRISES),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.its_just_a_burning_memory_tape.desc").getString(), BackroomsSounds.BURNING_MEMORY),
			new RadioBlock.RadioRecord(Text.translatable("item.backrooms.government_funding_tape.desc").getString(), BackroomsSounds.GOVERNMENT_FUNDING )
	);

	@Override
	public void onInitialize() {
		BackroomsConfig.HANDLER.load();
		BackroomsBlocks.registerBlocks();
		BackroomsBlockEntities.registerBlockEntities();
		BackroomsItems.registerItems();
		BackroomsSounds.registerSoundEvents();
		BackroomsBiomes.registerBiomes();
		BackroomsChunkGenerators.registerChunkGenerators();
		BackroomsNetworking.registerPackets();
		GeckoLib.initialize();
		BackroomsEntities.registerMobs();
		registerEvents();
		BackroomsMod.LOGGER.info("Initialized Backrooms");
	}

	public void registerEvents() {
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
			BackroomsConfig.HANDLER.save();
		});
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			if (BackroomsDimensions.isLevel(destination)) {
				StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StatusEffectInstance.INFINITE, 2, false, false, false);
				player.addStatusEffect(effect);
				player.setSpawnPoint(BackroomsDimensions.LEVEL_ZERO.key,
						player.getBlockPos(), // TODO make spawnpoint position randomized
						0.0f, true, false);
			} else if (BackroomsDimensions.isLevel(origin)){
				player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
			}
		});
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (player.getStackInHand(hand).isOf(BackroomsItems.BROKEN_BOTTLE)) {
				BacteriaEntity bacteria = (BacteriaEntity) entity;
				StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 2, false, false, false);
				bacteria.addStatusEffect(effect);
			}
			return ActionResult.PASS;
		});
	}

	public static List<RadioBlock.RadioRecord> getRecords() {
		return records;
	}
}
