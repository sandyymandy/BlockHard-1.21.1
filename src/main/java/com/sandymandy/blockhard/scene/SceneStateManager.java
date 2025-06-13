package com.sandymandy.blockhard.scene;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class SceneStateManager {
    private static final HashMap<UUID, SceneState> activeScenes = new HashMap<>();

    public static void startScene(PlayerEntity player, SceneEntity entity) {
        if (isSceneRunning(player)) return;

        activeScenes.put(player.getUuid(), new SceneState(entity));
        entity.startScene(player);
    }

    public static void stopScene(PlayerEntity player) {
        SceneState state = activeScenes.remove(player.getUuid());
        if (state != null) {
            state.entity().stopScene();
        }
    }

    public static boolean isSceneRunning(PlayerEntity player) {
        return activeScenes.containsKey(player.getUuid());
    }

    public static SceneState getSceneState(PlayerEntity player) {
        return activeScenes.get(player.getUuid());
    }

    public record SceneState(SceneEntity entity) {}
}
