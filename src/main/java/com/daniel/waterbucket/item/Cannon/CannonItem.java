package com.daniel.waterbucket.item.Cannon;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.init.EnchantmentInit;
import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.WaterBuckets.WaterBuckets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3f;

import java.util.Random;
import java.util.function.Predicate;

public class CannonItem extends BowItem{
    public ItemStack bullet;
    public boolean particle;

    public CannonItem() {
        super(new Settings().maxCount(1).maxDamage(400).maxDamageIfAbsent(400));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    //获取背包是否有水桶
    public ItemStack getWaterBucket(PlayerEntity user){
        for(int i = 0; i < user.getInventory().size(); ++i) {
            ItemStack bucket = user.getInventory().getStack(i);
            if (bucket.getItem() == ItemInit.meteorite || bucket.getItem() == Items.WATER_BUCKET
                    || bucket.getItem() == ItemInit.GroundWaterBucket
                    || bucket.getItem() == ItemInit.WaterBuckets && bucket.hasNbt() && bucket.getNbt().getInt("water") > 0) {
                bullet = bucket;
                return bucket;
            }
        }
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //设置NBT
        if (!user.getStackInHand(hand).hasNbt()) user.getStackInHand(hand).setNbt(new NbtCompound());
        //设置背包是否有水桶
        getWaterBucket(user);

        ItemStack itemStack = user.getStackInHand(hand);

        if (user.getAbilities().creativeMode || getWaterBucket(user) != null) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) user;
        int t = EnchantmentHelper.getLevel(EnchantmentInit.charged,stack);
        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);

        //耐久
        int p;
        //充能次数
        int charged = stack.getNbt().getInt("charged");
        //充能3
        if (t >= 1 && stack.hasNbt()){
            if (EnchantmentHelper.getLevel(Enchantments.FLAME,stack) >= 1){
                return;
            }
            if (charged < 1) {
                return;
            }
            laserParticle(world, stack, player);
            circleParticle(player, world);
            user.setVelocity(-user.getRotationVector().getX() * 2, -user.getRotationVector().getY() * 2, -user.getRotationVector().getZ() * 2);
            for (int i = 0; i < 3; i++) {
                user.getWorld().playSound((PlayerEntity) user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, new Random().nextFloat(2), new Random().nextFloat(2));
            }
            stack.getNbt().putInt("charged", 0);
            player.getItemCooldownManager().set(stack.getItem(),200);
            return;
        }
        //发射水桶
        WaterBucketEntity bucket = new WaterBucketEntity(EntityInit.WATER_BUCKET_ENTITY,world,stack,bullet,charged);
        bucket.setOwner(user);
        bucket.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        bucket.teleport(user.getX() + user.getRotationVector().getX() * 0.2, user.getY() + 1.5, user.getZ() + user.getRotationVector().getZ() * 0.2);
        float i = 0.1f;
        float useTime = getPullProgress(getMaxUseTime(stack)-remainingUseTicks);

