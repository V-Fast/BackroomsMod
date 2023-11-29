package org.vfast.backrooms.entities.client;

import org.vfast.backrooms.BackroomsMod;
import org.vfast.backrooms.entities.BacteriaEntity;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@ClientOnly
public class BacteriaModel extends DefaultedEntityGeoModel<BacteriaEntity> {
    public BacteriaModel() {
        super(new Identifier(BackroomsMod.MOD_ID, "bacteria"), true);
    }
}
