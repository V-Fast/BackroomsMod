package org.vfast.backrooms;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfast.backrooms.blocks.BackroomsBlocks;
import org.vfast.backrooms.blocks.entity.BackroomsBlockEntities;
import org.vfast.backrooms.entity.BackroomsEntities;
import org.vfast.backrooms.items.BackroomsComponents;
import org.vfast.backrooms.items.BackroomsItems;
import org.vfast.backrooms.items.BackroomsItemsGroup;
import org.vfast.backrooms.network.BackroomsNetworking;
import org.vfast.backrooms.sounds.BackroomsSounds;
import org.vfast.backrooms.world.BackroomsGameRules;
import org.vfast.backrooms.world.ThresholdTeleporter;
import org.vfast.backrooms.world.generation.BackroomsPieceTypes;

public class BackroomsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("backrooms");
    public static final String ID = "backrooms";

    @Override
    public void onInitialize() {
        BackroomsBlocks.registerBlocks();
        BackroomsBlockEntities.registerBlockEntities();
        BackroomsEntities.registerEntities();
        BackroomsItems.registerItems();
        BackroomsComponents.registerComponents();
        BackroomsItemsGroup.registerItemGroups();
        BackroomsSounds.registerSounds();
        BackroomsGameRules.registerGameRules();
        BackroomsPieceTypes.registerStructures();
        BackroomsNetworking.registerNetwork();
        ThresholdTeleporter.register();

        LOGGER.info("[BackroomsMod] Mod initialized");
    }
}