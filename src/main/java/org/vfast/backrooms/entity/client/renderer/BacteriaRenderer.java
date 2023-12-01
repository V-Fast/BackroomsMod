package org.vfast.backrooms.entity.client.renderer;

import org.vfast.backrooms.entity.BacteriaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.vfast.backrooms.entity.client.model.BacteriaModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class BacteriaRenderer extends GeoEntityRenderer<BacteriaEntity> {
    public BacteriaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BacteriaModel());
    }
}
