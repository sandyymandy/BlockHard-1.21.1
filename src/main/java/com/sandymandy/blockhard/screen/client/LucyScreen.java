package com.sandymandy.blockhard.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEntity;
import com.sandymandy.blockhard.screen.LucyScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

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

        renderLucyEntity(context, (width - GUI_WIDTH) / 2 + 50, (width - GUI_WIDTH) / 2 + 75, mouseX, mouseY);
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

        int buttonX = centerX - 70;
        int buttonY = centerY + 10;

        // Example button
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Break Up"),
                btn -> {
                    System.out.println("Break Up pressed!");
                    assert this.client != null;
                    this.client.setScreen(null); // 👈 Close the GUI
                }
        ).dimensions(buttonX, buttonY, 60, 20).build());

    }

    private void renderLucyEntity(DrawContext context, int x, int y, int mouseX, int mouseY) {
        if (lucy == null) return;

        float scale = 30.0F;
        float yaw = (float) Math.atan((x - mouseX) / 40.0F) * 20.0F;
        float pitch = (float) Math.atan((y - mouseY) / 40.0F) * 20.0F;

        // Backup original rotation values
        float bodyYaw = lucy.bodyYaw;
        float headYaw = lucy.headYaw;
        float prevYaw = lucy.prevYaw;
        float yawOld = lucy.getYaw();
        float pitchOld = lucy.getPitch();

        // Set temporary rotation values for rendering
        lucy.bodyYaw = 180.0F + yaw;
        lucy.setYaw(180.0F + yaw * 2.0F);
        lucy.setPitch(-pitch);
        lucy.headYaw = lucy.getYaw();

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.translate(x, y, 100.0); // position in GUI
        matrices.scale(-scale, scale, scale); // flip and scale
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F)); // face forward

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(lucy, 0, 0, 0, 0.0F, 1.0F, matrices, context.getVertexConsumers(), 0x00F000F0);
        context.getVertexConsumers().draw();
        dispatcher.setRenderShadows(true);

        matrices.pop();

        // Restore original rotation values
        lucy.bodyYaw = bodyYaw;
        lucy.headYaw = headYaw;
        lucy.prevYaw = prevYaw;
        lucy.setYaw(yawOld);
        lucy.setPitch(pitchOld);
    }




}
