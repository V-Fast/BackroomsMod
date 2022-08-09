package com.lumination.backrooms.entities.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.mod.EntityEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class EntityModel extends AnimatedGeoModel<EntityEntity> {
    @Override
    public Identifier getModelResource(EntityEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "geo/entity_backrooms.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "textures/entity/entity.png");
    }

    @Override
    public Identifier getAnimationResource(EntityEntity animatable) {
        return new Identifier(BackroomsMod.MOD_ID, "animations/entity_backrooms.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(EntityEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
