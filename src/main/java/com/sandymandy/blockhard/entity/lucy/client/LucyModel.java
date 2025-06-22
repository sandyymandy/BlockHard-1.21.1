package com.sandymandy.blockhard.entity.lucy.client;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.client.AbstractGirlModel;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import net.minecraft.util.Identifier;

public class LucyModel extends AbstractGirlModel<LucyEntity> {

    @Override
    public Identifier getModelResource(LucyEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "geo/lucy.geo.json");
    }

    @Override
    public Identifier getTextureResource(LucyEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "textures/entities/lucy.png");
    }

    @Override
    public Identifier getAnimationResource(LucyEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "animations/lucy.animation.json");
    }

}