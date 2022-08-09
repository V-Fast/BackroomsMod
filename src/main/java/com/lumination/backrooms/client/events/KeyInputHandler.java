package com.lumination.backrooms.client.events;

import com.lumination.backrooms.BackroomsMod;
import com.lumination.backrooms.client.screens.LevelScreen;
import com.lumination.backrooms.utils.Backrooms;
import com.lumination.backrooms.utils.extensions.ConvertRooms;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static final ConvertRooms cr = new ConvertRooms();
    public static final String KEY_CATEGORY = "mod.backrooms.name";
    public static final String KEY_LEVEL_INFO = "key.backrooms.level_info";

    public static KeyBinding levelInfoKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (levelInfoKey.wasPressed()) {
                getBackroom(client.player);
                client.setScreen(new LevelScreen(cr.convert(Backrooms.OVERWORLD)));
            }
        });
    }

    public static void register() {
        levelInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LEVEL_INFO,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY
        ));

        registerKeyInputs();
    }

    public static final DimensionType getBackroom(ClientPlayerEntity player) {
        DimensionType dim = player.getWorld().getDimension();
        BackroomsMod.print(dim.toString());
        return dim;
    }
}
