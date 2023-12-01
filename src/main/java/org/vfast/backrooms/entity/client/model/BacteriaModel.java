package org.vfast.backrooms.entity.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entity.BacteriaEntity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class BacteriaModel extends DefaultedEntityGeoModel<BacteriaEntity> {
    public BacteriaModel() {
        super(new Identifier(BackroomsMod.ID, "bacteria"), true);
    }
}
