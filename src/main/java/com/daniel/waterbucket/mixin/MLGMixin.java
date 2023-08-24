package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.init.DimensionInit;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class MLGMixin {
    @Shadow @Final private PlayerInventory inventory;

    @Shadow public abstract boolean isPushedByFluids();

    private boolean isTeleporting = false;
    private long lastTeleportTime = 0;
    private static final long TELEPORT_COOLDOWN = 5; // Adjust this value to set the cooldown time in ticks (1 second = 20 ticks)
    private boolean countdownActive = false;
    private int remainingTime = 0;
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getEntityWorld() instanceof ServerWorld && player.getWorld().getDimensionKey() == DimensionInit.MLG_TYPE_KEY) {
            if (player.getY() < -100) {
                long currentTime = player.world.getTime();
                if (currentTime - lastTeleportTime >= TELEPORT_COOLDOWN) {
                    Random random = new Random();
                    Vec3d teleportPos = new Vec3d(player.getX() + random.nextDouble(200), 250, player.getZ() + random.nextDouble(200));
                    player.teleport(teleportPos.x, teleportPos.y, teleportPos.z);
                    lastTeleportTime = currentTime;
                }
            }
            if (player.getY() >= 240) {
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100000, 0, false, false), player);
            }
            if (player.hasStatusEffect(StatusEffects.SLOW_FALLING) && player.isOnGround()) {
                player.removeStatusEffect(StatusEffects.SLOW_FALLING);
            }
            if (player.getStatusEffect(StatusEffects.NIGHT_VISION) == null) {
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 100000, 1, false, false), player);
            }
            if (!player.isCreative() && player.getWorld().getPlayers().size() <= 1){
                if(!player.isDead()) {
                    player.sendMessage(Text.of("你赢得了比赛"), false);
                    if (player instanceof ServerPlayerEntity server)
                        server.teleport(server.getServer().getWorld(World.OVERWORLD),
                                server.getWorld().getSpawnPos().getX(),
                                server.getWorld().getSpawnPos().getY(),
                                server.getWorld().getSpawnPos().getZ(),
                                server.getYaw(), server.getPitch());
                }
            }
        }
    }
}