        if (j > 0) {
            i = i + (0.5f * j) ;
            bucket.setDamage(bucket.getDamage() + (double)j * 0.5 + 0.5);
        }
        if (k > 0) {
            bucket.setPunch(k);
        }
        bucket.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, useTime * i, 1F);
        world.spawnEntity(bucket);

        //耐久设置
        if(!player.isCreative()){
            if (charged == 3) {
                p = (int) (10 + (getMaxUseTime(stack)-remainingUseTicks) * 0.1);
            }else if (charged ==2){
                p = 5;
            }else p = 1;
            stack.damage(p, user, (o) -> {
                o.sendToolBreakStatus(user.getActiveHand());
            });
        }
        //扣除水桶
        if (!player.getAbilities().creativeMode && getWaterBucket(player) != null) {
            if (getWaterBucket(player).getItem() instanceof WaterBuckets){
                if (stack.hasNbt()) {
                    getWaterBucket(player).getNbt().putInt("count",getWaterBucket(player).getNbt().getInt("count")-1);
                    getWaterBucket(player).getNbt().putInt("bucket",getWaterBucket(player).getNbt().getInt("bucket")-1);
                    getWaterBucket(player).getNbt().putInt("water",getWaterBucket(player).getNbt().getInt("water")-1);
                }
            }else getWaterBucket(player).decrement(1);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        tick(stack,entity.getWorld(),(PlayerEntity) entity,selected);
    }

    public void tick(ItemStack stack,World world,PlayerEntity entity,boolean selected){
        if (stack != null && stack.hasNbt()) {
            int charged = stack.getNbt().getInt("charged");
            int level = EnchantmentHelper.getLevel(EnchantmentInit.charged, stack);


            double useTime = entity.getItemUseTime()/3;
            if (useTime >= 7 && charged == 0 && level >= 1) {
                stack.getNbt().putInt("charged", charged + 1);
            }else if (useTime >= 14 && charged == 1 && level >= 2){
                stack.getNbt().putInt("charged",charged + 1);
            }else if (useTime >= 20 && charged == 2 && level >= 3) {
                stack.getNbt().putInt("charged", charged + 1);
            }

            if (entity.getItemUseTime() == 0){
                stack.getNbt().putInt("charged",0);
                //设置停止充能粒子NBT
                particle = false;
            }
        }
    }
    public void chargedParticle(PlayerEntity user,ItemStack stack,World world){
        if (world.isClient()) return;
        double q = new Random().nextDouble(1.2);
        if (new Random().nextDouble(100) >=95)
            for (int t = 0; t < 100; t++) {
                double[] first = circleParticlePoint(user,4 + q,2, t);
                double[] fin   = circleParticlePoint(user,1,2, t);

                int a = (int)(first[0]*100000);
                int b = (int)(first[1]*100000);
                int c = (int)(first[2]*100000);
                int d = (int)(fin[0]*100000);
                int e = (int)(fin[1]*100000);
                int f = (int)(fin[2]*100000);
                PacketByteBuf bf = PacketByteBufs.create();
                bf.writeIntArray(new int[]{a,b,c,d,e,f});
                for (ServerPlayerEntity player : user.getServer().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new Identifier(Waterbucket.MOD_ID, "particle4"), bf);
                }


            }
    }

    public void laserParticle(World world,ItemStack stack,PlayerEntity user){
        if (world.isClient()) return;
        if (stack.hasNbt()) {
            double x = user.getX();
            double y = user.getY() + 2;
            double z = user.getZ();
            double vx =  user.getRotationVector().getX();
            double vy =  user.getRotationVector().getY();
            double vz =  user.getRotationVector().getZ();

            for (int i = 0; i < 120; i++) {
                double s = 3;
                Vector3f point = new Vector3f((float) (x + vx * i), (float) (y + vy * i), (float) (z + vz * i));
                PacketByteBuf bf = PacketByteBufs.create();
                bf.writeVector3f(point);
                bf.writeDouble(s);
                // 迭代世界上所有追踪位置的玩家，并将数据包发送给每个玩家
                for (ServerPlayerEntity player : user.getServer().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new Identifier(Waterbucket.MOD_ID, "particle2"), bf);
                }
                laserDamage(world,point,user,stack);
//                if (world.getBlockState(new BlockPos(point)).getBlock() != Blocks.AIR) {
//                    explosion(world,user,stack,point.getX(), point.getY(), point.getZ(), i);
//                }
            }
        }
    }
    public void laserDamage(World world,Vector3f pos, PlayerEntity player, ItemStack stack){
        if (stack != null && stack.hasNbt()) {
            int charged = stack.getNbt().getInt("charged");
            double range = 0.5 + (player.getItemUseTime() * 0.003 * (EnchantmentHelper.getLevel(EnchantmentInit.rapid, stack) + 1f));
            Box box = new Box(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)).expand(range);
//        for (BlockPos b : BlockPos.iterate((int) box.minX, (int)box.minY, (int)box.minZ, (int)box.maxX, (int)box.maxY, (int)box.maxZ)) {
//            // 设置该位置的方块为红色玻璃方块
//            world.setBlockState(b, Blocks.RED_STAINED_GLASS.getDefaultState(), 2);
//        }
            float amount = 12 + charged * 6.5f;
            world.getEntitiesByClass(LivingEntity.class, box, entity -> entity != player)
                    .forEach(entity -> entity.damage(world.getDamageSources().playerAttack(player),amount));
        }
    }
    public void explosion(World world,PlayerEntity user,ItemStack stack,double x,double y,double z,double a){
        if (world.isClient()) return;
        if (a >= 110) {
            float power = 2f + (user.getItemUseTime()* (EnchantmentHelper.getLevel(EnchantmentInit.rapid,stack)+1) * 0.002f);
            if (user.getWorld().getDimensionKey() == DimensionTypes.THE_NETHER) power = 8;
            Explosion explosion2 = new Explosion(world, user, x, y, z, power, false, Explosion.DestructionType.DESTROY);
            explosion2.collectBlocksAndDamageEntities();
            if (user.getWorld().getDimensionKey() != DimensionTypes.THE_NETHER) for (int i = 0; i < explosion2.getAffectedBlocks().toArray().length; i++) {
                BlockPos boomPos = new BlockPos(explosion2.getAffectedBlocks().get(i));
                world.setBlockState(boomPos, Blocks.WATER.getDefaultState());
            }
        }else {
            float power = 1.8f + (user.getItemUseTime() * (EnchantmentHelper.getLevel(EnchantmentInit.rapid,stack)+1) * 0.001f);
            if (user.getWorld().getDimensionKey() == DimensionTypes.THE_NETHER) power = 5;
            Explosion explosion = new Explosion(world, user, x, y, z, power, false, Explosion.DestructionType.DESTROY);
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);
        }

    }
    //圆圈粒子
    public void circleParticle(Entity user,World world){
        for (int j = 0; j < 4; j++) {
            if (world.isClient()) return;
            for (int t = 0; t < 200 + j * 60; t++) {
                double[] first = circleParticlePoint(user,7 + j * 4,4+j * 12, t);
                PacketByteBuf bf = PacketByteBufs.create();
                int x = (int) (first[0]*100000);
                int y = (int) (first[1]*100000);
                int z = (int) (first[2]*100000);
                bf.writeIntArray(new int[]{x,y,z});
                // 迭代世界上所有追踪位置的玩家，并将数据包发送给每个玩家
                for (ServerPlayerEntity player : user.getServer().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new Identifier(Waterbucket.MOD_ID, "particle3"), bf);
                }
            }
        }
    }

    public double[] circleParticlePoint(Entity user,double h,double k,double t){
        //h为半径,k:距离,t:循环
        double a = user.getX() +     user.getRotationVector().getX() * k;
        double b = user.getY() + 2 + user.getRotationVector().getY() * k;
        double c = user.getZ() +     user.getRotationVector().getZ() * k;
        double r = h;
        double vx = user.getRotationVector().getX();
        double vy = user.getRotationVector().getY();
        double vz = user.getRotationVector().getZ();
        double[] n = {vx,vy,vz};
        double[] u = {n[1],-n[0],0};
        double[] v = {(n[1] * u[2])-(n[2] * u[1]),
                (n[2] * u[0])-(n[0] * u[2]),
                (n[0] * u[1])-(n[1] * u[0]),
        };
        //单位化向量:
        double us = Math.sqrt((u[0]*u[0])+(u[1]*u[1])+(u[2]*u[2]));
        double vs = Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2]));
        double[] uu = {u[0]/us,u[1]/us,0};
        double[] vv = {v[0]/vs,v[1]/vs,v[2]/vs};
        /*
        x=x_0+r(cos t)(d/|u|)+r(sin t)(b f-c e)/|v|
        y=y_0+r(cos t)(e/|u|)+r(sin t)(c d-a f)/|v|
        z=z_0+r(cos t)(f/|u|)+r(sin t)(a e-b d)/|v|
         */
        double x = a + r * Math.cos(t) * uu[0] + r * Math.sin(t) * vv[0];
        double y = b + r * Math.cos(t) * uu[1] + r * Math.sin(t) * vv[1];
        double z = c + r * Math.cos(t) * uu[2] + r * Math.sin(t) * vv[2];

        return new double[]{x, y, z};
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return 15;
    }
}
