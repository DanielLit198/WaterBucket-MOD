package com.daniel.waterbucket.entity.cannon;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WaterBucketRenderer extends GeoEntityRenderer<WaterBucketEntity> {

    public WaterBucketRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WaterBucketModel());
        this.shadowOpacity = 0.4f;
    }

    @Override
    public RenderLayer getRenderType(WaterBucketEntity animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        float b = 1.5F;
        stack.scale(b,b,b);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}
