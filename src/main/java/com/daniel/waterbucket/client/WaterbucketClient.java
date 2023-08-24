package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.event.KeyInputHandler;
import com.daniel.waterbucket.init.ParticleInit;
import com.daniel.waterbucket.init.RendererInit;
import com.daniel.waterbucket.particle.WaterBucketParticle;
import com.daniel.waterbucket.particle.WaterDropParticle;
import com.daniel.waterbucket.particle.WaterParticle;
import com.daniel.waterbucket.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class WaterbucketClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModModelPredicateProvider.registerModModels();
        HudRenderCallback.EVENT.register(new HudOverlay());
        RendererInit.init();
        CannonParticleS2C.init();
//        KeyInputHandler.register();

        ParticleFactoryRegistry.getInstance().register(ParticleInit.WaterDrop, WaterDropParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.Water, WaterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.WaterBucket, WaterParticle.Factory::new);
    }
}
