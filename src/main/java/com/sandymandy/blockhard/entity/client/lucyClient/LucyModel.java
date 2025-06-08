package com.sandymandy.blockhard.entity.client.lucyClient;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEntity;
import com.sandymandy.blockhard.util.JigglePhysics;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class LucyModel extends GeoModel<LucyEntity> {
    private final Map<Long, Map<String, JigglePhysics>> jiggleMapByEntity = new HashMap<>();
    private final Map<Long, Map<String, Vec3d>> defaultRotationsByEntity = new HashMap<>();



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
    public void setCustomAnimations(LucyEntity lucy, long instanceId, AnimationState<LucyEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        float currentYaw = lucy.getYaw();
        float yawDelta = currentYaw - lucy.previousYaw;
        if (yawDelta > 180) yawDelta -= 360;
        if (yawDelta < -180) yawDelta += 360;

        Vec3d velocity = lucy.getVelocity();
        Vec3d deltaVelocity = velocity.subtract(lucy.previousVelocity);

        double yawInfluenceX = Math.sin(Math.toRadians(currentYaw)) * yawDelta * 0.05;
        double yawInfluenceZ = Math.cos(Math.toRadians(currentYaw)) * yawDelta * 0.05;

        Vec3d inertiaForce = deltaVelocity.multiply(1.2).add(new Vec3d(yawInfluenceX, 0, yawInfluenceZ));

        GeoBone leftBoob = getAnimationProcessor().getBone("boobL");
        GeoBone rightBoob = getAnimationProcessor().getBone("boobR");
//        GeoBone boobs = getAnimationProcessor().getBone("boobs");


        if (leftBoob == null || rightBoob == null) return;

        // Initialize maps per-entity
        jiggleMapByEntity.putIfAbsent(instanceId, new HashMap<>());
        defaultRotationsByEntity.putIfAbsent(instanceId, new HashMap<>());

        Map<String, JigglePhysics> jiggleMap = jiggleMapByEntity.get(instanceId);
        Map<String, Vec3d> defaultRotations = defaultRotationsByEntity.get(instanceId);


        // Save default rotations (only once)
        defaultRotations.putIfAbsent("boobL", new Vec3d(leftBoob.getRotX(), leftBoob.getRotY(), leftBoob.getRotZ()));
        defaultRotations.putIfAbsent("boobR", new Vec3d(rightBoob.getRotX(), rightBoob.getRotY(), rightBoob.getRotZ()));
//        defaultRotations.putIfAbsent("boobs", new Vec3d(boobs.getRotX(), boobs.getRotY(), boobs.getRotZ()));


        jiggleMap.putIfAbsent("boobL", new JigglePhysics(0.2, 0.1));
        jiggleMap.putIfAbsent("boobR", new JigglePhysics(0.2, 0.1));
//        jiggleMap.putIfAbsent("boobs", new JigglePhysics(0.2, 0.1));


        // Apply jiggle
        applyJiggle("boobL", leftBoob, inertiaForce, jiggleMap, defaultRotations);
        applyJiggle("boobR", rightBoob, inertiaForce, jiggleMap, defaultRotations);
//        applyJiggle("boobs", boobs, inertiaForce, jiggleMap, defaultRotations);


        // Set head rotation
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
            }
        }
    }

    private void applyJiggle(String boneKey, GeoBone bone, Vec3d motion,
                             Map<String, JigglePhysics> jMap,
                             Map<String, Vec3d> dMap) {
        JigglePhysics jiggle = jMap.get(boneKey);
        Vec3d defaultRot = dMap.get(boneKey);
        if (jiggle == null || defaultRot == null) return;

        jiggle.update(motion);
        Vec3d offset = jiggle.getDisplacement();

        bone.setRotX((float) (defaultRot.x + offset.x));
        bone.setRotY((float) (defaultRot.y + offset.y));
        bone.setRotZ((float) (defaultRot.z + offset.z));
    }

}
