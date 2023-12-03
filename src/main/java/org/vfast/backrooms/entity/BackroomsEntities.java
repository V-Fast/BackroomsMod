package org.vfast.backrooms.entity;

import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entity.client.renderer.BacteriaRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.vfast.backrooms.entity.client.renderer.SmilerRenderer;

public class BackroomsEntities {
    public static final EntityType<BacteriaEntity> BACTERIA = Registry.register(Registries.ENTITY_TYPE, new Identifier(BackroomsMod.MOD_ID, "bacteria"),
            QuiltEntityTypeBuilder.create(SpawnGroup.CREATURE, BacteriaEntity::new).setDimensions(EntityDimensions.fixed(1.4f, 2.7f)).build());

    public static final EntityType<SmilerEntity> SMILER = Registry.register(Registries.ENTITY_TYPE, new Identifier(BackroomsMod.MOD_ID, "smiler"),
            QuiltEntityTypeBuilder.create(SpawnGroup.CREATURE, SmilerEntity::new).setDimensions(EntityDimensions.fixed(1.8f, 1.6f)).build());

    public static void registerMobs() {
        BackroomsEntities.registerAttributes();
    }

    @ClientOnly
    public static void registerRenderers() {
        EntityRendererRegistry.register(BackroomsEntities.BACTERIA, BacteriaRenderer::new);
        EntityRendererRegistry.register(BackroomsEntities.SMILER, SmilerRenderer::new);
    }

    @SuppressWarnings("deprecation")
    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(BackroomsEntities.BACTERIA, BacteriaEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(BackroomsEntities.SMILER, SmilerEntity.setAttributes());
    }
}
