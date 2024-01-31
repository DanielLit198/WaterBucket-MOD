package com.daniel.waterbucket.entity.spongeball;

import com.daniel.waterbucket.init.ItemInit;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.block.Block.dropStacks;

public class SpongeBallEntity extends ThrownItemEntity {
    public ItemStack stack;
    private static final Direction[] field_43257 = Direction.values();
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
        getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(ItemInit.spongeBall)), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.3, ((double) this.random.nextFloat() - 0.5) * 0.3, ((double) this.random.nextFloat() - 0.5) * 0.3);
        super.onCollision(hitResult);
    }
    @Override
    public void tick() {
        if (!getWorld().isClient()){
            if (getWorld().getBlockState(getBlockPos()).getBlock() == Blocks.WATER) {
                ItemEntity item = new ItemEntity(getWorld(), this.getX(), this.getY(), this.getZ(), ItemInit.wetSpongeBall.getDefaultStack());
                getWorld().spawnEntity(item);
                absorbWater(getWorld(), getBlockPos());
                discard();
            }else if (getWorld().getBlockState(getBlockPos()).getBlock() != Blocks.AIR){
                ItemEntity item = new ItemEntity(getWorld(), this.getX(), this.getY(), this.getZ(), ItemInit.spongeBall.getDefaultStack());
                getWorld().spawnEntity(item);
                discard();
            }
        }
        super.tick();
    }
    private boolean absorbWater(World world, BlockPos pos) {
        return BlockPos.iterateRecursively(pos, 6, 65, (currentPos, queuer) -> {
            Direction[] var2 = field_43257;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Direction direction = var2[var4];
                queuer.accept(currentPos.offset(direction));
            }

        }, (currentPos) -> {
            if (currentPos.equals(pos)) {
                return true;
            } else {
                BlockState blockState = world.getBlockState(currentPos);
                FluidState fluidState = world.getFluidState(currentPos);
                if (!fluidState.isIn(FluidTags.WATER)) {
                    return false;
                } else {
                    Block block = blockState.getBlock();
                    if (block instanceof FluidDrainable) {
                        FluidDrainable fluidDrainable = (FluidDrainable)block;
                        if (!fluidDrainable.tryDrainFluid(world, currentPos, blockState).isEmpty()) {
                            return true;
                        }
                    }

                    if (blockState.getBlock() instanceof FluidBlock) {
                        world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
                    } else {
                        if (!blockState.isOf(Blocks.KELP) && !blockState.isOf(Blocks.KELP_PLANT) && !blockState.isOf(Blocks.SEAGRASS) && !blockState.isOf(Blocks.TALL_SEAGRASS)) {
                            return false;
                        }

                        BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(currentPos) : null;
                        dropStacks(blockState, world, currentPos, blockEntity);
                        world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
                    }

                    return true;
                }
            }
        }) > 1;
    }
    @Override
    protected Item getDefaultItem() {
        return ItemInit.spongeBall;
    }
}
