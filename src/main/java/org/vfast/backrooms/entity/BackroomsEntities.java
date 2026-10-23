package org.vfast.backrooms.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entity.stilllife.StillLife;

public class BackroomsEntities {
    public static final EntityType<StillLife> STILL_LIFE = register("still_life", EntityType.Builder.of(StillLife::new, MobCategory.MONSTER)
            .sized(0.6F, 1.8F)
            .eyeHeight(1.62F)
            .vehicleAttachment(Avatar.DEFAULT_VEHICLE_ATTACHMENT)
            .clientTrackingRange(10)
            .noLootTable());

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BackroomsMod.ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(STILL_LIFE, StillLife.createAttributes());
        SpawnPlacements.register(STILL_LIFE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StillLife::checkStillLifeSpawnRules);
        BackroomsMod.LOGGER.info("[BackroomsMod] Entities initialized");
    }
}
