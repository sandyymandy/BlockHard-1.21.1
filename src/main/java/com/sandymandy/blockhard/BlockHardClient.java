package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.EntityInit;
import com.sandymandy.blockhard.entity.bia.client.BiaRenderer;
import com.sandymandy.blockhard.entity.lucy.client.LucyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.sandymandy.blockhard.screen.client.GirlScreen;


public class BlockHardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(BlockHard.GIRL_SCREEN_HANDLER, GirlScreen::new);

        EntityRendererRegistry.register(EntityInit.LUCY, LucyRenderer::new);
        EntityRendererRegistry.register(EntityInit.BIA, BiaRenderer::new);
    }
}
