package com.daniel.waterbucket.item.Meteorite;

import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class MeteoriteShower extends Item {
    private int Age;
    public boolean spawn;
    public int isRecorded = 0;
    ItemStack itemStack = null;
    public boolean PisOnG = false;
    public MeteoriteShower() {
        super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
        Age = 0;
        spawn = false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity user) {
            int number = new Random().nextInt(100);
            if (spawn && Age <= 500) {
                Age = Age + 1;
                if (Age >= 55 && world.getBlockState(new BlockPos(user.getBlockX(), user.getBlockY() - 5, user.getBlockZ())).isOf(Blocks.AIR) && !PisOnG) {
                    user.setVelocity(0, -0.1, 0);
                } else if (!world.getBlockState(new BlockPos(user.getBlockX(), user.getBlockY() - 5, user.getBlockZ())).isOf(Blocks.AIR)) PisOnG = true;
                if (number >= 70) {
                    int w = new Random().nextInt(2) == 1 ? 1 : -1;
                    int f = new Random().nextInt(50) * w;
                    int g = new Random().nextInt(2) == 1 ? 1 : -1;
                    int h = new Random().nextInt(50) * g;

                    MeteoriteEntity waterBallN = new MeteoriteEntity(EntityInit.WATER_BALL, world);
                    waterBallN.refreshPositionAndAngles(user.getX() + f, user.getY() + 20, user.getZ() + h, 0, 0);
                    waterBallN.setVec3(new Vec3d(0, ((getBlock(user) - waterBallN.getY()) / 10) * 0.3, 0));
                    world.spawnEntity(waterBallN);
                    waterBallN.setOwner(user);
                }
            }else if (Age >= 500) {
                Age = 0;
                spawn = false;
                PisOnG = false;
                itemStack.decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1, 1);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        spawn = true;

        int q = new Random().nextInt(2) == 1 ? 1:-1;
        int d = 10 * q;
        int a = new Random().nextInt(2) == 1 ? 1:-1;
        int c = 10 * a;
        double m = 0.5;

        MeteoriteEntity waterBall = new MeteoriteEntity(EntityInit.WATER_BALL, world);
        waterBall.refreshPositionAndAngles(user.getX() + d , user.getY() +10, user.getZ() + c, 0, 0);
        waterBall.setVec3(new Vec3d(-q * m, ((getBlock(user) - waterBall.getY()) / 10) * m, -a * m));
        world.spawnEntity(waterBall);
        user.setVelocity(new Vec3d(0, 2, 0));
        waterBall.setOwner(user);

        if (user.getStackInHand(Hand.MAIN_HAND).getItem() == this) {
            itemStack = user.getStackInHand(Hand.MAIN_HAND);
        } else if (user.getStackInHand(Hand.OFF_HAND).getItem() == this) {
            itemStack = user.getStackInHand(Hand.OFF_HAND);
        }

        user.getItemCooldownManager().set(this,250);

        for (int i = 0; i < 50; i++) {
            if (user.getInventory().getStack(i).getItem() == this && user.getInventory().getStack(i) != itemStack){
                user.getInventory().getStack(i).decrement(1);
            }
        }

        return super.use(world, user, hand);
    }

    public int getBlock(PlayerEntity user){
        int x = 0;
        while (true){
            if (user.getWorld().getBlockState(new BlockPos(user.getBlockX(),user.getBlockY()+x,user.getBlockZ())) != Blocks.AIR.getDefaultState()){
                return user.getBlockY()+x;
            }
            x = x -1;
        }
    }

}
