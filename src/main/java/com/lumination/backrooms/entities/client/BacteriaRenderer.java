package com.lumination.backrooms.entities.client;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.entities.mod.BacteriaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class BacteriaRenderer extends GeoEntityRenderer<BacteriaEntity> {
    public BacteriaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BacteriaModel());
    }

    @Override
    public Identifier getTextureLocation(BacteriaEntity object) {
        return new Identifier(BackroomsMod.MOD_ID, "textures/entity/bacteria.png");
    }
}
