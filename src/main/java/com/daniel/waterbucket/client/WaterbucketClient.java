package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.ParticleInit;
import com.daniel.waterbucket.init.RendererInit;
import com.daniel.waterbucket.init.SoundsInit;
import com.daniel.waterbucket.mixin.ParticleCountMixin;
import com.daniel.waterbucket.particle.CloudParticle;
import com.daniel.waterbucket.particle.WaterBucketParticle;
import com.daniel.waterbucket.particle.WaterDropParticle;
import com.daniel.waterbucket.particle.WaterParticle;
import com.daniel.waterbucket.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WaterbucketClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModModelPredicateProvider.registerModModels();
        HudRenderCallback.EVENT.register(new HudOverlay());
        RendererInit.init();
        CannonParticleS2C.init();
        SoundsInit.init();

        ParticleFactoryRegistry.getInstance().register(ParticleInit.WaterDrop, WaterDropParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.Water, WaterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.WaterBucket, WaterBucketParticle.CloudFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.Cloud, CloudParticle.CloudFactory::new);
    }
}
