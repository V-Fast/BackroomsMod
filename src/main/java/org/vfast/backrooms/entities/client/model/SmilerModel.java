package org.vfast.backrooms.entities.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entities.SmilerEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@Environment(EnvType.CLIENT)
public class SmilerModel extends DefaultedEntityGeoModel<SmilerEntity> {
    public SmilerModel() {
        super(new Identifier(BackroomsMod.MOD_ID, "smiler"), true);
    }
}
