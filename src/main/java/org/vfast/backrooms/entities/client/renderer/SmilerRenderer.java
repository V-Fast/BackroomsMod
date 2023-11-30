package org.vfast.backrooms.entities.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.vfast.backrooms.entities.SmilerEntity;
import org.vfast.backrooms.entities.client.model.SmilerModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@ClientOnly
public class SmilerRenderer extends GeoEntityRenderer<SmilerEntity> {
    public SmilerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SmilerModel());
    }

    @Override
    protected int getBlockLight(SmilerEntity allayEntity, BlockPos blockPos) {
        return 15;
    }
}
