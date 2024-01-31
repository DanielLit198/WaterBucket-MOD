package com.daniel.waterbucket.item.WaterBuckets;

import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WaterBuckets extends Item implements GeoItem, FluidModificationItem {
    private final Fluid fluid = Fluids.WATER;
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public WaterBuckets() {
        super(new Settings().maxCount(1));

    }
    public ItemStack getWaterBucket(PlayerEntity user){
        for(int i = 0; i < user.getInventory().size(); ++i) {
            ItemStack bucket = user.getInventory().getStack(i);
            if (bucket.getItem() == Items.WATER_BUCKET || bucket.getItem() == Items.BUCKET
            || bucket.getItem() == ItemInit.meteorite){
                return bucket;
            }
        }
        return null;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.hasNbt()) {
            if (stack.getNbt().getInt("count") <= 0){
                stack.decrement(1);
            }
        }
        //设置nbt
        if (!stack.hasNbt()){
            stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("count", 4);
            stack.getNbt().putInt("meteorite",0);
            stack.getNbt().putInt("bucket",4);
            stack.getNbt().putInt("water", 4);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (itemStack.hasNbt()) {
            if (user.isSneaking()) {
                ItemStack waterBucket = getWaterBucket(user);
                if (getWaterBucket(user) != null) {
                    if (waterBucket.getItem() == Items.WATER_BUCKET) {
                        itemStack.getNbt().putInt("count", itemStack.getNbt().getInt("count") + 1);
                        itemStack.getNbt().putInt("water", itemStack.getNbt().getInt("water") + 1);
                        itemStack.getNbt().putInt("bucket", itemStack.getNbt().getInt("bucket") + 1);
                        user.sendMessage(Text.of("水桶：[" + itemStack.getNbt().getInt("water") + " / " + itemStack.getNbt().getInt("bucket") + "]"),true);
                    }else if (waterBucket.getItem() == Items.BUCKET){
                        itemStack.getNbt().putInt("bucket", itemStack.getNbt().getInt("bucket") + 1);
                        itemStack.getNbt().putInt("count", itemStack.getNbt().getInt("count") + 1);
                        user.sendMessage(Text.of("水桶：[" + itemStack.getNbt().getInt("water") + " / " + itemStack.getNbt().getInt("bucket") + "]"),true);
                    }else if (waterBucket.getItem() == ItemInit.meteorite){
                        itemStack.getNbt().putInt("meteorite", itemStack.getNbt().getInt("meteorite") + 1);
                        itemStack.getNbt().putInt("count", itemStack.getNbt().getInt("count") + 1);
                        user.sendMessage(Text.of("陨石水桶：[" + itemStack.getNbt().getInt("meteorite") + "]"),true);

                    }
                    world.playSound(user,user.getBlockPos(),SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW,SoundCategory.BLOCKS,1.0F,1.0F);
                    waterBucket.decrement(1);
                    return TypedActionResult.success(itemStack);
                }
            }

            if (itemStack.getNbt().getInt("water") <= 0 && itemStack.getNbt().getInt("meteorite") > 0){
                Meteorite.spawn(world,user,hand,0,raycast(world, user,itemStack,RaycastContext.FluidHandling.SOURCE_ONLY).getBlockPos());
                itemStack.getNbt().putInt("meteorite", itemStack.getNbt().getInt("meteorite") - 1);
                itemStack.getNbt().putInt("count", itemStack.getNbt().getInt("count") - 1);
                user.sendMessage(Text.of("陨石水桶：[" + itemStack.getNbt().getInt("meteorite") + "]"),true);
                user.playSound(SoundEvents.ENTITY_ITEM_BREAK,1,1);
                return TypedActionResult.success(itemStack, world.isClient());
            }

            BlockHitResult blockHitResult = raycast(world, user,itemStack,RaycastContext.FluidHandling.SOURCE_ONLY);
            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return TypedActionResult.pass(itemStack);
            } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
                return TypedActionResult.pass(itemStack);
            } else {
                BlockPos blockPos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getSide();
                BlockPos blockPos2 = blockPos.offset(direction);
                if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos2, direction, itemStack) ) {
                    BlockState blockState;
                    if (itemStack.getNbt().getInt("count") > itemStack.getNbt().getInt("water")) {
                        blockState = world.getBlockState(blockPos);
                        if (blockState.getBlock() instanceof FluidDrainable && blockState.getFluidState().getFluid() != Fluids.LAVA) {
                            FluidDrainable fluidDrainable = (FluidDrainable) blockState.getBlock();
                            ItemStack itemStack2 = fluidDrainable.tryDrainFluid(world, blockPos, blockState);
                            if (!itemStack2.isEmpty()) {
                                fluidDrainable.getBucketFillSound().ifPresent((sound) -> {
                                    user.playSound(sound, 1.0F, 1.0F);
                                });
                                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                            }
                            if(itemStack.getNbt().getInt("count") > itemStack.getNbt().getInt("water"))
                                itemStack.getNbt().putInt("water", itemStack.getNbt().getInt("water") + 1);
                                user.sendMessage(Text.of("水桶：[" + itemStack.getNbt().getInt("water") + " / " + itemStack.getNbt().getInt("bucket") + "]"),true);
                            return TypedActionResult.success(itemStack, world.isClient());
                        }
                    }
                    if (itemStack.getNbt().getInt("water") > 0) {
                        blockState = world.getBlockState(blockPos);
                        BlockPos blockPos3 = blockState.getBlock() instanceof FluidFillable && this.fluid == Fluids.WATER ? blockPos : blockPos2;
                        if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                            this.onEmptied(user, world, itemStack, blockPos3);
                            if (user instanceof ServerPlayerEntity) {
                                Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, blockPos3, itemStack);
                            }
                            if (itemStack.getNbt().getInt("water") > 0) {
                                itemStack.getNbt().putInt("water", itemStack.getNbt().getInt("water") - 1);
                                user.sendMessage(Text.of("水桶：[" + itemStack.getNbt().getInt("water") + " / " + itemStack.getNbt().getInt("bucket") + "]"), true);
                            }
                            return TypedActionResult.success(itemStack, world.isClient());
                        } else {
                            return TypedActionResult.fail(itemStack);
                        }
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }
    protected static BlockHitResult raycast(World world, PlayerEntity player,ItemStack stack,RaycastContext.FluidHandling fluidHandling) {
        if (stack.hasNbt()) {
            float f = player.getPitch();
            float g = player.getYaw();
            Vec3d vec3d = player.getEyePos();
            float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
            float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
            float j = -MathHelper.cos(-f * 0.017453292F);
            float k = MathHelper.sin(-f * 0.017453292F);
            float l = i * j;
            float n = h * j;

            double d = stack.getNbt().getInt("count") + 5;
            Vec3d vec3d2 = vec3d.add((double) l * d, (double) k * d, (double) n * d);
            return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
        }
        return null;
    }
    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(player.getStackInHand(Hand.MAIN_HAND).hasNbt() && player.getStackInHand(Hand.MAIN_HAND).getNbt().getInt("water") >= 0)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            boolean bl = blockState.canBucketPlace(this.fluid);
            boolean bl2 = blockState.isAir() || bl || block instanceof FluidFillable && ((FluidFillable)block).canFillWithFluid(world, pos, blockState, this.fluid);
            if (!bl2) {
                return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), (BlockHitResult)null);
            } else if (world.getDimension().ultrawarm() && this.fluid.isIn(FluidTags.WATER)) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    world.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0, 0.0, 0.0);
                }

                return true;
            } else if (block instanceof FluidFillable && this.fluid == Fluids.WATER) {
                ((FluidFillable)block).tryFillWithFluid(world, pos, blockState, ((FlowableFluid)this.fluid).getStill(false));
                this.playEmptyingSound(player, world, pos);
                return true;
            } else {
                if (!world.isClient && bl && !blockState.isLiquid()) {
                    world.breakBlock(pos, true);
                }

                if (!world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), 11) && !blockState.getFluidState().isStill()) {
                    return false;
                } else {
                    this.playEmptyingSound(player, world, pos);
                    return true;
                }
            }
        }
    }

    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = this.fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.hasNbt()) {
            tooltip.add(Text.of("桶：["+stack.getNbt().getInt("count")+"]"));
            tooltip.add(Text.of("水桶：[" + stack.getNbt().getInt("water") + " / " + stack.getNbt().getInt("bucket") + "]"));
            if (stack.getNbt().getInt("meteorite") > 0)
            tooltip.add(Text.of("陨石水桶：[" + stack.getNbt().getInt("meteorite")+"]"));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private WaterBucketsRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new WaterBucketsRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }


}
