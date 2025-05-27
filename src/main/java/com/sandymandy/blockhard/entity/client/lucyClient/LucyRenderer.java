package com.sandymandy.blockhard.entity.client.lucyClient;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEnitiy;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LucyRenderer extends GeoEntityRenderer<LucyEnitiy> {
    public LucyRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new LucyModel());
    }

    @Override
    public Identifier getTextureLocation(LucyEnitiy animatable){
        return Identifier.of(BlockHard.MOD_ID, "textures/entities/lucy.png");
    }

    @Override
    public void render(LucyEnitiy entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
