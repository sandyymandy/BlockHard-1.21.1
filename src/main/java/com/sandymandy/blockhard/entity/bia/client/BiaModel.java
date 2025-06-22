package com.sandymandy.blockhard.entity.bia.client;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.client.AbstractGirlModel;
import com.sandymandy.blockhard.entity.bia.BiaEntity;
import com.sandymandy.blockhard.util.jigglePhysics.JiggleBoneConfig;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected List<JiggleBoneConfig> JIGGLE_BONES(BiaEntity bia) {
        List<JiggleBoneConfig> bones = new ArrayList<>(super.JIGGLE_BONES(bia));

        if(bia.isStripped()) {
            bones.add(new JiggleBoneConfig("bra", 0.2, 0.3));
        }
        return bones;
    }
}