package com.lumination.backrooms.entities.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.mod.BacteriaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class BacteriaModel extends AnimatedGeoModel<BacteriaEntity> {
    @Override
    public Identifier getModelResource(BacteriaEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "geo/entity_backrooms.geo.json");
    }

    @Override
    public Identifier getTextureResource(BacteriaEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "textures/entity/entity.png");
    }

    @Override
    public Identifier getAnimationResource(BacteriaEntity animatable) {
        return new Identifier(BackroomsMod.MOD_ID, "animations/entity_backrooms.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(BacteriaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
