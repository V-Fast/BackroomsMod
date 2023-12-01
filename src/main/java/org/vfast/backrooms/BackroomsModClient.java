package org.vfast.backrooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import org.vfast.backrooms.client.hud.CameraRecordHud;
import org.vfast.backrooms.entity.BackroomsEntities;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.vfast.backrooms.client.hud.SanityHudOverlay;
import org.vfast.backrooms.config.BackroomsConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BackroomsModClient implements ClientModInitializer {
    public static final CameraRecordHud cameraHud = new CameraRecordHud();
    public static final SanityHudOverlay sanityHud = new SanityHudOverlay();

    public static KeyBinding configKeybind;

    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(BackroomsMod.ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(BackroomsMod.ID, "old_ssc"), modContainer, ResourcePackActivationType.NORMAL);
        });
        BackroomsEntities.registerRenderers();
        registerHuds();
        configKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.backrooms.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyBinding.MISC_CATEGORY
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeybind.wasPressed()) {
                client.setScreen(BackroomsConfig.HANDLER.instance().getScreen(client.currentScreen));
            }
        });
        BackroomsMod.LOGGER.info("Initialized Client Backrooms");
    }

    public void registerHuds() {
        HudRenderCallback.EVENT.register(cameraHud);
        SanityHudOverlay.EVENT.register(sanityHud);
    }
}