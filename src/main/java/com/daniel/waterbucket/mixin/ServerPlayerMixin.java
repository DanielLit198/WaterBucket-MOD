package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.init.DimensionInit;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Shadow @Final private TextStream textStream;

    @Inject(method = "onDeath", at = @At("RETURN"))
    public void death(DamageSource damageSource, CallbackInfo ci){
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld serverWorld = player.getServer().getWorld(World.OVERWORLD);
        if (player.getWorld().getDimensionKey() == DimensionInit.MLG_TYPE_KEY) {
            if (damageSource.getAttacker() != null && damageSource.getAttacker().isPlayer()){
                player.setSpawnPoint(serverWorld.getRegistryKey(), serverWorld.getSpawnPos(), 1, true, false);
                player.sendMessage(Text.of("你输掉了比赛"), false);
            }else if (player.getSpawnPointDimension() != DimensionInit.MLG_DIMENSION_KEY){
                player.sendMessage(Text.of("你输掉了比赛"), false);
            }
        }
    }

}
