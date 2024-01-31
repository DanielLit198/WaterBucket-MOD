package com.daniel.waterbucket.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CloudParticle extends SpriteBillboardParticle {
    /*
   水桶吸收水粒子
    */
    float c = 0.25f;

    private final SpriteProvider spriteProvider;


    public CloudParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.velocityMultiplier = 0.96F;
        this.spriteProvider = spriteProvider;
        float f = 2.5F;
        this.velocityX *= 0.10000000149011612;
        this.velocityY *= 0.10000000149011612;
        this.velocityZ *= 0.10000000149011612;
        this.velocityX += velocityX;
        this.velocityY += velocityY;
        this.velocityZ += velocityZ;
        float g = 1.0F - (float)(Math.random() * 0.30000001192092896);
        this.red = g;
        this.green = g;
        this.blue = g;
        this.scale *= 1.875F;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.maxAge = (int)Math.max((float)i * 5F, 1.0F);
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }


    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float)this.age + tickDelta) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        super.tick();
        if (!world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).isAir())this.markDead();
        if (!this.dead) {
            this.velocityY = this.velocityY -0.001;

            this.setSpriteForAge(this.spriteProvider);
            PlayerEntity playerEntity = this.world.getClosestPlayer(this.x, this.y, this.z, 2.0, false);
            if (playerEntity != null) {
                double d = playerEntity.getY();
                if (this.y > d) {
                    this.y += (d - this.y) * 0.2;
                    this.velocityY += (playerEntity.getVelocity().y - this.velocityY) * 0.2;
                    this.setPos(this.x, this.y, this.z);
                }
            }
        }
//        world.addParticle(ParticleInit.Water,true,this.x,this.y,this.z,0,0,0);
    }

    @Environment(EnvType.CLIENT)
    public static class SneezeFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public SneezeFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            Particle particle = new WaterBucketParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            particle.setColor(200.0F, 50.0F, 120.0F);
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class CloudFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public CloudFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new WaterBucketParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
