package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.particle.WaterBucketParticle;
import com.daniel.waterbucket.particle.WaterDropParticle;
import com.daniel.waterbucket.particle.WaterParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class WaterbucketClient implements ClientModInitializer {

    public static final DefaultParticleType WaterDrop  = Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterdrop"), FabricParticleTypes.simple(true));
    public static final DefaultParticleType Water  = Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"water"), FabricParticleTypes.simple(true));
    public static final DefaultParticleType WaterBucket  = Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucketp"), FabricParticleTypes.simple(true));

    @Override
    public void onInitializeClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(Waterbucket.MOD_ID,"particles/waterdrop"));
        }));
        ParticleFactoryRegistry.getInstance().register(WaterDrop, WaterDropParticle.Factory::new);

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(Waterbucket.MOD_ID,"particles/water"));
        }));
        ParticleFactoryRegistry.getInstance().register(Water, WaterParticle.Factory::new);

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(Waterbucket.MOD_ID,"particles/waterbucketp"));
        }));
        ParticleFactoryRegistry.getInstance().register(WaterBucket, WaterBucketParticle.Factory::new);

    }
}
