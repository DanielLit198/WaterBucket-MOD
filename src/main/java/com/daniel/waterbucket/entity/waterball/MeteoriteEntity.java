package com.daniel.waterbucket.entity.waterball;

import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.init.ParticleInit;
import com.daniel.waterbucket.item.WaterBuckets.WaterBuckets;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class MeteoriteEntity extends PersistentProjectileEntity implements GeoAnimatable {
    public Vec3d vec3;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public MeteoriteEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public void setVec3(Vec3d vec3) {
        this.vec3 = vec3;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult || hitResult instanceof BlockHitResult blockHitResult && getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock() != Blocks.AIR && getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock() != Blocks.WATER) {
            particle1();
            particle3();
            explosion();
            waterDamage();
            discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    public void waterDamage(){
        double range = 5;
        Box box = new Box(this.getBlockPos()).expand(range);
//        for (BlockPos b : BlockPos.iterate((int) box.minX, (int)box.minY, (int)box.minZ, (int)box.maxX, (int)box.maxY, (int)box.maxZ)) {
//            // 设置该位置的方块为红色玻璃方块
//            world.setBlockState(b, Blocks.RED_STAINED_GLASS.getDefaultState(), 2);
//        }
        float amount = 18;
        getWorld().getEntitiesByClass(LivingEntity.class,box, entity -> true)
                .forEach(entity -> entity.damage(getDamageSources().mobAttack((LivingEntity) this.getOwner()), amount));
    }
    @Override
    public void tick(){
        if (vec3 != null) setVelocity(vec3);
        if (!this.isOnGround())particle2();

        Box box = new Box(this.getBlockPos()).expand(3);
        System.out.println(this.getWorld().getOtherEntities(this, box).size());
        if (this.getWorld().getOtherEntities(this, box).size() > 0){
            for (int i = 0; i < this.getWorld().getOtherEntities(null, box).size(); i++) {
                if (!(this.getWorld().getOtherEntities(null, box).get(i) instanceof MeteoriteEntity)) {
                    particle1();
                    particle3();
                    explosion();
                    waterDamage();
                    discard();
                }
            }
        }

        if (!getWorld().getBlockState(getBlockPos()).isAir()){
            particle1();
            particle3();
            explosion();
            waterDamage();
            discard();
        }
        super.tick();
    }
    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.AIR);
    }
    public void explosion(){
        if (getWorld().isClient()) return;
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE,4,0);
        Explosion explosion = new Explosion(this.getWorld(),this,this.getX(),this.getY(),this.getZ(),1.4f,false, Explosion.DestructionType.DESTROY);
        explosion.collectBlocksAndDamageEntities();
        for (int i = 0; i < explosion.getAffectedBlocks().toArray().length; i++) {
            BlockPos boomPos = new BlockPos(explosion.getAffectedBlocks().get(i));
            this.getWorld().setBlockState(boomPos, Blocks.WATER.getDefaultState());
        }

    }

    public void particle1(){
        double a = 0;
        double b = 0;

        double r = 2;
        double fr = 3;
        for (int i = 0; i < 3; i++) {
            b = b + 0.1;
            r = r + b;
            fr = fr + b;
            for (int j = 0; j < 200; j++) {
                a = a + 0.01 * Math.PI;
                double x = this.getX() + r * Math.cos(a);
                double z = this.getZ() + r * Math.sin(a);

                double fx = this.getX() + fr * Math.cos(a);
                double fz = this.getZ() + fr * Math.sin(a);
                this.getWorld().addParticle(ParticleTypes.CLOUD,true, x, this.getY() + 2, z, fx - x, 0, fz - z);
            }
        }
    }

    public void particle2(){

        for (int i = 0; i < 160; i++) {
            int q = new Random().nextInt(2) == 1 ? 1:-1;
            double w = new Random().nextDouble(3) * q;
            int e = new Random().nextInt(2) == 1 ? 1:-1;
            double r = new Random().nextDouble(3) * e;
            int t = new Random().nextInt(2) == 1 ? 1:-1;
            double y = new Random().nextDouble(3) * t;

            int a = new Random().nextInt(2) == 1 ? 1:-1;
            double s = new Random().nextDouble(3) * a;
            int d = new Random().nextInt(2) == 1 ? 1:-1;
            double f = new Random().nextDouble(3) * d;
            int g = new Random().nextInt(2) == 1 ? 1:-1;
            double h = new Random().nextDouble(3) * g;

            int v = new Random().nextInt(2) == 1 ? 1:-1;
            int b = new Random().nextInt(2) == 1 ? 1:-1;
            int n = new Random().nextInt(2) == 1 ? 1:-1;
            int m = new Random().nextInt(2) == 1 ? 1:-1;
            int l = new Random().nextInt(2) == 1 ? 1:-1;
            int k = new Random().nextInt(2) == 1 ? 1:-1;


            this.getWorld().addParticle(ParticleTypes.CLOUD,true,getX()+s,getY()+f,getZ()+h,v * 0.3,b * 0.3,n * 0.3);
            this.getWorld().addParticle(ParticleInit.Water,true,getX()+w,getY()+y,getZ()+r,m,l,k);
        }
    }

    public void particle3(){
        for (int i = 0; i < 300; i++) {
            double y = new Random().nextDouble(5);
            double r = 1 + new Random().nextInt(5) + y*3;
            double c = new Random().nextDouble(2);
            double a = c * Math.PI;
            double x = getX() + r * Math.cos(a);
            double z = getZ() + r * Math.sin(a);

            int q = 1;
            this.getWorld().addParticle(ParticleInit.WaterDrop,true,getX(),getY()+2,getZ(),(x - getX()) * q,y * q,(z - getZ()) * q);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
