package com.sandymandy.blockhard.entity.client.lucyClient;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEntity;
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
/*
    @Override
    protected Identifier getTextureOverrideForBone(GeoBone bone, LucyEntity animatable, float partialTick) {
        if (bone == null || animatable == null) return null;

        String name = bone.getName();
        if ("Torso2".equals(name) || "RightLeg".equals(name) || "RightArm".equals(name) || "LeftArm".equals(name) || "kneeR2".equals(name) || "shinR2".equals(name) || "bone13".equals(name) || "kneeR3".equals(name) || "shinL2".equals(name) || "bone14".equals(name) || "ballR".equals(name) || "ballL".equals(name) || "cock".equals(name) || "Head2".equals(name) || "bone148".equals(name) || "bone149".equals(name) || "bone150".equals(name) || "bone151".equals(name) || "shaft".equals(name) || "lSide5".equals(name) || "lSide6".equals(name) || "bottom2".equals(name) || "backSide2".equals(name)) {

            return Identifier.of(BlockHard.MOD_ID, "textures/player/steve.png");
        }

        return null;
    }*/

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

