package com.lumination.backrooms.entities.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.mod.EntityEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EntityRenderer extends GeoEntityRenderer<EntityEntity> {
    public EntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EntityModel());
    }

    @Override
    public Identifier getTextureResource(EntityEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "textures/entity/entity.png");
    }
}
