package com.daniel.waterbucket.entity.tank;

import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.TankItem.*;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.explosion.Explosion;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Exp;

import java.util.Queue;

import static net.minecraft.block.Block.dropStacks;

public class bulletEntity extends ThrownItemEntity {
    public bulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public bulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world, ItemStack stack){
        super(entityType, world);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            if (getItem().getItem() instanceof Bullet4) {
                for (int i = 0; i < 32; ++i) {
                    world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian());
                }
            }else {
                for (int i = 0; i < 100; i++) {
                    double speedX = (Math.random() - 0.5) * 2;
                    double speedY = (Math.random() - 0.5) * 2;
                    double speedZ = (Math.random() - 0.5) * 2;
                    world.addParticle(ParticleTypes.FIREWORK, true, getX(), getY(), getZ(), speedX, speedY, speedZ);
                    if (getItem().getItem() instanceof Bullet2) world.addParticle(ParticleTypes.LAVA, true, getX(), getY(), getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient && !this.isRemoved()) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), getBulletDamage());
            }
            if (getItem().getItem() instanceof Bullet4) {
                enderBullet();
            } else {
                boolean fire = false;
                if (getItem().getItem() instanceof Bullet2) fire = true;
                this.world.createExplosion(this.getOwner(), this.getX(), this.getBodyY(0.0625), this.getZ(), getBulletPower(), fire, Explosion.DestructionType.BREAK);

                if (getItem().getItem() instanceof Bullet5) {
                    for (int i = 0; i < 4; i++) {
                        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                        lightningEntity.teleport(this.getX(), this.getY(), this.getZ());
                        world.spawnEntity(lightningEntity);
                    }
                }

                if (getItem().getItem() instanceof Bullet6) {
                    Explosion explosion2 = new Explosion(world, this.getOwner(), this.getX(), this.getBodyY(0.0625), this.getZ(), getBulletPower(), false, Explosion.DestructionType.BREAK);
                    explosion2.collectBlocksAndDamageEntities();
                    for (int i = 0; i < explosion2.getAffectedBlocks().toArray().length; i++) {
                        BlockPos boomPos = new BlockPos(explosion2.getAffectedBlocks().get(i));
                        world.setBlockState(boomPos, Blocks.FROSTED_ICE.getDefaultState());
                        world.createAndScheduleBlockTick(boomPos, Blocks.FROSTED_ICE, MathHelper.nextInt(((LivingEntity) this.getOwner()).getRandom(), 10, 20));

                    }
                    Box box = new Box(new BlockPos(hitResult.getPos())).expand(5);
                    world.getEntitiesByClass(LivingEntity.class, box, entity -> true)
                            .forEach(entity -> entity.setInPowderSnow(true));
//                }
//            }

                }
            }
            this.world.sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }
    public void enderBullet(){
        if (!this.world.isClient && !this.isRemoved()) {
            Entity entity = this.getOwner().getFirstPassenger();
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
                if (serverPlayerEntity.networkHandler.getConnection().isOpen() && serverPlayerEntity.world == this.world && !serverPlayerEntity.isSleeping()) {
                    if (entity.hasVehicle()) {
                        this.getOwner().setVelocity(this.getX()-this.getOwner().getX(), this.getY()-this.getOwner().getY(), this.getZ()-this.getOwner().getZ());
                    }
                }
            } else if (entity != null) {
                entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                entity.onLanding();
            }
        }
    }
    @Override
    public void tick() {
        addVelocity(0,0.0325,0);
        world.addParticle(ParticleTypes.CLOUD,true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        super.tick();
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.bullet;
    }
    public int getBulletDamage(){
        if (getItem().getItem() instanceof Bullet && getItem().getNbt() != null){
            int damage = getItem().getNbt().getInt("damage");
            return damage;
        }
        return 0;
    }
    public int getBulletPower(){
        if (getItem().getItem() instanceof Bullet && getItem().getNbt() != null){
            int power = getItem().getNbt().getInt("explode");
            return power;
        }
        return 0;
    }
}
