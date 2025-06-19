package com.sandymandy.blockhard.entity;

import com.sandymandy.blockhard.entity.lucy.screen.LucyScreenHandlerFactory;
import com.sandymandy.blockhard.scene.SceneEntity;
import com.sandymandy.blockhard.util.GlobleMessages;
import com.sandymandy.blockhard.util.inventory.GirlInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractGirlEntity extends TameableEntity implements GeoEntity, SceneEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public Set<String> hiddenBones;
    public Set<String> shownBones;
    public boolean showHiddenBones = false;
    public final int maxRelationshipLevel = 3;
    public int currentRelationshipLevel;
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final GirlInventory inventory = GirlInventory.ofSize();
    private BlockPos basePos;
    private LivingEntity attackTarget;
    private int ticksSinceLastHit;
    private static final int MAX_TICKS_NO_HIT = 20 * 20;
    public float previousYaw = 0;
    public Vec3d previousVelocity = Vec3d.ZERO;
    protected Item getTameItem() {
        return Items.DANDELION;
    }

    protected String getGirlName() {
        return "Null";
    }

    protected AbstractGirlEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void openThisHandledScreen(PlayerEntity player){
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);

    }


    public GirlInventory getInventory() {
        return inventory;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SitGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TameableEscapeDangerGoal(1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.add(5, new TemptGoal(this, 1.25D, Ingredient.ofItems(getTameItem()), false));
        this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this));


    }



    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item itemInHand = itemStack.getItem();


        if (!this.getWorld().isClient && !this.isTamed() && itemInHand.equals(getTameItem())) {
            itemStack.decrementUnlessCreative(1, player);
            this.tryTame(player);
            return ActionResult.SUCCESS;
        }

        if(!this.getWorld().isClient && this.isTamed() &! isOwner(player) && (hand.equals(Hand.MAIN_HAND) || itemInHand.equals(getTameItem()))){
            player.sendMessage(Text.literal("She's Already In A Relationship With Someone" ), true);
            return ActionResult.FAIL;
        }

        if (!this.getWorld().isClient && isOwner(player) && this.isTamed() && hand.equals(Hand.MAIN_HAND)) {
            if (!player.isSneaking()) {
                openThisHandledScreen(player);
                this.getNavigation().stop();
                this.setVelocity(0, 0, 0);
                this.setTarget(null);
                return ActionResult.SUCCESS;
            }else{
                this.setSit(!isSittingdown());
                return ActionResult.SUCCESS;
            }
        }

        if (!this.getWorld().isClient && isOwner(player) && this.isTamed() && itemInHand.equals(Items.COAL)) {
            this.disown(player);
            return ActionResult.SUCCESS;
        }

        if (itemInHand.equals(getTameItem()) && this.isTamed()) {
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
            player.sendMessage(Text.literal("You Asked " + getGirlName() + " Out And She Said §aYes" ), true);
            this.setBasePosHere();
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
            player.sendMessage(Text.literal("You Asked " + getGirlName() + " Out And She Said §cNo"), true);
        }
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        this.setTarget(null);
        this.calculateDimensions();
        super.setSitting(sitting);
    }

    public boolean isSittingdown() {
        return this.dataTracker.get(SITTING);

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
    public boolean shouldRenderName() {
        this.setCustomName(Text.of(getGirlName()));
        this.setCustomNameVisible(true);
        return true;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory(); // calls standard drop logic

        for (ItemStack stack : this.getInventory().getItems()) {
            if (!stack.isEmpty()) {
                this.dropStack(stack);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;

        String damageType = source.getName();
        // If killed by /kill or void, allow normal death
        if (damageType.equals("outOfWorld") || damageType.equals("genericKill")) {
            return super.damage(source, amount);
        }

        if(this.isTamed() && (this.getHealth() - amount <= 0.0F) &! (damageType.equals("outOfWorld") || damageType.equals("genericKill"))) {
            this.setHealth(getMaxHealth());
            // If basePos is still null, fall back to current position
            BlockPos respawnPos = (this.basePos != null)
                    ? this.basePos
                    : this.getBlockPos();

            // Send a message referencing whichever Pos we have
            new GlobleMessages().GlobleMessage(
                    this.getWorld(),
                    getGirlName() + " died and respawned at base: " +
                            respawnPos.getX() + ", " +
                            respawnPos.getY() + ", " +
                            respawnPos.getZ()
            );

            // If basePos was null, make sure to set it now so future hits won’t NPE
            if (this.basePos == null) {
                this.basePos = respawnPos;
            }
            teleportToBase();

            return false;
        }else{
            return super.damage(source, amount);
        }
    }

    public void disown(PlayerEntity player) {
        if(!player.getWorld().isClient){
            this.setTamed(false,true); // Mark the entity as untamed
            this.setOwnerUuid(null); // Remove the owner UUID
            this.setSit(false); // Ensure the entity is not sitting
            if(!isTamed() && !isOwner(player)){
                player.sendMessage(Text.literal("§cYou Broke Up With " + getGirlName()), true);
            }
        }
    }

    public void setBasePosHere(){
        this.basePos = this.getBlockPos();
    }

    public void setBasePos(BlockPos pos) {
        this.basePos = pos;
    }

    public BlockPos getBasePos(){
        return basePos;
    }

    public void teleportToBase() {
        if (this.basePos != null && this.getWorld() != null) {
            setSit(true);
            this.teleport(this.basePos.getX(), this.basePos.getY(), this.basePos.getZ(), false);
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (attackTarget != null) {
            ticksSinceLastHit++;

            if (ticksSinceLastHit >= MAX_TICKS_NO_HIT) {
                // Lost interest — stop attacking
                this.setTarget(null);
                attackTarget = null;
                ticksSinceLastHit = 0;
            }
        }
    }

    public void tick() {
        // Call this each tick in entity
        super.tick();
        previousYaw = getYaw();
        previousVelocity = getVelocity();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);

        if (target != null) {
            attackTarget = target;
            ticksSinceLastHit = 0; // reset countdown on new target
        } else {
            attackTarget = null;
            ticksSinceLastHit = 0;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);

        if (success && target == attackTarget) {
            ticksSinceLastHit = 0; // reset timer on successful hit
        }

        return success;
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

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.writeNbt(nbt, this.inventory.getItems(), registryLookup);
        nbt.putBoolean("Sitting", this.isSittingdown());
        if (this.basePos != null) {
            nbt.putInt("BaseX", this.basePos.getX());
            nbt.putInt("BaseY", this.basePos.getY());
            nbt.putInt("BaseZ", this.basePos.getZ());}
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.readNbt(nbt, this.inventory.getItems(), registryLookup);
        this.setSit(nbt.getBoolean("Sitting"));
        if (nbt.contains("BaseX") && nbt.contains("BaseY") && nbt.contains("BaseZ")) {
            int x = nbt.getInt("BaseX");
            int y = nbt.getInt("BaseY");
            int z = nbt.getInt("BaseZ");
            this.basePos = new BlockPos(x, y, z);}

    }

    @Override
    public void startScene(PlayerEntity player) {
        player.startRiding(this, true);
    }

    @Override
    public void stopScene() {
        for (Entity passenger : getPassengerList()) {
            passenger.stopRiding();
        }
    }

    @Override
    public void updateMountedOffset(PlayerEntity player) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
