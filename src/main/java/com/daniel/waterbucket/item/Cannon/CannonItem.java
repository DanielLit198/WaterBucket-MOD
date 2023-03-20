package com.daniel.waterbucket.item.Cannon;

import com.daniel.waterbucket.client.WaterbucketClient;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;

public class CannonItem extends RangedWeaponItem {
    public CannonItem() {
        super(new Settings().group(ItemGroup.COMBAT).maxCount(1));
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        //设置NBT
        if (!user.getStackInHand(hand).hasNbt()) user.getStackInHand(hand).setNbt(new NbtCompound());
        //开火NBT
        user.getStackInHand(hand).getNbt().putBoolean("wait", true);
        return TypedActionResult.success(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        //设置停止充能粒子NBT
        stack.getNbt().putBoolean("wait", false);
        stack.getNbt().putInt("power", 0);
        //激光炮与圆圈与玩家的击退实现
        if (getMaxUseTime(stack) - remainingUseTicks >= 120) {
            particle2(world, stack, user);
            particle3(stack, world);
            user.setVelocity(-user.getRotationVector().getX()*2, -user.getRotationVector().getY()*2, -user.getRotationVector().getZ()*2);
        }


    }
    public void confirm(LivingEntity user, ItemStack stack) {
        //确认玩家信息,记入NBT
        if (stack.hasNbt()) {
            double x = user.getX();
            double y = user.getY() + 2;
            double z = user.getZ();
            double vx = user.getRotationVector().getX();
            double vy = user.getRotationVector().getY();
            double vz = user.getRotationVector().getZ();
            stack.getNbt().putDouble("x", x);
            stack.getNbt().putDouble("y", y);
            stack.getNbt().putDouble("z", z);
            stack.getNbt().putDouble("vx", vx);
            stack.getNbt().putDouble("vy", vy);
            stack.getNbt().putDouble("vz", vz);
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        tick(stack,world,(PlayerEntity) entity,selected);
    }

    public void tick(ItemStack stack,World world,PlayerEntity entity,boolean selected){
        if (!selected) {
            stack.getNbt().putBoolean("wait", false);
            stack.getNbt().putInt("power", 0);
        }
        if (stack.hasNbt() && stack.getNbt().getBoolean("wait")){
            stack.getNbt().putInt("power", stack.getNbt().getInt("power")+1);
            if (stack.getNbt().getInt("power")>=100) {
                stack.getNbt().putInt("power", 100);
            }
            if (selected) confirm(entity,stack);
            //充能粒子
            particle(world,entity,stack.getNbt().getInt("power"));
            particle4(stack, world);
        }
    }
    public void particle(World world,PlayerEntity user,double power){
        int q = new Random().nextInt(2) == 1 ? 1:-1;
        int i = new Random().nextInt(2) == 1 ? 1:-1;
        int j = new Random().nextInt(2) == 1 ? 1:-1;
        double a = new Random().nextDouble(5) * q;
        double b = new Random().nextDouble(5) * i;
        double c = new Random().nextDouble(5) * j;
        Vec3d point = new Vec3d(user.getX() + user.getRotationVector().getX() * 2.5, user.getY() + 2 + user.getRotationVector().getY() * 2.5,user.getZ() + user.getRotationVector().getZ() * 2.5);
        world.addParticle(WaterbucketClient.Water,point.getX() + a,point.getY() + b,point.getZ() + c,-a/3.8,-b/3.8,-c/3.8);
        for (int l = 0; l < 100 * 0.5; l++) {
            double size = 0.1 + power * 0.001;
            Random m = new Random();
            Random k = new Random();
            Random o = new Random();
            double g = Math.sqrt(size) * m.nextGaussian() + 0;
            double h = Math.sqrt(size) * k.nextGaussian() + 0;
            double z = Math.sqrt(size) * o.nextGaussian() + 0;
            world.addParticle(WaterbucketClient.Water,point.getX() + g,point.getY() + h,point.getZ() + z,0,0,0);
        }
    }

    public Vec3d particlePoint(ItemStack stack,double i){
        if (stack.hasNbt()) {
            //接收信息
            double x = stack.getNbt().getDouble("x");
            double y = stack.getNbt().getDouble("y");
            double z = stack.getNbt().getDouble("z");
            double vx = stack.getNbt().getDouble("vx");
            double vy = stack.getNbt().getDouble("vy");
            double vz = stack.getNbt().getDouble("vz");
            Vec3d point = new Vec3d(x + vx * i, y + vy * i, z + vz * i);
            return point;
        }else return null;
    }

    public void particle2(World world,ItemStack stack,LivingEntity user){
        if (stack.hasNbt()) {
            //炮大小
            for (int i = 0; i < 100; i++) {
                double size = 3 + i * 0.5;
                for (int l = 0; l < 140; l++) {
                    Random q = new Random();
                    Random k = new Random();
                    Random j = new Random();
                    Random p = new Random();
                    Random o = new Random();
                    Random u = new Random();
                    double a = Math.sqrt(size) * q.nextGaussian() + 0;
                    double b = Math.sqrt(size) * k.nextGaussian() + 0;
                    double c = Math.sqrt(size) * j.nextGaussian() + 0;
                    double e = Math.sqrt(size) * p.nextGaussian() + 0;
                    double t = Math.sqrt(size) * o.nextGaussian() + 0;
                    double r = Math.sqrt(size) * u.nextGaussian() + 0;
                    Vec3d point = particlePoint(stack, i);
                    world.addParticle(WaterbucketClient.Water, point.getX() + a, point.getY() + b, point.getZ() + c, 0, 0, 0);
                    world.addParticle(ParticleTypes.CLOUD, true, point.getX() + e, point.getY() + t,point.getZ()+r, 0,0,0);

                    if (world.getBlockState(new BlockPos(point)).getBlock() != Blocks.AIR){
                        explosion(world,user, point.getX(), point.getY(),point.getZ(),i);
                    }
                }
            }
        }
    }
    public void explosion(World world,LivingEntity user,double x,double y,double z,double a){
        if (world.isClient()) return;
        for (int j = 0; j < 4; j++) {
            Explosion explosion = new Explosion(world, user, x, y, z, 8, false, Explosion.DestructionType.BREAK);
            explosion.collectBlocksAndDamageEntities();
            if (a >= 110) {
                Explosion explosion2 = new Explosion(world, user, x, y, z, 15, false, Explosion.DestructionType.BREAK);
                explosion2.collectBlocksAndDamageEntities();
                for (int i = 0; i < explosion2.getAffectedBlocks().toArray().length; i++) {
                    BlockPos boomPos = new BlockPos(explosion2.getAffectedBlocks().get(i));
                    world.setBlockState(boomPos, Blocks.WATER.getDefaultState());
                }
            } else explosion.affectWorld(true);
        }
    }
    public void particle3(ItemStack stack,World world){
        for (int j = 0; j < 4; j++) {
            for (int t = 0; t < 280 + j * 60; t++) {
                double[] first = particle3Point(stack,7 + j * 4,4+j * 12, t);
                world.addParticle(ParticleTypes.CLOUD, true, first[0], first[1], first[2],
                        0,0,0);
            }
        }
    }
    public void particle4(ItemStack stack,World world){
        double a = new Random().nextDouble(1.2);
        if (new Random().nextDouble(100) >=95)
        for (int t = 0; t < 100; t++) {
            double[] first = particle3Point(stack,4 + a,2, t);
            double[] fin   = particle3Point(stack,1,2, t);
            world.addParticle(ParticleTypes.CLOUD, true, first[0], first[1], first[2],
                    (fin[0]- first[0])/20,(fin[1]- first[1])/20,(fin[2]- first[2])/20);
        }
    }
    public double[] particle3Point(ItemStack stack,double h,double k,double t){
        //h为半径,k:距离,t:循环
        double a = stack.getNbt().getDouble("x") + stack.getNbt().getDouble("vx") * k;
        double b = stack.getNbt().getDouble("y") + stack.getNbt().getDouble("vy") * k;
        double c = stack.getNbt().getDouble("z") + stack.getNbt().getDouble("vz") * k;
        double r = h;
        double vx = stack.getNbt().getDouble("vx");
        double vy = stack.getNbt().getDouble("vy");
        double vz = stack.getNbt().getDouble("vz");
        double[] n = {vx,vy,vz};
        double[] u = {n[1],-n[0],0};
        double[] v = {(n[1] * u[2])-(n[2] * u[1]),
                (n[2] * u[0])-(n[0] * u[2]),
                (n[0] * u[1])-(n[1] * u[0]),
        };
        //单位化向量:
        double us = Math.sqrt((u[0]*u[0])+(u[1]*u[1])+(u[2]*u[2]));
        double vs = Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2]));
        double[] uu = {u[0]/us,u[1]/us,0};
        double[] vv = {v[0]/vs,v[1]/vs,v[2]/vs};
        /*
        x=x_0+r(cos t)(d/|u|)+r(sin t)(b f-c e)/|v|
        y=y_0+r(cos t)(e/|u|)+r(sin t)(c d-a f)/|v|
        z=z_0+r(cos t)(f/|u|)+r(sin t)(a e-b d)/|v|
         */

        double x = a + r * Math.cos(t) * uu[0] + r * Math.sin(t) * vv[0];
        double y = b + r * Math.cos(t) * uu[1] + r * Math.sin(t) * vv[1];
        double z = c + r * Math.cos(t) * uu[2] + r * Math.sin(t) * vv[2];

        return new double[]{x, y, z};
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return 15;
    }
}
