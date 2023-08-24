package com.daniel.waterbucket.entity.tank;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TankEntityModel extends AnimatedGeoModel<TankEntity> {
    @Override
    public Identifier getModelResource(TankEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"geo/entity/tank.geo.json");
    }

    @Override
    public Identifier getTextureResource(TankEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"textures/entity/tank.png");
    }

    @Override
    public Identifier getAnimationResource(TankEntity animatable) {
        return null;
    }
}
