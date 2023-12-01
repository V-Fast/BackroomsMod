package org.vfast.backrooms.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entities.client.renderer.BacteriaRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.vfast.backrooms.entities.client.renderer.SmilerRenderer;

public class BackroomsEntities {
    public static final EntityType<BacteriaEntity> BACTERIA = Registry.register(Registries.ENTITY_TYPE, new Identifier(BackroomsMod.MOD_ID, "bacteria"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BacteriaEntity::new).dimensions(EntityDimensions.fixed(1.4f, 2.7f)).build());

    public static final EntityType<SmilerEntity> SMILER = Registry.register(Registries.ENTITY_TYPE, new Identifier(BackroomsMod.MOD_ID, "smiler"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SmilerEntity::new).dimensions(EntityDimensions.fixed(1.8f, 1.6f)).build());

    public static void registerMobs() {
        BackroomsEntities.registerAttributes();
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderers() {
        EntityRendererRegistry.register(BackroomsEntities.BACTERIA, BacteriaRenderer::new);
        EntityRendererRegistry.register(BackroomsEntities.SMILER, SmilerRenderer::new);
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(BackroomsEntities.BACTERIA, BacteriaEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(BackroomsEntities.SMILER, SmilerEntity.setAttributes());
    }
}
