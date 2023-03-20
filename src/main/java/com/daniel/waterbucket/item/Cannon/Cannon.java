package com.daniel.waterbucket.item.Cannon;

import com.daniel.waterbucket.entity.ModEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Cannon extends Item {
    public NbtCompound nbt = new NbtCompound();
    public Cannon() {
        super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        //WaterBucketEntity的初始设定
        double x = entity.getRotationVector().getX();
        double y = entity.getRotationVector().getY();
        double z = entity.getRotationVector().getZ();
        double a = Math.acos(z / Math.sqrt(x * x + z * z));
        if (-x < 0) a = a * -1;
        WaterBucketEntity waterBucket = new WaterBucketEntity(ModEntity.WATER_BUCKET_ENTITY, world);
        waterBucket.refreshPositionAndAngles(entity.getX() + 2 * Math.cos(a + 0.25 * Math.PI), entity.getY() + entity.getRotationVector().getY() + 1.2, entity.getZ() + 2 * Math.sin(a + 0.25 * Math.PI), 0, 0);
        waterBucket.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(entity.getX() + x * 100, entity.getY() + 1.2 + y * 100, entity.getZ() + z * 100));
        if (entity instanceof PlayerEntity player) {
            waterBucket.item = stack;
            waterBucket.player = player;
            if (selected && !stack.hasNbt()) {
                stack.setNbt(new NbtCompound());
                stack.getNbt().putBoolean("spawn",false);
            }
            //nbt.putBoolean("sure", false);
            //使生成只会执行一次
           if (selected && stack.hasNbt() && !stack.getNbt().getBoolean("spawn") && !stack.getNbt().getBoolean("sure")) {
               world.spawnEntity(waterBucket);
               stack.getNbt().putBoolean("spawn", true);
           } else if (!selected && stack.hasNbt() && stack.getNbt().getBoolean("spawn")) {
               stack.getNbt().putBoolean("spawn", false);
          }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (stack.hasNbt()) {
            return stack.getNbt().getBoolean("sure");
        }
        return super.hasGlint(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()){
            tooltip.add(Text.translatable(String.valueOf(stack.getNbt().getBoolean("spawn"))));
            tooltip.add(Text.translatable(String.valueOf(stack.getNbt().getBoolean("sure"))));
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //确定位置以及传输确定信息到WaterBucketEntity
        if (user.getMainHandStack().getNbt().getBoolean("spawn")) {
            user.getMainHandStack().getNbt().putBoolean("sure", true);
            //发送玩家的最后坐标与视角向量固定水桶
            ArrayList<Long> arrayList = new ArrayList();
            arrayList.add((long) (user.getX()*10000));
            arrayList.add((long) (user.getY()*10000));
            arrayList.add((long) (user.getZ()*10000));
            arrayList.add((long) (user.getRotationVector().getX()*10000));
            arrayList.add((long) (user.getRotationVector().getY()*10000));
            arrayList.add((long) (user.getRotationVector().getZ()*10000));
            user.getMainHandStack().getNbt().putLongArray("xyz", arrayList);
        }
        return super.use(world, user, hand);
    }
}
