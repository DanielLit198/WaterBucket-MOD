package com.daniel.waterbucket.entity.waterball;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MeteoriteRenderer extends GeoEntityRenderer<MeteoriteEntity> {

    public MeteoriteRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MeteoriteModel());
        this.shadowOpacity = 0.4f;
    }

}
