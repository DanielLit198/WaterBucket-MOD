package com.daniel.waterbucket.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class WaterBucketParticle extends SpriteBillboardParticle {
    /*
    水桶吸收水粒子
     */
    float c = 0.25f;
    public WaterBucketParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.maxAge = 100;
        double q = 0.1;
        this.velocityX = g * q;
        this.velocityY = h * q;
        this.velocityZ = i * q;
    }


    @Override
    public void tick() {
        if (age >= 60 + new Random().nextInt(40) && c >= 0){
            c = c - 0.02f;
        }else if (c <= 0) c = 0;
        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return c;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider
         ) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            WaterBucketParticle waterDropParticle = new WaterBucketParticle(clientWorld, d, e, f, g, h, i);
            waterDropParticle.setSprite(this.spriteProvider);
            return waterDropParticle;
        }
    }
}
