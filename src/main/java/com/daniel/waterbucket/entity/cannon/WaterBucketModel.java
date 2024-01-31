package com.daniel.waterbucket.entity.cannon;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class WaterBucketModel extends GeoModel<WaterBucketEntity> {
    @Override
    public Identifier getModelResource(WaterBucketEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"geo/entity/waterbucket.geo.json");
    }

    @Override
    public Identifier getTextureResource(WaterBucketEntity object) {
        return new Identifier(Waterbucket.MOD_ID,"textures/entity/waterbucket_texture.png");
    }

    @Override
    public Identifier getAnimationResource(WaterBucketEntity animatable) {
        return null;
    }

}
