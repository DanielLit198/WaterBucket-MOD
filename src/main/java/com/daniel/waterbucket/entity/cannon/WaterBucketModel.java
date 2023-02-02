package com.daniel.waterbucket.entity.cannon;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WaterBucketModel extends AnimatedGeoModel<WaterBucketEntity> {
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

    @Override
    public void setCustomAnimations(WaterBucketEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone bucket = this.getAnimationProcessor().getBone("group");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        int unpausedMultiplier = !MinecraftClient.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

        bucket.setRotationX(bucket.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
        bucket.setRotationY(bucket.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    }
}
