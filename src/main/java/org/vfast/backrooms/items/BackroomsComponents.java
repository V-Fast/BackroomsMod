package org.vfast.backrooms.items;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.vfast.backrooms.BackroomsMod;

public class BackroomsComponents {
    public static final DataComponentType<Boolean> VHS_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(BackroomsMod.ID, "vhs_effect"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static void registerComponents() {
        BackroomsMod.LOGGER.info("[BackroomsMod] Initialized components");
    }
}
