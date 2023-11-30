package org.vfast.backrooms.entities.client.model;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entities.SmilerEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@ClientOnly
public class SmilerModel extends DefaultedEntityGeoModel<SmilerEntity> {
    public SmilerModel() {
        super(new Identifier(BackroomsMod.MOD_ID, "smiler"), true);
    }
}
