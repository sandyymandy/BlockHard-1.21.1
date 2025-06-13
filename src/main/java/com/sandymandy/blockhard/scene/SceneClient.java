package com.sandymandy.blockhard.scene;

import com.sandymandy.blockhard.scene.SceneStateManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class SceneClient implements ClientModInitializer {
    private static final KeyBinding EXIT_SCENE_KEY = new KeyBinding(
            "key.blockhard.exit_scene",
            GLFW.GLFW_KEY_X,
            "key.categories.gameplay"
    );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (EXIT_SCENE_KEY.wasPressed()) {
                if (client.player != null && SceneStateManager.isSceneRunning(client.player)) {
                    SceneStateManager.stopScene(client.player);
                }
            }

            // Prevent dismount key (Shift) from working
            if (client.player != null &&
                    SceneStateManager.isSceneRunning(client.player)) {
                client.options.sneakKey.setPressed(false);  // disables shift dismount
            }
        });
    }
}
