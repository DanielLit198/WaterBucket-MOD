package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleInit {
    public static final DefaultParticleType WaterBucket  = FabricParticleTypes.simple();
    public static final DefaultParticleType WaterDrop = FabricParticleTypes.simple();
    public static final DefaultParticleType Water = FabricParticleTypes.simple();
    public static final DefaultParticleType Cloud = FabricParticleTypes.simple();
    public static void init(){
        Registry.register(Registries.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterdrop"), WaterDrop);
        Registry.register(Registries.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"water"), Water);
        Registry.register(Registries.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucketp"), WaterBucket);
        Registry.register(Registries.PARTICLE_TYPE,new Identifier(Waterbucket.MOD_ID,"cloud"), Cloud);

    }
}
