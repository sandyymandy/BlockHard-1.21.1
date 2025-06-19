package com.sandymandy.blockhard.entity.lucy.client;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoEntityRenderer;

public class LucyRenderer extends DynamicGeoEntityRenderer<LucyEntity> {
    public LucyRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new LucyModel());
    }
    @Override
    public Identifier getTextureLocation(LucyEntity animatable){
        return Identifier.of(BlockHard.MOD_ID, "textures/entities/lucy.png");
    }


    @Override
    public void render(LucyEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, LucyEntity entity, BakedGeoModel model,
                          VertexConsumerProvider bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, int colour) {

        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, colour);

        // Always show bones in shownBones
        if (entity.shownBones != null) {
            for (String boneName : entity.shownBones) {
                model.getBone(boneName).ifPresent(bone -> {
                    bone.setHidden(false);
                    bone.setChildrenHidden(false);
                });
            }
        }

        // Show or hide bones in hiddenBones based on flag
        if (entity.hiddenBones != null) {
            for (String boneName : entity.hiddenBones) {
                model.getBone(boneName).ifPresent(bone -> {
                    boolean visible = entity.showHiddenBones; // boolean flag on entity
                    bone.setHidden(!visible);
                    bone.setChildrenHidden(!visible);
                });
            }
        }
    }






}

