package com.daniel.waterbucket.entity.cannon;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterBucketRenderer extends GeoEntityRenderer<WaterBucketEntity> {

    public WaterBucketRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WaterBucketModel());
        this.shadowOpacity = 0.4f;
    }

    @Override
    public void renderFinal(MatrixStack poseStack, WaterBucketEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(WaterBucketEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, entity.prevYaw, entity.getYaw()) + 90.0F));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTick, -entity.prevPitch, -entity.getPitch())));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

        float s = (float)entity.shake - partialTick;
        if (s > 0.0F) {
            float t = -MathHelper.sin(s * 3.0F) * s;
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t));
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

    }

    @Override
    public RenderLayer getRenderType(WaterBucketEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
