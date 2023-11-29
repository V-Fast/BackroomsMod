package org.vfast.backrooms.entities.client;

import org.vfast.backrooms.entities.BacteriaEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@ClientOnly
public class BacteriaRenderer extends GeoEntityRenderer<BacteriaEntity> {
    public BacteriaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BacteriaModel());
    }
}
