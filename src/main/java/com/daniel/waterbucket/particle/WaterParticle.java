package com.daniel.waterbucket.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class WaterParticle extends SpriteBillboardParticle {
    /*
    水陨石坠落地面的粒子
     */
    float c = 0;
    public WaterParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.maxAge = 10;
        double q = 0.1;
        this.velocityX = g * q;
        this.velocityY = h * q;
        this.velocityZ = i * q;
    }

    @Override
    public void tick() {
        if (age >= 2){
            c = c + 0.03f;
        }
        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return 0.25f - c;
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
            WaterParticle waterParticle = new WaterParticle(clientWorld, d, e, f, g, h, i);
            waterParticle.setSprite(this.spriteProvider);
            return waterParticle;
        }
    }
}
