package com.daniel.waterbucket.item.GroundWater;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class GroundWaterBucket extends Item {
    public GroundWaterBucket() {
        super(new Settings().group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        particle1(user, world);
        return super.use(world, user, hand);
    }
    public void particle1(PlayerEntity user,World world){
        for (int i = 0; i < 240; i++) {
            int q = new Random().nextInt(2) == 1 ? 1:-1;
            int e = new Random().nextInt(2) == 1 ? 1:-1;
            int w = new Random().nextInt(2) == 1 ? 1:-1;
            double t = new Random().nextDouble(1) * e;
            double y = new Random().nextDouble(1) * q;
            double u = new Random().nextDouble(1) * w;
            world.addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER,user.getX()+t,user.getY()+i*0.1,user.getZ()+u,0,0,0);

        }
    }
}
