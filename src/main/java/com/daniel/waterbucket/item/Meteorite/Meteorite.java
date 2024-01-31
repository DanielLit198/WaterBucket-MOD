package com.daniel.waterbucket.item.Meteorite;

import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.init.ItemInit;
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
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Meteorite extends Item {
    public int Age;
    public static boolean spawn;
    public Meteorite() {
        super(new FabricItemSettings().maxCount(1));
        Age = 0;
        spawn = false;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        spawn(world,user,hand,0,null);
        if (!user.isCreative()) {
            if (user.getStackInHand(hand).getItem() == ItemInit.meteorite) {
                user.getStackInHand(hand).decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    public static void spawn(World world, Entity user, Hand hand,int y,@Nullable BlockPos pos){
        spawn = true;

        double z = new Random().nextDouble(10);
        int q = new Random().nextInt(2) == 1 ? 1:-1;
        double d = 10 * q;
        int a = new Random().nextInt(2) == 1 ? 1:-1;
        double c = 10 * a;
        double m = 0.8;

        MeteoriteEntity waterBall = new MeteoriteEntity(EntityInit.WATER_BALL, world);
        if (pos != null){
            waterBall.refreshPositionAndAngles(pos.getX() + d , pos.getY() +40+y, pos.getZ() + c, 0, 0);
            waterBall.setVec3(new Vec3d(-q * m, ((getBlock(user,pos)+y - waterBall.getY()) / 10) * m, -a * m));
        }else {
            waterBall.refreshPositionAndAngles(user.getX() + d , user.getY() +40+y, user.getZ() + c, 0, 0);
            waterBall.setVec3(new Vec3d(-q * m, ((getBlock(user,null)+y - waterBall.getY()) / 10) * m, -a * m));
        }
        waterBall.setOwner(user);
        world.spawnEntity(waterBall);
    }

    public static int getBlock(Entity user,@Nullable BlockPos pos){
        int x = 0;
        if (pos != null){
            while (true){
                if (user.getWorld().getBlockState(new BlockPos(pos.getX(),pos.getY()+x,pos.getZ())) != Blocks.AIR.getDefaultState()){
                    return pos.getY()+x;
                }
                x = x -1;
            }
        }else{
            while (true){
                if (user.getWorld().getBlockState(new BlockPos(user.getBlockX(),user.getBlockY()+x,user.getBlockZ())) != Blocks.AIR.getDefaultState()){
                    return user.getBlockY()+x;
                }
                x = x -1;
            }
        }
    }
}
