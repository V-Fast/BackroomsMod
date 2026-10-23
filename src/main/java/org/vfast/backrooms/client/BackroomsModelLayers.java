package org.vfast.backrooms.client;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.client.render.entity.StillLifeModel;

public class BackroomsModelLayers {
    public static final ModelLayerLocation STILL_LIFE = create("still_life");
    public static final ModelLayerLocation STILL_LIFE_SLIM = create("still_life_slim");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(BackroomsMod.ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(STILL_LIFE, () -> StillLifeModel.createBodyLayer(false));
        ModelLayerRegistry.registerModelLayer(STILL_LIFE_SLIM, () -> StillLifeModel.createBodyLayer(true));
    }
}
