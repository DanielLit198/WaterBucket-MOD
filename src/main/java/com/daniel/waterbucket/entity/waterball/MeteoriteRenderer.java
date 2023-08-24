package com.daniel.waterbucket.entity.waterball;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MeteoriteRenderer extends GeoProjectilesRenderer<MeteoriteEntity> {

    public MeteoriteRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MeteoriteModel());
        this.shadowOpacity = 0.4f;
    }

    @Override
    public RenderLayer getRenderType(MeteoriteEntity animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        float b = 2;
        stack.scale(b,b,b);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
