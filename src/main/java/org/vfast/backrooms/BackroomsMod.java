package org.vfast.backrooms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.vfast.backrooms.block.BackroomsBlocks;
import org.vfast.backrooms.block.entity.BackroomsBlockEntities;
import org.vfast.backrooms.item.BackroomsItems;
import org.vfast.backrooms.sound.BackroomsSounds;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

	@Override
	public void onInitialize() {
//		BackroomsConfig.HANDLER.load();
		BackroomsBlocks.registerBlocks();
		BackroomsBlockEntities.registerBlockEntities();
		BackroomsItems.registerItems();
		BackroomsSounds.registerSoundEvents();
//		BackroomsBiomes.registerBiomes();
//		BackroomsChunkGenerators.registerChunkGenerators();
//		BackroomsNetworking.registerPackets();
//		GeckoLib.initialize();
//		BackroomsEntities.registerMobs();
//		registerCommands();
		registerEvents();
		BackroomsMod.LOGGER.info("Initialized Backrooms");
	}

//	public void registerCommands() {
//		CommandRegistrationCallback.EVENT.register(SanityCommand::register);
//	}

	public void registerEvents() {
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
//			BackroomsConfig.HANDLER.save();
		});
//        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
//			if (BackroomsDimensions.isLevel(destination)) {
//				StatusEffectInstance miningFatigue = new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StatusEffectInstance.INFINITE, 2, false, false, false);
//				player.addStatusEffect(miningFatigue);
//				player.setSpawnPoint(BackroomsDimensions.LEVEL_ZERO.key,// TODO Make this the same as origin dimension
//						player.getBlockPos(), // TODO make spawnpoint position randomized
//						0.0f, true, false);
//			} else if (BackroomsDimensions.isLevel(origin)){
//				ServerWorld overworld = player.getServer().getOverworld();
//				player.setSpawnPoint(overworld.getRegistryKey(), overworld.getSpawnPos(), overworld.getSpawnAngle(), true, false);
//				player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
//			}
//		});
//		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
//			if (newEntity instanceof LivingEntity entity) {
//				if (destination == BackroomsDimensions.LEVEL_RUN.getWorld(entity.getServer())) {
//					StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, StatusEffectInstance.INFINITE, 0, false, false, false);
//					entity.addStatusEffect(speed);
//				} else if (origin == BackroomsDimensions.LEVEL_RUN.getWorld(entity.getServer())) {
//					entity.removeStatusEffect(StatusEffects.SPEED);
//				}
//			}
//		});
//		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
//			if (player.getStackInHand(hand).isOf(BackroomsItems.BROKEN_BOTTLE)) {
//				BacteriaEntity bacteria = (BacteriaEntity) entity;
//				StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 2, false, false, false);
//				bacteria.addStatusEffect(effect);
//			}
//			return ActionResult.PASS;
//		});
	}
}