package com.daniel.waterbucket.init;

import com.daniel.waterbucket.entity.cannon.WaterBucketRenderer;
import com.daniel.waterbucket.entity.chest.ChestEntityRenderer;
import com.daniel.waterbucket.entity.tank.TankEntity;
import com.daniel.waterbucket.entity.tank.TankEntityRenderer;
import com.daniel.waterbucket.entity.waterball.MeteoriteRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class RendererInit {
    public static void init(){
        EntityRendererRegistry.register(EntityInit.CHEST_ENTITY, ChestEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.WATER_BALL, MeteoriteRenderer::new);
        EntityRendererRegistry.register(EntityInit.WATER_BUCKET_ENTITY, WaterBucketRenderer::new);
        EntityRendererRegistry.register(EntityInit.TANK, TankEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.SPONGE_BALL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.bullet, FlyingItemEntityRenderer::new);

    }
}
