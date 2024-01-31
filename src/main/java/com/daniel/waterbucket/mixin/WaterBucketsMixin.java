package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class WaterBucketsMixin {
    public ItemStack getWaterBucket(PlayerEntity user,ItemStack stack) {
        for (int i = 0; i < user.getInventory().size(); ++i) {
            ItemStack bucket = user.getInventory().getStack(i);
            if (bucket != stack) {
                if (bucket.getItem() == Items.WATER_BUCKET || bucket.getItem() == Items.BUCKET
                        || bucket.getItem() == ItemInit.meteorite) {
                    return bucket;
                }
            }
        }
        return null;
    }

    @Inject(method = "use", at = @At("HEAD"))
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (user.isSneaking()) {
            ItemStack handBucket = user.getStackInHand(hand);
            ItemStack buckets = new ItemStack(ItemInit.WaterBuckets);
            buckets.setNbt(new NbtCompound());
            ItemStack backPack = getWaterBucket(user,handBucket);
            if (backPack != null) {
                //结算手上水桶转换成压缩桶
                if (handBucket.getItem() == Items.WATER_BUCKET) {
                    buckets.getNbt().putInt("count", buckets.getNbt().getInt("count") + 1);
                    buckets.getNbt().putInt("water", buckets.getNbt().getInt("water") + 1);
                    buckets.getNbt().putInt("bucket", buckets.getNbt().getInt("bucket") + 1);
                    user.sendMessage(Text.of("水桶：[" + buckets.getNbt().getInt("water") + " / " + buckets.getNbt().getInt("bucket") + "]"), true);
                } else {
                    buckets.getNbt().putInt("bucket", buckets.getNbt().getInt("bucket") + 1);
                    buckets.getNbt().putInt("count", buckets.getNbt().getInt("count") + 1);
                    user.sendMessage(Text.of("水桶：[" + buckets.getNbt().getInt("water") + " / " + buckets.getNbt().getInt("bucket") + "]"), true);
                }
                //结算背包里的水桶
                if (backPack.getItem() == Items.WATER_BUCKET) {
                    buckets.getNbt().putInt("count", buckets.getNbt().getInt("count") + 1);
                    buckets.getNbt().putInt("water", buckets.getNbt().getInt("water") + 1);
                    buckets.getNbt().putInt("bucket", buckets.getNbt().getInt("bucket") + 1);
                    user.sendMessage(Text.of("水桶：[" + buckets.getNbt().getInt("water") + " / " + buckets.getNbt().getInt("bucket") + "]"), true);
                } else if (backPack.getItem() == Items.BUCKET) {
                    buckets.getNbt().putInt("bucket", buckets.getNbt().getInt("bucket") + 1);
                    buckets.getNbt().putInt("count", buckets.getNbt().getInt("count") + 1);
                    user.sendMessage(Text.of("水桶：[" + buckets.getNbt().getInt("water") + " / " + buckets.getNbt().getInt("bucket") + "]"), true);
                } else if (backPack.getItem() == ItemInit.meteorite) {
                    buckets.getNbt().putInt("meteorite", buckets.getNbt().getInt("meteorite") + 1);
                    buckets.getNbt().putInt("count", buckets.getNbt().getInt("count") + 1);
                    user.sendMessage(Text.of("陨石水桶：[" + buckets.getNbt().getInt("meteorite") + "]"), true);
                }
                handBucket.decrement(1);
                world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW, SoundCategory.BLOCKS, 1.0F, 1.0F);
                backPack.decrement(1);
                user.giveItemStack(buckets);

            }
        }
    }
}
