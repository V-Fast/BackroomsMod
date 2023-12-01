package org.vfast.backrooms.entity.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entity.SmilerEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class SmilerModel extends DefaultedEntityGeoModel<SmilerEntity> {
    public SmilerModel() {
        super(new Identifier(BackroomsMod.ID, "smiler"), true);
    }
}
