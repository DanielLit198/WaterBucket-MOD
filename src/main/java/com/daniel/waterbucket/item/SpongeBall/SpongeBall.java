package com.daniel.waterbucket.item.SpongeBall;

import com.daniel.waterbucket.entity.spongeball.SpongeBallEntity;
import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.init.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class SpongeBall extends Item {
    public SpongeBall() {
        super(new Settings().maxCount(16).group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ServerPlayerEntity player) {
            System.out.println(player.getSpawnPointDimension());

        }

        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient()) {
            int count = 1;
            if (user.isSneaking()) count = stack.getCount();
            for (int i = 0; i < count; i++) {
                SpongeBallEntity spongeBallEntity = new SpongeBallEntity(EntityInit.SPONGE_BALL, world, stack);
                spongeBallEntity.setItem(stack);
                spongeBallEntity.teleport(user.getX(), user.getY() + 1.5, user.getZ());
                spongeBallEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0F, 1.5F, 8.0F);
                world.spawnEntity(spongeBallEntity);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.4F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
