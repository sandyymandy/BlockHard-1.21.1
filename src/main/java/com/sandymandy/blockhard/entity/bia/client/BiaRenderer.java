package com.sandymandy.blockhard.entity.bia.client;

import com.sandymandy.blockhard.util.entity.client.AbstractGirlRenderer;
import com.sandymandy.blockhard.entity.bia.BiaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BiaRenderer extends AbstractGirlRenderer<BiaEntity> {
    public BiaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BiaModel());
    }
}
