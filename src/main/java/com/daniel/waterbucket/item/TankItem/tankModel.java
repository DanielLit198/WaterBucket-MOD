package com.daniel.waterbucket.item.TankItem;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class tankModel extends AnimatedGeoModel<tank> {
    @Override
    public Identifier getModelResource(tank object) {
        return new Identifier(Waterbucket.MOD_ID,"geo/entity/tank.geo.json");
    }

    @Override
    public Identifier getTextureResource(tank object) {
        return new Identifier(Waterbucket.MOD_ID,"textures/entity/tank.png");
    }

    @Override
    public Identifier getAnimationResource(tank animatable) {
        return null;
    }
}
