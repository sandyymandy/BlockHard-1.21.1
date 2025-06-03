package com.sandymandy.blockhard.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEntity;
import com.sandymandy.blockhard.screen.LucyScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LucyScreen extends HandledScreen<LucyScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(BlockHard.MOD_ID,"textures/gui/girlinventory.png");
    private float xMouse;
    private float yMouse;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 170;


    public LucyScreen(LucyScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context,mouseX,mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0,TEXTURE);

        int centerX = (width - GUI_WIDTH) / 2;
        int centerY = (height - GUI_HEIGHT) / 2;

        // Draw the GUI background texture
        context.drawTexture(TEXTURE, centerX, centerY, 0, 0, GUI_WIDTH, GUI_HEIGHT,GUI_WIDTH, GUI_HEIGHT);

    }


    @Override
    protected void init() {
        super.init();
        int centerX = (this.width - GUI_WIDTH) / 2;
        int centerY = (this.height - GUI_HEIGHT) / 2;

//        // Example button
//        this.addDrawableChild(ButtonWidget.builder(
//                Text.literal("Sit"),
//                button -> System.out.println("Dance pressed!")
//        ).dimensions(centerX + 10, centerY + 150, 60, 20).build());

        // Move player inventory title down where player slots are
        playerInventoryTitleX = 8;
        playerInventoryTitleY = backgroundHeight - 94; // standard
    }

}
