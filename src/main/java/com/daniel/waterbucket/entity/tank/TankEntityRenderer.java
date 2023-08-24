package com.daniel.waterbucket.entity.tank;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TankEntityRenderer extends GeoEntityRenderer<TankEntity> {
    public TankEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new TankEntityModel());
    }
}
