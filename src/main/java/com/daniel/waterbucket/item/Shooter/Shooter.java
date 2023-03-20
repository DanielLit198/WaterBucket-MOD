package com.daniel.waterbucket.item.Shooter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Random;

public class Shooter extends Item {

    public Shooter() {
        super(new Settings().group(ItemGroup.COMBAT).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.getStackInHand(hand).hasNbt() && user.getStackInHand(hand).getNbt().getBoolean("fire") == false) {
            //确认玩家信息
            double x = user.getX();
            double y = user.getY();
            double z = user.getZ();
            double vx = user.getRotationVector().getX();
            double vy = user.getRotationVector().getY();
            double vz = user.getRotationVector().getZ();
            user.getStackInHand(hand).getNbt().putDouble("x", x);
            user.getStackInHand(hand).getNbt().putDouble("y", y);
            user.getStackInHand(hand).getNbt().putDouble("z", z);
            user.getStackInHand(hand).getNbt().putDouble("vx", vx);
            user.getStackInHand(hand).getNbt().putDouble("vy", vy);
            user.getStackInHand(hand).getNbt().putDouble("vz", vz);
        }
        //开火
        user.getStackInHand(hand).getNbt().putBoolean("fire", true);
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.hasNbt())stack.setNbt(new NbtCompound());
        if (stack.hasNbt() && stack.getNbt().getBoolean("fire")) {
            PlayerEntity user = (PlayerEntity) entity;
            double x = stack.getNbt().getDouble("x");
            double y = stack.getNbt().getDouble("y");
            double z = stack.getNbt().getDouble("z");
            double vx = stack.getNbt().getDouble("vx");
            double vy = stack.getNbt().getDouble("vy");
            double vz = stack.getNbt().getDouble("vz");
            stack.getNbt().putInt("a",stack.getNbt().getInt("a") + 1);
            hit(world,user,stack,stack.getNbt().getInt("a"),x,y,z,vx,vy,vz);
            writeNbt(world,stack);
        }else if (selected == false){

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    //写入NBT
    public void writeNbt(World world,ItemStack stack){
        if (world.isClient())return;
        stack.getNbt().putInt("a",stack.getNbt().getInt("a")+1);
        //"a"为发射的距离
        if (stack.getNbt().getInt("a") >= 20){
            stack.getNbt().putInt("a",0);
            stack.getNbt().putBoolean("fire",false);
        }
    }

    public void hit(World world, PlayerEntity user,ItemStack stack,double i, double ux, double uy, double uz, double uvx, double uvy, double uvz){
        double a = i * 0.5;
        double x = ux + uvx * a;
        double y = uy + 1.5 + uvy * a;
        double z = uz + uvz * a;
        for (int j = 0; j < world.getEntitiesByClass(Entity.class,new Box(new BlockPos(x,y,z)),entity -> true).toArray().length; j++) {
            LivingEntity entities = null;
            if (world.getEntitiesByClass(Entity.class,new Box(new BlockPos(x,y,z)), entity -> true).get(j) instanceof LivingEntity)
            entities = (LivingEntity) world.getEntitiesByClass(Entity.class,new Box(new BlockPos(x,y,z)), entity -> true).get(j);
            if (entities != null && entities.getType() != EntityType.PLAYER) {
                entities.damage(DamageSource.GENERIC, 5);
                entities.takeKnockback(1, -uvx, -uvz);
            }
        }
        for (int j = 0; j < 60; j++) {
            int q = new Random().nextInt(2) == 1 ? 1:-1;
            double w = new Random().nextDouble(1.2) * q;
            int e = new Random().nextInt(2) == 1 ? 1:-1;
            double r = new Random().nextDouble(1.2) * e;
            int t = new Random().nextInt(2) == 1 ? 1:-1;
            double c = new Random().nextDouble(1.2) * t;
            for (int k = 0; k < 3; k++) {
                int u = new Random().nextInt(2) == 1 ? 1:-1;
                double b = new Random().nextDouble(0.8) * u;
                int o = new Random().nextInt(2) == 1 ? 1:-1;
                double n = new Random().nextDouble(0.8) * o;
                int p = new Random().nextInt(2) == 1 ? 1:-1;
                double m = new Random().nextDouble(0.8) * p;
                world.addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER,true,x+b,y+n,z+m,0,0,0);
            }
            world.addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER,true,x+w,y+r,z+c,0,0,0);

        }

    }
}
