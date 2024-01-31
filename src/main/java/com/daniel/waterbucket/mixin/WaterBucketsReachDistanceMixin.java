package com.daniel.waterbucket.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)

public class WaterBucketsReachDistanceMixin {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getReachDistance() {
        return 64.0F; // 默认选中方块的距离，根据需要进行调整

    }
}
