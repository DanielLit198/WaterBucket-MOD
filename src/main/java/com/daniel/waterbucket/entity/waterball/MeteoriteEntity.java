package com.daniel.waterbucket.entity.waterball;

import com.daniel.waterbucket.client.WaterbucketClient;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class MeteoriteEntity extends MobEntity implements IAnimatable {
    public PlayerEntity owner;
    int age;
    public int explosionTime;
    public Vec3d vec3;

    private final AnimationFactory factory = new AnimationFactory(this);

    public MeteoriteEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setVec3(Vec3d vec3) {
        this.vec3 = vec3;
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.updateVelocity(0.1f, movementInput);
        this.move(MovementType.SELF, this.getVelocity());
        if (world.getBlockState(new BlockPos(getX(),getY()-1,getZ())).isAir() && vec3 != null) {
            setVelocity(vec3);
        }else if (world.getBlockState(new BlockPos(getX(),getY()-1,getZ())).isOf(Blocks.WATER)){
            setVelocity(0,-2,0);
        }else setVelocity(0, 0, 0);
        super.travel(movementInput);
    }

    @Override
    public void tick(){
        if (!world.getBlockState(new BlockPos(getX(),getY()-1,getZ())).isAir() ||
                !world.getBlockState(new BlockPos(getX()+1,getY()-1,getZ())).isAir() ||
                !world.getBlockState(new BlockPos(getX()-1,getY()-1,getZ())).isAir() ||
                !world.getBlockState(new BlockPos(getX(),getY()-1,getZ()+1)).isAir() ||
                !world.getBlockState(new BlockPos(getX(),getY()-1,getZ()-1)).isAir() ||
                !world.getBlockState(new BlockPos(getX()+1,getY()-1,getZ()-1)).isAir() ||
                !world.getBlockState(new BlockPos(getX()-1,getY()-1,getZ()-1)).isAir() ||
                !world.getBlockState(new BlockPos(getX()+1,getY()-1,getZ()+1)).isAir() ||
                !world.getBlockState(new BlockPos(getX()-1,getY()-1,getZ()+1)).isAir()) {
            age = age + 1;
            if (explosionTime == 0) {
                explosion();
                explosionTime = explosionTime + 1;
            }
        }else particle2();


        if (age >= 40){
            discard();
        }
         super.tick();
    }
    public void explosion(){
        particle1();
        particle3();

        Explosion explosion = new Explosion(this.getWorld(),this,this.getX(),this.getY()-5,this.getZ(),30,false, Explosion.DestructionType.BREAK);
        explosion.collectBlocksAndDamageEntities();
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE,4,0);
        for (int i = 0; i < explosion.getAffectedBlocks().toArray().length; i++) {
            BlockPos boomPos = new BlockPos(explosion.getAffectedBlocks().get(i));
            if (getWorld().getBlockState(boomPos) != Blocks.AIR.getDefaultState()) {
                this.getWorld().setBlockState(boomPos, Blocks.WATER.getDefaultState());
            }
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
                this.world.addParticle(ParticleTypes.CLOUD,true, x, this.getY() + 2, z, fx - x, 0, fz - z);
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


            this.world.addParticle(ParticleTypes.CLOUD,true,getX()+s,getY()+f,getZ()+h,v * 0.3,b * 0.3,n * 0.3);
            this.world.addParticle(WaterbucketClient.Water,getX()+w,getY()+y,getZ()+r,m,l,k);
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
            this.world.addParticle(WaterbucketClient.WaterDrop, getX(),getY()+2,getZ(),(x - getX()) * q,y * q,(z - getZ()) * q);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
