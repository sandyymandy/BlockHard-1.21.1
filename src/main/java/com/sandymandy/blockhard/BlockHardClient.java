package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.client.lucyClient.LucyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.sandymandy.blockhard.screen.client.LucyScreen;

public class BlockHardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BlockHard.LUCY, LucyRenderer::new);
        HandledScreens.register(BlockHard.LUCY_SCREEN_HANDLER, LucyScreen::new);

    }
}
