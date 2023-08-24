package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.particle.WaterBucketParticle;
import com.daniel.waterbucket.particle.WaterDropParticle;
import com.daniel.waterbucket.particle.WaterParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleInit {
    public static final DefaultParticleType WaterBucket  = FabricParticleTypes.simple();
    public static final DefaultParticleType WaterDrop = FabricParticleTypes.simple();
    public static final DefaultParticleType Water = FabricParticleTypes.simple();
    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterdrop"), WaterDrop);
        Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"water"), Water);
        Registry.register(Registry.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucketp"), WaterBucket);

    }
}
