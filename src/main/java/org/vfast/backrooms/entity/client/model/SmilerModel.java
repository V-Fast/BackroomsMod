package org.vfast.backrooms.entity.client.model;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entity.SmilerEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@ClientOnly
public class SmilerModel extends DefaultedEntityGeoModel<SmilerEntity> {
    public SmilerModel() {
        super(new Identifier(BackroomsMod.ID, "smiler"), true);
    }
}
