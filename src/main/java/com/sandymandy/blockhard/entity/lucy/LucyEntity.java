package com.sandymandy.blockhard.entity.lucy;

import com.sandymandy.blockhard.entity.AbstractGirlEntity;
import com.sandymandy.blockhard.entity.lucy.screen.LucyScreenHandlerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.*;

public class LucyEntity extends AbstractGirlEntity{

    protected LucyEntity(EntityType<? extends AbstractGirlEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getTameItem() {
        return Items.ALLIUM;
    }

    @Override
    protected String getGirlName() {
        return "Lucy";
    }

    @Override
    protected void openThisHandledScreen(PlayerEntity player){
        player.openHandledScreen(new LucyScreenHandlerFactory(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
//                .add(EntityAttributes.GENERIC_ARMOR, armor);

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate).transitionLength(3)); // sets the transition length to the next animation as 3 in game ticks
    }

    private <lucyentity extends GeoAnimatable> PlayState predicate(AnimationState<lucyentity> lucyEntityAnimationState) {
        if (!this.isOnGround() &! isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.fly", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }

        if (lucyEntityAnimationState.isMoving() &! isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.walk", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }

        if (isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.sit", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }


        lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.idle", Animation.LoopType.LOOP));
        toggleModelBones("steve", false);
        return PlayState.CONTINUE;
    }

}