package com.daniel.waterbucket.item;

import com.daniel.waterbucket.entity.ModEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WaterBucketCannon extends Item {
    public WaterBucketCannon() {
        super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        double x = user.getRotationVector().getX();
        double z = user.getRotationVector().getZ();
        double a = Math.acos(z/Math.sqrt(x*x+z*z));
        if (-x < 0){
            a = a * -1;
        }
        WaterBucketEntity waterBucket = new WaterBucketEntity(ModEntity.WATER_BUCKET_ENTITY,world);
        waterBucket.refreshPositionAndAngles(user.getX()+2 * Math.cos(a + 0.25 * Math.PI),user.getY() + user.getRotationVector().getY() + 1.2,user.getZ() + 2 * Math.sin(a + 0.25 * Math.PI),0,0);
        waterBucket.player = user;
        world.spawnEntity(waterBucket);

        if (!user.isCreative()) {
            if (user.getStackInHand(Hand.MAIN_HAND).getItem() == this) {
                user.getStackInHand(Hand.MAIN_HAND).decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
            } else if (user.getStackInHand(Hand.OFF_HAND).getItem() == this)
                user.getStackInHand(Hand.OFF_HAND).decrement(1);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
        }

        return super.use(world, user, hand);
    }
}
