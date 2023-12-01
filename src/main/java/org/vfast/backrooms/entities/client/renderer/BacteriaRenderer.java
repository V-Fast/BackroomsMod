package org.vfast.backrooms.entities.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.vfast.backrooms.entities.BacteriaEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.vfast.backrooms.entities.client.model.BacteriaModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class BacteriaRenderer extends GeoEntityRenderer<BacteriaEntity> {
    public BacteriaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BacteriaModel());
    }
}
