package com.sandymandy.blockhard.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.screen.LucyScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LucyScreen extends HandledScreen<LucyScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(BlockHard.MOD_ID,"textures/gui/girlinventory.png");

    private static final int BACKGROUND_WIDTH  = 176;
    private static final int BACKGROUND_HEIGHT = 170;

    public LucyScreen(LucyScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth  = BACKGROUND_WIDTH;
        this.backgroundHeight = BACKGROUND_HEIGHT;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
