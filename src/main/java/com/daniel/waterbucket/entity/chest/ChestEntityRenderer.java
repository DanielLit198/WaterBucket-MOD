package com.daniel.waterbucket.entity.chest;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ChestEntityRenderer extends GeoEntityRenderer<ChestEntity> {
    public ChestEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx,new ChestEntityModel());
    }

}
