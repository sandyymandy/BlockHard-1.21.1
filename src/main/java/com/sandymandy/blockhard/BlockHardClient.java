package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.lucy.LucyInit;
import com.sandymandy.blockhard.entity.lucy.client.LucyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.sandymandy.blockhard.entity.lucy.client.screen.LucyScreen;

public class BlockHardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(LucyInit.LUCY, LucyRenderer::new);
        HandledScreens.register(LucyInit.LUCY_SCREEN_HANDLER, LucyScreen::new);

    }
}
