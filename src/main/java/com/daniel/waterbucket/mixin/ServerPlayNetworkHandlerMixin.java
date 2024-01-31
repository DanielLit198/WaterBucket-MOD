package com.daniel.waterbucket.mixin;

import net.fabricmc.fabric.mixin.networking.accessor.ServerPlayNetworkHandlerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)

public class ServerPlayNetworkHandlerMixin {
/**
 * @author
 * @reason
 */
//    @Overwrite
//    protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
//        float f = player.getPitch();
//        float g = player.getYaw();
//        Vec3d vec3d = player.getEyePos();
//        float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
//        float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
//        float j = -MathHelper.cos(-f * 0.017453292F);
//        float k = MathHelper.sin(-f * 0.017453292F);
//        float l = i * j;
//        float n = h * j;
//        double d = 64.0;
//        Vec3d vec3d2 = vec3d.add((double)l * d, (double)k * d, (double)n * d);
//        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
//    }



}
