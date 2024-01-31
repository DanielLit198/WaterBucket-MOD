package com.daniel.waterbucket.item.SpongeBall;

import com.daniel.waterbucket.init.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class WetSpongeBall extends Item {
    public WetSpongeBall() {
        super(new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof PlayerEntity player){
            if (player.getItemUseTime() > 0){
                double range = 0.5;
                double count = 10;
                Random random = new Random();
                for (int i = 0; i < count; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * range;
                    double offsetY = (random.nextDouble() - 0.5) * range;
                    double offsetZ = (random.nextDouble() - 0.5) * range;
                    world.addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER, player.getX() + offsetX, player.getY() + offsetY + 1, player.getZ() + offsetZ, 0, 0, 0);
                }
            }
            if (player.getItemUseTime() >= 30){
                if (player.getStackInHand(Hand.MAIN_HAND).getItem() == this) {
                    player.setStackInHand(Hand.MAIN_HAND, new ItemStack(ItemInit.spongeBall,player.getStackInHand(Hand.MAIN_HAND).getCount()));
                }else player.setStackInHand(Hand.OFF_HAND, new ItemStack(ItemInit.spongeBall,player.getStackInHand(Hand.OFF_HAND).getCount()));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
