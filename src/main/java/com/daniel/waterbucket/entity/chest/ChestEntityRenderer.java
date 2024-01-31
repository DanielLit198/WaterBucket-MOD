package com.daniel.waterbucket.entity.chest;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChestEntityRenderer extends GeoEntityRenderer<ChestEntity> {
    public ChestEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx,new ChestEntityModel());
    }

}
