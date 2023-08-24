package com.daniel.waterbucket.entity.spongeball;

import com.daniel.waterbucket.init.ItemInit;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Queue;

import static net.minecraft.block.Block.dropStacks;

public class SpongeBallEntity extends ThrownItemEntity {
    public ItemStack stack;
    public SpongeBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public SpongeBallEntity(EntityType<? extends ThrownItemEntity> entityType,World world,ItemStack stack){
        super(entityType, world);
        this.stack = stack;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult player && player.getEntity().isPlayer()) return;
        for (int i = 0; i < 10; i++)
        world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(ItemInit.spongeBall)), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.3, ((double) this.random.nextFloat() - 0.5) * 0.3, ((double) this.random.nextFloat() - 0.5) * 0.3);
        super.onCollision(hitResult);
    }
    @Override
    public void tick() {
        if (!world.isClient()){
            if (world.getBlockState(getBlockPos()).getBlock() == Blocks.WATER) {
                ItemEntity item = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), ItemInit.wetSpongeBall.getDefaultStack());
                world.spawnEntity(item);
                absorbWater(world, getBlockPos());
                discard();
            }else if (world.getBlockState(getBlockPos()).getBlock() != Blocks.AIR){
                ItemEntity item = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), ItemInit.spongeBall.getDefaultStack());
                world.spawnEntity(item);
                discard();
            }
        }
        super.tick();
    }
    private boolean absorbWater(World world, BlockPos pos) {
        Queue<Pair<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Pair(pos, 0));
        int i = 0;

        while(!queue.isEmpty()) {
            Pair<BlockPos, Integer> pair = (Pair)queue.poll();
            BlockPos blockPos = (BlockPos)pair.getLeft();
            int j = (Integer)pair.getRight();
            Direction[] var8 = Direction.values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Direction direction = var8[var10];
                BlockPos blockPos2 = blockPos.offset(direction);
                BlockState blockState = world.getBlockState(blockPos2);
                FluidState fluidState = world.getFluidState(blockPos2);
                Material material = blockState.getMaterial();
                if (fluidState.isIn(FluidTags.WATER)) {
                    if (blockState.getBlock() instanceof FluidDrainable && !((FluidDrainable)blockState.getBlock()).tryDrainFluid(world, blockPos2, blockState).isEmpty()) {
                        ++i;
                        if (j < 6) {
                            queue.add(new Pair(blockPos2, j + 1));
                        }
                    } else if (blockState.getBlock() instanceof FluidBlock) {
                        world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Pair(blockPos2, j + 1));
                        }
                    }else if (material == Material.UNDERWATER_PLANT || material == Material.REPLACEABLE_UNDERWATER_PLANT) {
                        BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(blockPos2) : null;
                        dropStacks(blockState, world, blockPos2, blockEntity);
                        world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Pair(blockPos2, j + 1));
                        }
                    }
                }
            }

            if (i > 64) {
                break;
            }
        }

        return i > 0;
    }
    @Override
    protected Item getDefaultItem() {
        return ItemInit.spongeBall;
    }
}
