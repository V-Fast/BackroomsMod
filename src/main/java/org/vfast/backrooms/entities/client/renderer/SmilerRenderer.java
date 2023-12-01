package org.vfast.backrooms.entities.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.BlockPos;
import org.vfast.backrooms.entities.SmilerEntity;
import org.vfast.backrooms.entities.client.model.SmilerModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SmilerRenderer extends GeoEntityRenderer<SmilerEntity> {
    public SmilerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SmilerModel());
    }

    @Override
    protected int getBlockLight(SmilerEntity allayEntity, BlockPos blockPos) {
        return 15;
    }
}
