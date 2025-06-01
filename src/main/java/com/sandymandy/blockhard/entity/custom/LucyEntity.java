package com.sandymandy.blockhard.entity.custom;


import com.sandymandy.blockhard.util.inventory.GirlInventory;
import com.sandymandy.blockhard.screen.LucyScreenHandler;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.*;
import java.util.*;

public class LucyEntity extends TameableEntity implements GeoEntity, NamedScreenHandlerFactory, GirlInventory {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public Set<String> hiddenBones;
    public Set<String> shownBones;
    public boolean showHiddenBones = false;
    private final Item tamedWith = Items.ALLIUM;
    public final int  maxRelationshipLevel = 3;
    public int currentRelationshipLevel;
    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(LucyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(TOTAL_SLOTS,ItemStack.EMPTY);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
    }

    public LucyEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new LucyScreenHandler(syncId,playerInventory,this);
    }


    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.writeNbt(nbt, this.inventory, registryLookup);

        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item itemInHand = itemStack.getItem();


        if (!this.getWorld().isClient && !this.isTamed() && itemInHand.equals(tamedWith)) {
            itemStack.decrementUnlessCreative(1,player);
            this.tryTame(player);
            return ActionResult.SUCCESS;
        }

        if(!this.getWorld().isClient && isOwner(player) && this.isTamed() && hand.equals(Hand.MAIN_HAND)) {
            if(player.isSneaking()) {
                player.openHandledScreen(this);
                return ActionResult.SUCCESS;
            }
            else {
                setSit(!isSitting());
                return ActionResult.SUCCESS;
            }
        }

        if(itemInHand.equals(tamedWith) && this.isTamed()){
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    private void tryTame(PlayerEntity player) {
        if (this.random.nextInt(3) == 0) {
            this.setOwner(player);
            this.navigation.stop();
            setTarget(null);
            setSit(true);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
        }
    }

    private void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSittingdown() {
        return this.dataTracker.get(SITTING);

    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
//        this.goalSelector.add(1, new TameableEntity.TameableEscapeDangerGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new EscapeDangerGoal(this, 1.2));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.add(5, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.ALLIUM), false));
        this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
//                .add(EntityAttributes.GENERIC_ARMOR, armor);

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
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate).transitionLength(3)); // sets the transition length to the next animation as 3 in game ticks
    }

    private <lucyentity extends GeoAnimatable> PlayState predicate(AnimationState<lucyentity> lucyEntityAnimationState) {
        if ((!this.isOnGround() && this.getVelocity().getY() < 0) &! isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.fall", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }

        if (lucyEntityAnimationState.isMoving() &! isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.walk", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }

        if(isSittingdown()) {
            lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.sit", Animation.LoopType.LOOP));
            toggleModelBones("steve", false);
            return PlayState.CONTINUE;
        }

        lucyEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lucy.idle", Animation.LoopType.LOOP));
        toggleModelBones("steve", false);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void toggleModelBones(String bones, boolean visible) {
        String[] boneArray = bones.replaceAll("\\s+", "").split(",");

        if (this.hiddenBones == null) {
            this.hiddenBones = new HashSet<>();
        }
        if (this.shownBones == null) {
            this.shownBones = new HashSet<>();
        }

        List<String> boneList = Arrays.asList(boneArray);

        if (visible) {
            boneList.forEach(this.hiddenBones::remove);
            this.shownBones.addAll(boneList);
            this.showHiddenBones = true;
        } else {
            boneList.forEach(this.shownBones::remove);
            this.hiddenBones.addAll(boneList);
            this.showHiddenBones = false;
        }

    }


}
