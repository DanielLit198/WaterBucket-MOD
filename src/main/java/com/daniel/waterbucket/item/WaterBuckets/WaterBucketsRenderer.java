package com.daniel.waterbucket.item.WaterBuckets;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.RendererInit;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WaterBucketsRenderer extends GeoItemRenderer<WaterBuckets> {

    public WaterBucketsRenderer() {
        super(new WaterBucketsModel());
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderFinal(MatrixStack poseStack, WaterBuckets animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Identifier getTextureLocation(WaterBuckets animatable) {
        if (getCurrentItemStack().hasNbt()) {
            int count = getCurrentItemStack().getNbt().getInt("count");
            int water = getCurrentItemStack().getNbt().getInt("water");
            int meteorite = getCurrentItemStack().getNbt().getInt("meteorite");
            if (count >= 20){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets5.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets5.png");
            } else if (count >= 16){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets4.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets4.png");
            } else if (count >= 12){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets3.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets3.png");
            } else if (count >= 8){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets2.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets2.png");
            } else if (count >= 4){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets1.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets1.png");
            } else if (count == 3){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets0.2.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets0.2.png");
            }else if (count == 2){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets0.1.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets0.1.png");
            }else if (count == 1){
                if (water >= 1 || meteorite >= 1){
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/water_buckets0.png");
                }else
                    return new Identifier(Waterbucket.MOD_ID,"textures/item/buckets0.png");
            }

        }
        return super.getTextureLocation(animatable);
    }

    @Override
    public ItemStack getCurrentItemStack() {
        return super.getCurrentItemStack();
    }
}
