package com.daniel.waterbucket.entity.cannon;

import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.init.*;
import com.daniel.waterbucket.item.GroundWater.GroundWaterBucket;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3d;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class WaterBucketEntity extends PersistentProjectileEntity implements GeoAnimatable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ItemStack stack;
    public ItemStack bullet;
    public int charged;
    public int explodeTime;

    public WaterBucketEntity(EntityType<? extends WaterBucketEntity> entityType, World world) {
        super(entityType, world);

    }
    public WaterBucketEntity(EntityType<? extends WaterBucketEntity> entityType, World world,ItemStack stack,ItemStack bullet,int charged) {
        super(entityType,world);
        this.stack = stack;
        this.charged = charged;
        if (bullet != null) this.bullet = bullet;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (stack != null) {
            if (hitResult instanceof EntityHitResult entity && entity.getEntity() instanceof PlayerEntity player && player == getOwner() || hitResult instanceof BlockHitResult block && getWorld().getBlockState(block.getBlockPos()).getBlock() == Blocks.AIR ||hitResult instanceof BlockHitResult blocks && getWorld().getBlockState(blocks.getBlockPos()).getBlock() == Blocks.WATER) return;
            //水量附魔
            int aquamagnitude = EnchantmentHelper.getLevel(EnchantmentInit.aquamagnitude, stack);

            this.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT,4,10);
            //附上火矢或者在地狱的情况
            if (getWorld().getDimension().ultrawarm() || EnchantmentHelper.getLevel(Enchantments.FLAME, stack) >= 1) {
                double range = 2;
                double count = 20;

                //水量影响粒子范围以及数量
                if (aquamagnitude >= 1) {
                    range = 2 + aquamagnitude;
                    count = 20 * (10* aquamagnitude);
                }
                //正常粒子+音效
                Random random = new Random();
                for (int i = 0; i < count; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * range;
                    double offsetY = (random.nextDouble() - 0.5) * range;
                    double offsetZ = (random.nextDouble() - 0.5) * range;
                    getWorld().addParticle(ParticleTypes.CLOUD, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0, 0, 0);
                }
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 2.6F + (getWorld().random.nextFloat() - getWorld().random.nextFloat()) * 0.8F);
                this.discard();
                return;
            }
            //陨石水桶
            if (bullet != null && bullet.getItem() == ItemInit.meteorite) {
                int q = new Random().nextInt(2) == 1 ? 1 : -1;
                int d = 10 * q;
                int a = new Random().nextInt(2) == 1 ? 1 : -1;
                int c = 10 * a;
                double m = 0.7;

                MeteoriteEntity waterBall = new MeteoriteEntity(EntityInit.WATER_BALL, getWorld());
                waterBall.refreshPositionAndAngles(this.getX() + d, this.getY() + 40, this.getZ() + c, 0, 0);
                waterBall.setVec3(new Vec3d(-q * m, ((this.getY() - waterBall.getY()) / 10) * m, -a * m));
                waterBall.setOwner(this.getOwner());
                getWorld().spawnEntity(waterBall);
                this.discard();
                return;
            }
            //压缩
            if (bullet !=null && bullet.getItem() == ItemInit.GroundWaterBucket && bullet.hasNbt()){
                if (bullet.getNbt().getInt("meteorite") > 0) {
                    for (int i = 0; i < bullet.getNbt().getInt("meteorite"); i++) {
                        Meteorite.spawn(this.getWorld(), this, null,20 + (stack.getNbt().getInt("water")/20 * 10),null);
                    }
                }
                if (bullet.getNbt().getInt("water") > 0){
                    GroundWaterBucket.ground(this.getWorld(),this.getBlockPos(),this,1,bullet.getNbt().getInt("water"));
                }
                discard();
                return;
            }
            //水量附魔
            if (aquamagnitude >= 1) {
                //粒子
                for (int i = 0; i < 400; i++) {
                    double offsetX = getWorld().random.nextGaussian() * 1.5;
                    double offsetY = getWorld().random.nextGaussian() * 1.5;
                    double offsetZ = getWorld().random.nextGaussian() * 1.5;
                    Vec3d offset = new Vec3d(offsetX, offsetY, offsetZ);
                    getWorld().addParticle(ParticleInit.WaterDrop, this.getX(), this.getY(), this.getZ(), offset.x, offset.y, offset.z);

                }
                //放置水
                if (!getWorld().isClient()) {
                    float power = 0.2F;
                    if (aquamagnitude >= 1) power = 0.2F + (0.2F * aquamagnitude);
                    Explosion explosion = new Explosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), power, false, Explosion.DestructionType.KEEP);
                    explosion.collectBlocksAndDamageEntities();
                    for (int i = 0; i < explosion.getAffectedBlocks().toArray().length; i++) {
                        BlockPos boomPos = new BlockPos(explosion.getAffectedBlocks().get(i));
                        if (getWorld().getBlockState(boomPos) == Blocks.AIR.getDefaultState()) {
                            this.getWorld().setBlockState(boomPos, Blocks.WATER.getDefaultState());
                        }
                    }
                    explosion.affectWorld(false);
                    discard();
                }
                return;
            }
            //无水量附魔
            if (!getWorld().isClient()) {
                BlockState water = Blocks.WATER.getDefaultState();
                getWorld().setBlockState(this.getBlockPos(), water, 3);
                discard();
            }
            dropTheItem(stack);
        }
        super.onCollision(hitResult);
    }
    public Entity dropTheItem(ItemStack stack){
        //充能Ⅱ以及不是水桶的不掉落
        ItemStack bucket = new ItemStack(Items.BUCKET);
        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), bucket);
        if (stack != null && EnchantmentHelper.getLevel(EnchantmentInit.loyalty, stack) > 0) {
            itemEntity.setVelocity(this.getOwner().getPos().subtract(itemEntity.getPos()));
        }
        getWorld().spawnEntity(itemEntity);
        return itemEntity;
    }
    @Override
    public void tick() {
        this.addVelocity(0,-0.05,0);
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.AMBIENT_UNDERWATER_EXIT;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
