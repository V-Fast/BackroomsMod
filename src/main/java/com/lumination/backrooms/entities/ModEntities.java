package com.lumination.backrooms.entities;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.client.EntityRenderer;
import com.lumination.backrooms.entities.mod.EntityEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<EntityEntity> ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier(BackroomsMod.MOD_ID, "entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EntityEntity::new).dimensions(EntityDimensions.fixed(1.4f, 2.7f)).build());

    public static void registerMobs(boolean withAttributes) {
        EntityRendererRegistry.register(ModEntities.ENTITY, EntityRenderer::new);

        if (withAttributes == true) {
            registerAttributes();
        }
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.ENTITY, EntityEntity.setAttributes());
    }

    public static void registerMobs() {
        registerMobs(true);
    }
}
