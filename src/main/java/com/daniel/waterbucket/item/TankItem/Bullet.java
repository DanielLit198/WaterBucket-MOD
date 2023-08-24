package com.daniel.waterbucket.item.TankItem;

import com.daniel.waterbucket.entity.spongeball.SpongeBallEntity;
import com.daniel.waterbucket.entity.tank.bulletEntity;
import com.daniel.waterbucket.init.EntityInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Bullet extends Item {
    public Bullet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getVehicle() != null && user.getVehicle().getType() == EntityInit.TANK){
            if (!stack.hasNbt())stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("damage", setDamage());
            stack.getNbt().putInt("explode", setPower());
            for (int i = 0; i < setCount(); i++) {
                bulletEntity bulletEntity = new bulletEntity(EntityInit.bullet, world, stack);
                bulletEntity.setItem(stack);
                bulletEntity.teleport(user.getVehicle().getX(), user.getY(), user.getVehicle().getZ());
                bulletEntity.setVelocity(user, user.getVehicle().getPitch(), user.getVehicle().getYaw(), 0F, 2.5F, setCount()-1);
                bulletEntity.setOwner(user.getVehicle());
                bulletEntity.setItem(stack);
                world.spawnEntity(bulletEntity);
            }
            user.getItemCooldownManager().set(stack.getItem(),setCoolDown());

            user.getVehicle().setVelocity(-user.getVehicle().getRotationVector().getX()/1.3,0,-user.getVehicle().getRotationVector().getZ()/1.3);
            for (int i = 0; i < 10; i++) {
                double speedX = (Math.random() - 0.5) * 0.1;
                double speedY = (Math.random() - 0.5) * 0.1;
                double speedZ = (Math.random() - 0.5) * 0.1;
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, user.getVehicle().getX(), user.getVehicle().getY(), user.getVehicle().getZ(), speedX, 0.1, speedZ);
            }
            user.getVehicle().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, -100000F);
            stack.decrement(1);
        }
        return super.use(world, user, hand);
    }
    public int setCoolDown(){
        return 20;
    }
    public int setCount(){
        return 1;
    }
    public int setDamage(){
        return 3;
    }
    public int setPower(){
        return 1;
    }
}
