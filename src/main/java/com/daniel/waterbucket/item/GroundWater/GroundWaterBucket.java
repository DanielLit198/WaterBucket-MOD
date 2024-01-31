package com.daniel.waterbucket.item.GroundWater;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.ParticleInit;
import com.daniel.waterbucket.init.SoundsInit;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import com.daniel.waterbucket.mixin.ParticleCountMixin;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class GroundWaterBucket extends Item {
    public GroundWaterBucket() {
        super(new Settings());
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.hasNbt()){
            stack.getNbt().putBoolean("use",true);
            stack.getNbt().putInt("x", user.getBlockPos().getX());
            stack.getNbt().putInt("y", user.getBlockPos().getY());
            stack.getNbt().putInt("z", user.getBlockPos().getZ());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.hasNbt()){
            stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("tick",0);
            stack.getNbt().putBoolean("use",false);
        }
        if (stack.hasNbt()){
            if (stack.getNbt().getBoolean("use")) {
                NbtCompound nbt = stack.getNbt();
                if (stack.getNbt().getInt("tick") % 4 == 0){
                    nbt.putInt("times",nbt.getInt("times") + 1);
                    if (nbt.getInt("water") > 0){
                        ground(world, new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")), entity,nbt.getInt("times"),stack.getNbt().getInt("water"));

                    }

                    if (nbt.getInt("meteorite") > 0){
                        for (int i = 0; i < nbt.getInt("meteorite"); i++) {
                            Meteorite.spawn(world,entity,null,20 + (stack.getNbt().getInt("water")/20 * 10),null);
                        }
                    }
                }

                if (nbt.getInt("tick") >= 4){
                    nbt.putInt("tick",0);
                    nbt.putBoolean("use",false);
                    nbt.putInt("times",0);
                    if (entity instanceof PlayerEntity player && !player.isCreative()){
                        stack.decrement(1);
                    }
                }

                nbt.putInt("tick",stack.getNbt().getInt("tick") + 1);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {


        return super.useOnBlock(context);
    }

    public static void ground(World world, BlockPos pos,Entity entity,int times,int counts){
        int count = (counts / 20) + 1;
        particle1(pos, world, entity,times,count);
        world.playSound(entity,pos,SoundsInit.Group_Water_Explosion,SoundCategory.VOICE,10f, (float) (0.9f+new Random().nextDouble(0.5)));
        entity.setVelocity(0,1.5 + count-1,0);
        if (entity instanceof LivingEntity livingEntity)
            livingEntity.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 80, 0, false, false), entity);

        Box box = new Box(pos).expand(1.5);
        for (int i = 0; i < world.getOtherEntities(null, box).size(); i++) {
            world.getOtherEntities(null,box).get(i).setVelocity(0,1.5 + count-1,0);
        }
    }

    public static void particle1(BlockPos pos, World world,Entity entity,float times,int counts) {
        if (world.isClient()) return;
        PacketByteBuf bf = PacketByteBufs.create();
        bf.writeBlockPos(pos);
        bf.writeVector3f(new Vector3f((float) entity.getRotationVector().getX(),0, (float) entity.getRotationVector().getZ()));
        bf.writeFloat(times);
        bf.writeInt(counts);
        for (ServerPlayerEntity player : entity.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new Identifier(Waterbucket.MOD_ID, "ground"), bf);
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.hasNbt()) {
            tooltip.add(Text.of("水桶：[" + stack.getNbt().getInt("water") + "]"));
            if (stack.getNbt().getInt("meteorite") > 0)
            tooltip.add(Text.of("陨石水桶：[" + stack.getNbt().getInt("meteorite") + "]"));
        }
    }


}
