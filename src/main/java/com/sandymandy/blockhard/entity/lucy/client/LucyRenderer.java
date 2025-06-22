package com.sandymandy.blockhard.entity.lucy.client;

import com.sandymandy.blockhard.util.entity.client.AbstractGirlRenderer;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class LucyRenderer extends AbstractGirlRenderer<LucyEntity> {
    public LucyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LucyModel());
    }
}