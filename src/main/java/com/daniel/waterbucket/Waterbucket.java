package com.daniel.waterbucket;

import com.daniel.waterbucket.entity.ModEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketRenderer;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteRenderer;
import com.daniel.waterbucket.item.MeteoriteBucket;
import com.daniel.waterbucket.item.MeteoriteBucketO;
import com.daniel.waterbucket.item.TestItem;
import com.daniel.waterbucket.item.WaterBucketCannon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Waterbucket implements ModInitializer {
    public static final String MOD_ID = "waterbucket";

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(ModEntity.WATER_BALL, MeteoriteEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ModEntity.WATER_BUCKET_ENTITY, WaterBucketEntity.createMobAttributes());

        EntityRendererRegistry.register(ModEntity.WATER_BALL, MeteoriteRenderer::new);
        EntityRendererRegistry.register(ModEntity.WATER_BUCKET_ENTITY, WaterBucketRenderer::new);
        Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"waterbucket"),new MeteoriteBucket());
        Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"waterbucketo"),new MeteoriteBucketO());
        Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"waterbucketcannon"),new WaterBucketCannon());
        Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"test"),new TestItem());    }
}
