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



}