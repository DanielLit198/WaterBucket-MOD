package com.daniel.waterbucket.entity.chest;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ChestEntityModel extends GeoModel<ChestEntity> {

    @Override
    public Identifier getModelResource(ChestEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"geo/entity/chest.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChestEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"textures/entity/chest.png");
    }

    @Override
    public Identifier getAnimationResource(ChestEntity animatable) {
        return null;
    }
}
