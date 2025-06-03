package com.sandymandy.blockhard.entity.client.lucyClient;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LucyModel extends GeoModel<LucyEntity> {
    @Override
    public Identifier getModelResource(LucyEntity lucyEnitiy) {
        return Identifier.of(BlockHard.MOD_ID,"geo/lucy.geo.json");
    }

    @Override
    public Identifier getTextureResource(LucyEntity lucyEnitiy) {
        return Identifier.of(BlockHard.MOD_ID, "textures/entities/lucy.png");
    }

    @Override
    public Identifier getAnimationResource(LucyEntity lucyEnitiy) {
        return Identifier.of(BlockHard.MOD_ID,"animations/lucy.animation.json");
    }

    @Override
    public void setCustomAnimations(LucyEntity lucy, long instanceId, AnimationState<LucyEntity> animationState){

        GeoBone head = getAnimationProcessor().getBone("head");

        if(head != null){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            assert entityData != null;
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);

        }
    }
}
