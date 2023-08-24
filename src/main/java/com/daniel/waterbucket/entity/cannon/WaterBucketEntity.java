package com.daniel.waterbucket.entity.cannon;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.client.WaterbucketClient;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.init.EnchantmentInit;
import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.init.ParticleInit;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import com.mojang.datafixers.kinds.IdF;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WaterBucketEntity extends PersistentProjectileEntity implements IAnimatable {

    public ItemStack stack;
    public ItemStack bullet;
    public int charged;
    public int explodeTime;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

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
            if (hitResult instanceof EntityHitResult entity && entity.getEntity() instanceof PlayerEntity player && player == getOwner() || hitResult instanceof BlockHitResult block && world.getBlockState(block.getBlockPos()).getBlock() == Blocks.AIR ||hitResult instanceof BlockHitResult blocks && world.getBlockState(blocks.getBlockPos()).getBlock() == Blocks.WATER) return;
            dropTheItem(stack);
            this.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT,4,10);
            //附上火矢或者在地狱的情况
            if (world.getDimension().ultrawarm() || EnchantmentHelper.getLevel(Enchantments.FLAME, stack) >= 1) {
                double range = 2;
                double count = 20;

                //充能Ⅱ会损坏桶的粒子以及音效
                if (stack.getNbt().getInt("charged_entity") >= 2) {
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.5F, 1);
                    for (int i = 0; i < 60; i++) {
                        world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.BUCKET)), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.8, ((double) this.random.nextFloat() - 0.5) * 0.8, ((double) this.random.nextFloat() - 0.5) * 0.8);
                    }
                    range = 5;
                    count = 100;
                }
                //正常粒子+音效
                Random random = new Random();
                for (int i = 0; i < count; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * range;
                    double offsetY = (random.nextDouble() - 0.5) * range;
                    double offsetZ = (random.nextDouble() - 0.5) * range;
                    world.addParticle(ParticleTypes.CLOUD, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0, 0, 0);
                }
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
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

                MeteoriteEntity waterBall = new MeteoriteEntity(EntityInit.WATER_BALL, world);
                waterBall.refreshPositionAndAngles(this.getX() + d, this.getY() + 40, this.getZ() + c, 0, 0);
                waterBall.setVec3(new Vec3d(-q * m, ((this.getY() - waterBall.getY()) / 10) * m, -a * m));
                waterBall.setOwner(this.getOwner());
                world.spawnEntity(waterBall);
                this.discard();
                return;
            }
            //充能2
            if (stack.getNbt().getInt("charged_entity") >= 2) {
                //粒子
                for (int i = 0; i < 400; i++) {
                    double offsetX = world.random.nextGaussian() * 1.5;
                    double offsetY = world.random.nextGaussian() * 1.5;
                    double offsetZ = world.random.nextGaussian() * 1.5;
                    Vec3d offset = new Vec3d(offsetX, offsetY, offsetZ);
                    world.addParticle(ParticleInit.WaterDrop, this.getX(), this.getY(), this.getZ(), offset.x, offset.y, offset.z);

                }
                //爆炸水
                if (!world.isClient()) {
                    Explosion explosion = new Explosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), 1, false, Explosion.DestructionType.BREAK);
                    explosion.collectBlocksAndDamageEntities();
                    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 0);
                    for (int i = 0; i < explosion.getAffectedBlocks().toArray().length; i++) {
                        BlockPos boomPos = new BlockPos(explosion.getAffectedBlocks().get(i));
                        if (getWorld().getBlockState(boomPos) == Blocks.AIR.getDefaultState()) {
                            this.getWorld().setBlockState(boomPos, Blocks.WATER.getDefaultState());
                        }
                    }
                    double range = 1;
                    Box box = new Box(new BlockPos(this.getX(),this.getY(),this.getZ())).expand(range);

                    world.getEntitiesByClass(LivingEntity.class,box, entity -> entity != getOwner())
                            .forEach(entity -> entity.damage(DamageSource.player((PlayerEntity) getOwner()), 5));

                    //水桶坏掉
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.5F, 1);
                    for (int i = 0; i < 60; i++) {
                        world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.BUCKET)), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.8, ((double) this.random.nextFloat() - 0.5) * 0.8, ((double) this.random.nextFloat() - 0.5) * 0.8);
                    }
                    discard();
                }
                //无充能附魔
            } else if (!world.isClient()) {
                BlockState water = Blocks.WATER.getDefaultState();
                world.setBlockState(this.getBlockPos(), water, 3);
                discard();
            }
            stack.getNbt().putInt("charged_entity", 0);
        }
        super.onCollision(hitResult);
    }
    public void dropTheItem(ItemStack stack){
        //充能Ⅱ以及不是水桶的不掉落
        if (stack == null || stack.getNbt().getInt("charged_entity") >= 2) return;
        ItemStack bucket = new ItemStack(Items.BUCKET);
        ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), bucket);
        if (stack != null && EnchantmentHelper.getLevel(EnchantmentInit.loyalty, stack) > 0) {
            itemEntity.setVelocity(this.getOwner().getPos().subtract(itemEntity.getPos()));
        }
        world.spawnEntity(itemEntity);
    }
    @Override
    public void tick() {
        if (stack != null && stack.hasNbt() && stack.getNbt().getInt("charged_entity") >= 2){
            explodeTime = explodeTime + 1;
            if (explodeTime >= 20){
                Explosion explosion = new Explosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), 1, false, Explosion.DestructionType.BREAK);
                explosion.collectBlocksAndDamageEntities();
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 0);
                for (int i = 0; i < explosion.getAffectedBlocks().toArray().length; i++) {
                    BlockPos boomPos = new BlockPos(explosion.getAffectedBlocks().get(i));
                    if (getWorld().getBlockState(boomPos) == Blocks.AIR.getDefaultState()) {
                        this.getWorld().setBlockState(boomPos, Blocks.WATER.getDefaultState());
                    }
                }
                stack.getNbt().putInt("charged_entity", 0);
                explodeTime = 0;
                discard();
            }
        }
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
    @Override
    public void registerControllers(AnimationData animationData) {}
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.AMBIENT_UNDERWATER_EXIT;
    }


}
