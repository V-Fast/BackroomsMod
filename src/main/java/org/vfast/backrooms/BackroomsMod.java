package org.vfast.backrooms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.vfast.backrooms.block.BackroomsBlocks;
import org.vfast.backrooms.block.entity.BackroomsBlockEntities;
import org.vfast.backrooms.dimensions.BackroomsDimensions;
import org.vfast.backrooms.gamerules.BackroomsGamerules;
import org.vfast.backrooms.item.BackroomsItems;
import org.vfast.backrooms.sound.BackroomsSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsMod implements ModInitializer {

	public static final String ID = "backrooms";
	public static final String NAME = "The Backrooms";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	private static final String VERSION_ID = FabricLoader.getInstance()
			.getModContainer(BackroomsMod.ID)
			.orElseThrow()
			.getMetadata()
			.getVersion()
			.toString();

	@Override
	public void onInitialize() {
		// Enregistrement des blocs, entités, items, sons, dimensions, et règles du jeu
		BackroomsBlocks.registerBlocks();
		BackroomsBlockEntities.registerBlockEntities();
		BackroomsItems.registerItems();
		BackroomsSounds.registerSoundEvents();
		BackroomsDimensions.registerDimensions();
		BackroomsGamerules.registerGamerules();

		// Initialisation des portails
		ServerLifecycleEvents.SERVER_STARTED.register(server -> BackroomsDimensions.initPortal());

		LOGGER.info("Initialized Backrooms");
	}
}
