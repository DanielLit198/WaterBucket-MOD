package com.daniel.waterbucket.entity.tank;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TankEntity extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public TankEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void travel(Vec3d movementInput) {
        LivingEntity livingentity = (LivingEntity) this.getFirstPassenger();;
        if (livingentity != null) {
            this.setYaw(livingentity.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(livingentity.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), 0);
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;
            float f = livingentity.sidewaysSpeed * 0.5F;
            float g = livingentity.forwardSpeed;
            if (g <= 0.0F) {
                g *= 0.25F;
            }
            this.setMovementSpeed(0.3F);
            super.travel(new Vec3d(f, movementInput.y, g));
        }
        BlockPos pos = this.getBlockPos().offset(this.getMovementDirection());
        if (world.getBlockState(pos).isSolidBlock(world, pos))this.addVelocity(0,0.2,0);
        super.travel(movementInput);
    }

    @Override
    public void tick() {
        if (this.getPassengerList() != null && this.getFirstPassenger() instanceof PlayerEntity player)
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5, 100, false, false), player);
        super.tick();
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (!player.shouldCancelInteraction()) {
            if (!world.isClient()) player.startRiding(this);
        }
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isFromFalling())return false;
        return super.damage(source, amount);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (this.getPassengerList() != null && this.getFirstPassenger() instanceof PlayerEntity player) player.kill();
        super.onDeath(damageSource);
    }

    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
