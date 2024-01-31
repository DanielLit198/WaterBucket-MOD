package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Explosion.class)
public abstract class PlayerMixin{
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"),method = "collectBlocksAndDamageEntities")
        public List<Entity> entity(World instance, Entity entity, Box box){
        if (entity instanceof WaterBucketEntity){
            return instance.getEntitiesByClass(Entity.class, box,entity1 -> false);
        }
        return instance.getOtherEntities(entity,box);
    }
}

