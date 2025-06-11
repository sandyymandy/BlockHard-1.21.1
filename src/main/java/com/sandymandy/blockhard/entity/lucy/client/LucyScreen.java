package com.sandymandy.blockhard.entity.lucy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import com.sandymandy.blockhard.entity.lucy.LucyScreenHandler;
import com.sandymandy.blockhard.entity.lucy.client.screen.LucyButtonAction;
import com.sandymandy.blockhard.entity.lucy.client.screen.LucyButtonRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LucyScreen extends HandledScreen<LucyScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(BlockHard.MOD_ID, "/textures/gui/inventory.png");
    private float xMouse;
    private float yMouse;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 170;
    private final LucyEntity lucy;



    public LucyScreen(LucyScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
            this.lucy = handler.getLucy();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context,mouseX,mouseY);

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        //Stops the container names from rendering
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

        int startX = centerX - 90;
        int startY = centerY + 15;
        int buttonHeight = 22;
        int buttonWidth = 80;

        for (int i = 0; i < LucyButtonRegistry.BUTTONS.size(); i++) {
            LucyButtonAction action = LucyButtonRegistry.BUTTONS.get(i);
            int y = startY + i * (buttonHeight + 4);

            this.addDrawableChild(ButtonWidget.builder(
                    action.label(),
                    btn -> {
                        if (lucy != null && client != null && client.player != null) {
                            action.action().accept(lucy, client.player);  // Run the button's logic
                            this.client.setScreen(null);
                        }
                    }
            ).dimensions(startX, y, buttonWidth, buttonHeight).build());
        }

    }

}
