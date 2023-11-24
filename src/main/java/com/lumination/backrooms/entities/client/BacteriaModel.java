package com.lumination.backrooms.entities.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.BacteriaEntity;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@ClientOnly
public class BacteriaModel extends GeoModel<BacteriaEntity> {
    @Override
    public Identifier getModelResource(BacteriaEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "geo/bacteria.geo.json");
    }

    @Override
    public Identifier getTextureResource(BacteriaEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "textures/entity/bacteria.png");
    }

    @Override
    public Identifier getAnimationResource(BacteriaEntity animatable) {
        return new Identifier(BackroomsMod.MOD_ID, "animations/bacteria.animation.json");
    }

    @Override
    public void setCustomAnimations(BacteriaEntity animatable, long instanceId, AnimationState<BacteriaEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
