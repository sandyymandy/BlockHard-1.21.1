package com.sandymandy.blockhard.entity.bia.client;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.client.AbstractGirlModel;
import com.sandymandy.blockhard.entity.bia.BiaEntity;
import net.minecraft.util.Identifier;

public class BiaModel extends AbstractGirlModel<BiaEntity> {

    @Override
    public Identifier getModelResource(BiaEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "geo/bia.geo.json");
    }

    @Override
    public Identifier getTextureResource(BiaEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "textures/entities/bia.png");
    }

    @Override
    public Identifier getAnimationResource(BiaEntity entity) {
        return Identifier.of(BlockHard.MOD_ID, "animations/bia.animation.json");
    }

}