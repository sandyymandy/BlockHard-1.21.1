package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.client.lucyClient.LucyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class BlockHardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BlockHard.LUCY, LucyRenderer::new);

    }
}
