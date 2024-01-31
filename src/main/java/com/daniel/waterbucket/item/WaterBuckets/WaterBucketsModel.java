package com.daniel.waterbucket.item.WaterBuckets;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class WaterBucketsModel extends GeoModel<WaterBuckets> {

    @Override
    public Identifier getModelResource(WaterBuckets animatable) {
        return new Identifier(Waterbucket.MOD_ID,"geo/item/waterbuckets.geo.json");

    }

    @Override
    public Identifier getTextureResource(WaterBuckets animatable) {
        return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets3.png");
    }

    @Override
    public Identifier getAnimationResource(WaterBuckets animatable) {
        return null;
    }
}
