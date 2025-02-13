package org.vfast.backrooms.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.SimpleOption;

@Environment(EnvType.CLIENT)
public interface GameOptionsAccessor {
    SimpleOption<Boolean> getDefaultPanorama();
}
