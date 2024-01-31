package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.GroundWater.GroundWaterBucket;
import com.daniel.waterbucket.item.WaterBuckets.WaterBuckets;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilBlock.class)
public class anvilCompressed {

    @Inject(method = "onLanding", at = @At("HEAD"))
    public void landing(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity, CallbackInfo ci){
        if (world instanceof ServerWorld serverWorld){
            for (int i = 0; i < serverWorld.getOtherEntities(fallingBlockEntity,new Box(pos)).size(); i++) {
                if (serverWorld.getOtherEntities(fallingBlockEntity,new Box(pos)).get(i) instanceof ItemEntity itemEntity && itemEntity.getStack().getItem() instanceof WaterBuckets){
                    itemEntity.discard();
                    ItemStack itemStack = new ItemStack(ItemInit.GroundWaterBucket);
                    if (!itemStack.hasNbt()) {
                        itemStack.setNbt(new NbtCompound());
                        itemStack.getNbt().putInt("water",itemEntity.getStack().getNbt().getInt("water"));
                        itemStack.getNbt().putInt("meteorite",itemEntity.getStack().getNbt().getInt("meteorite"));
                    }
                    ItemEntity stack = new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ(),itemStack);
                    world.spawnEntity(stack);
                    break;
                }
            }
        }
    }
}
