package com.sandymandy.blockhard.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LucyEnitiy extends TameableEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public Set<String> hiddenBones;
    public Set<String> shownBones;
    public boolean showHiddenBones = false;



    public LucyEnitiy(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.POPPY), false));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(6, new WanderAroundPointOfInterestGoal(this, 1.02D, false));


    }

    public static DefaultAttributeContainer.Builder createAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);

    }



    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"controller", 0, this::predicate).transitionLength(3));
    }

    private <LucyEnitiy extends GeoAnimatable> PlayState predicate(AnimationState<LucyEnitiy> lucyEnitiyAnimationState) {
        if (!this.isOnGround() && this.getVelocity().getY() < 0) {
            lucyEnitiyAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.fall", Animation.LoopType.LOOP));
            toggleModelBones("steve",false);
            return PlayState.CONTINUE;
        }

        if(lucyEnitiyAnimationState.isMoving()){
            lucyEnitiyAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.walk", Animation.LoopType.LOOP));
            toggleModelBones("steve",false);
            return PlayState.CONTINUE;
        }


        lucyEnitiyAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.idle", Animation.LoopType.LOOP));
        toggleModelBones("steve",false);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void toggleModelBones(String bones, boolean visible){
        String[] boneArray = bones.replaceAll("\\s+", "").split(",");

        if(this.hiddenBones == null){
            this.hiddenBones = new HashSet<>();
        }
        if (this.shownBones == null){
            this.shownBones = new HashSet<>();
        }

        List<String> boneList = Arrays.asList(boneArray);

        if(visible){
            this.hiddenBones.removeAll(boneList);
            this.shownBones.addAll(boneList);
            this.showHiddenBones = true;
        }
        else{
            this.shownBones.removeAll(boneList);
            this.hiddenBones.addAll(boneList);
            this.showHiddenBones = false;
        }
    }

//    private static final TrackedData<Boolean> SITTING =
//            DataTracker.registerData(LucyEnitiy.class, TrackedDataHandlerRegistry.BOOLEAN);
//    @Override
//    public ActionResult interactMob(PlayerEntity player, Hand hand) {
//        ItemStack itemstack = player.getStackInHand(hand);
//        Item item = itemstack.getItem();
//
//        Item itemForTaming = Items.APPLE;
//
//        if (item == itemForTaming && !isTamed()) {
//            if (this.getWorld().isClient()) {
//                return ActionResult.CONSUME;
//            } else {
//                if (!player.getAbilities().creativeMode) {
//                    itemstack.decrement(1);
//                }
//
//                if (!this.getWorld().isClient()) {
//                    super.setOwner(player);
//                    this.navigation.recalculatePath();
//                    this.setTarget(null);
//                    this.getWorld().sendEntityStatus(this, (byte)7);
//                    setSit(true);
//                }
//
//                return ActionResult.SUCCESS;
//            }
//        }
//
//        if(isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND) {
//            setSit(!isSitting());
//            return ActionResult.SUCCESS;
//        }
//
//        if (itemstack.getItem() == itemForTaming) {
//            return ActionResult.PASS;
//        }
//
//        return super.interactMob(player, hand);
//    }
//
//    public void setSit(boolean sitting) {
//        this.dataTracker.set(SITTING, sitting);
//        super.setSitting(sitting);
//    }
//
//    public boolean isSitting() {
//        return this.dataTracker.get(SITTING);
//    }
//
//    @Override
//    public void setTamed(boolean tamed) {
//        super.setTamed(tamed);
//        if (tamed) {
//            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(60.0D);
//            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4D);
//            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.5f);
//        } else {
//            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
//            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2D);
//            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.25f);
//        }
//    }
//
//    @Override
//    public void writeCustomDataToNbt(NbtCompound nbt) {
//        super.writeCustomDataToNbt(nbt);
//        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
//    }
//
//    @Override
//    public void readCustomDataFromNbt(NbtCompound nbt) {
//        super.readCustomDataFromNbt(nbt);
//        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
//    }
//
//    @Override
//    public AbstractTeam getScoreboardTeam() {
//        return super.getScoreboardTeam();
//    }
//
//    public boolean canBeLeashedBy(PlayerEntity player) {
//        return false;
//    }
//
//    @Override
//    protected void initDataTracker() {
//        super.initDataTracker();
//        this.dataTracker.startTracking(SITTING, false);
//    }

}
