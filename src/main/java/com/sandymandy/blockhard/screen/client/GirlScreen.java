package com.sandymandy.blockhard.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import com.sandymandy.blockhard.screen.GirlScreenHandler;
import com.sandymandy.blockhard.util.network.ButtonAction;
import com.sandymandy.blockhard.util.network.ButtonRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GirlScreen extends HandledScreen<GirlScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(BlockHard.MOD_ID, "/textures/gui/inventory.png");
    private float xMouse;
    private float yMouse;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 170;
    private final AbstractGirlEntity girl;
    private final PlayerEntity player;



    public GirlScreen(GirlScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.girl = handler.getGirl();
        this.player = inventory.player;
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

        int buttonHeight = 22;
        int buttonWidth = 80;

        int startX = centerX - (buttonWidth + 10);
        int startY = centerY + 15;

        for (int i = 0; i < ButtonRegistry.BUTTONS_LEFT.size(); i++) {
            ButtonAction action = ButtonRegistry.BUTTONS_LEFT.get(i);
            int y = startY + i * (buttonHeight + 4);
            Text dynamicLabel = action.label();

            if (action.label().getString().equals("Strip") && girl.isStripped()) {
                dynamicLabel = Text.literal("Dress Up");
            }

            this.addDrawableChild(ButtonWidget.builder(
                    dynamicLabel,
                    btn -> {
                        if (girl != null && client != null && player != null) {
                            action.action().accept(girl, player);  // Run the button's logic
                            this.client.setScreen(null);
                        }
                    }
            ).dimensions(startX, y, buttonWidth, buttonHeight).build());
        }

        for (int i = 0; i < ButtonRegistry.BUTTONS_RIGHT.size(); i++) {
            ButtonAction action = ButtonRegistry.BUTTONS_RIGHT.get(i);
            int y = startY + i * (buttonHeight + 4);
            Text dynamicLabel = action.label();

            if (action.label().getString().equals("Sit") && girl.isSittingdown()){
                dynamicLabel = Text.literal("Stand");
            }
            else if (action.label().getString().equals("Follow Me") && girl.isFollowing()){
                dynamicLabel = Text.literal("Stop Following");
            }

            this.addDrawableChild(ButtonWidget.builder(
                    dynamicLabel,
                    btn -> {
                        if (girl != null && client != null && player != null) {
                            action.action().accept(girl, player);  // Run the button's logic
                            this.client.setScreen(null);
                        }
                    }
            ).dimensions(centerX + 176 + 10, y, buttonWidth, buttonHeight).build());
        }

    }

}
