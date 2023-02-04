package com.daniel.waterbucket.item;

import com.daniel.waterbucket.entity.ModEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WaterBucketCannon extends Item {
    public boolean isSpawn;
    public int mode;
    public WaterBucketCannon() {
        super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient){
            return;
        }
        double x = entity.getRotationVector().getX();
        double y = entity.getRotationVector().getY();
        double z = entity.getRotationVector().getZ();
        double a = Math.acos(z / Math.sqrt(x * x + z * z));
        if (-x < 0) a = a * -1;
        WaterBucketEntity waterBucket = new WaterBucketEntity(ModEntity.WATER_BUCKET_ENTITY, world);
        if (selected && !isSpawn){
            waterBucket.refreshPositionAndAngles(entity.getX() + 2 * Math.cos(a + 0.25 * Math.PI), entity.getY() + entity.getRotationVector().getY() + 1.2, entity.getZ() + 2 * Math.sin(a + 0.25 * Math.PI), 0, 0);
            waterBucket.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(entity.getX() + x * 100, entity.getY() + 1.2 + y * 100, entity.getZ() + z * 100));
            waterBucket.player = (PlayerEntity) entity;
            world.spawnEntity(waterBucket);
            isSpawn = true;
            System.out.println(1);

        } else if (!selected && isSpawn){
            isSpawn = false;
            waterBucket.dead = true;
            System.out.println(2);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        System.out.println(mode);
        WaterBucketEntity waterBucket = new WaterBucketEntity(ModEntity.WATER_BUCKET_ENTITY, world);
        double x = user.getRotationVector().getX();
        double y = user.getRotationVector().getY();
        double z = user.getRotationVector().getZ();
        double a = Math.acos(z / Math.sqrt(x * x + z * z));
        if (-x < 0) {
            a = a * -1;
        }
        if (!world.isClient) mode = mode + 1;
        if (mode == 1) {
            waterBucket.refreshPositionAndAngles(user.getX() + 2 * Math.cos(a + 0.25 * Math.PI), user.getY() + user.getRotationVector().getY() + 1.2, user.getZ() + 2 * Math.sin(a + 0.25 * Math.PI), 0, 0);
            waterBucket.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(user.getX() + x * 100, user.getY() + 1.2 + y * 100, user.getZ() + z * 100));
            waterBucket.player = user;
            world.spawnEntity(waterBucket);
        }
        if (mode >= 2){
            System.out.println("dd");
            mode = 0;
        }



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
