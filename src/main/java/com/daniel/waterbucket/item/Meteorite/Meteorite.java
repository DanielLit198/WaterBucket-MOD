package com.daniel.waterbucket.item.Meteorite;

import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
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

public class Meteorite extends Item {
    public int Age;
    public boolean spawn;
    public Meteorite() {
        super(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1));
        Age = 0;
        spawn = false;
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
        waterBall.refreshPositionAndAngles(user.getX() + d , user.getY() +40, user.getZ() + c, 0, 0);
        waterBall.setVec3(new Vec3d(-q * m, ((getBlock(user) - waterBall.getY()) / 10) * m, -a * m));
        waterBall.setOwner(user);
        world.spawnEntity(waterBall);


        if (!user.isCreative()) {
            if (user.getStackInHand(Hand.MAIN_HAND).getItem() == this) {
                user.getStackInHand(Hand.MAIN_HAND).decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
            } else if (user.getStackInHand(Hand.OFF_HAND).getItem() == this)
                user.getStackInHand(Hand.OFF_HAND).decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
        }



        return TypedActionResult.success(user.getStackInHand(hand));
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
