package com.sandymandy.blockhard.scene;

import net.minecraft.entity.player.PlayerEntity;

public interface SceneEntity {
    void startScene(PlayerEntity player);
    void stopScene();
    void updateMountedOffset(PlayerEntity player);
}
