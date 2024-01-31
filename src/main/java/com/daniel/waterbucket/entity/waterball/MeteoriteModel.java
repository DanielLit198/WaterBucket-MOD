package com.daniel.waterbucket.entity.waterball;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MeteoriteModel extends GeoModel<MeteoriteEntity> {
    @Override
    public Identifier getModelResource(MeteoriteEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"geo/entity/waterball.geo.json");
    }

    @Override
    public Identifier getTextureResource(MeteoriteEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"textures/entity/water.png");
    }

    @Override
    public Identifier getAnimationResource(MeteoriteEntity animatable) {
        return null;
    }
}
