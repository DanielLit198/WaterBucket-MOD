package com.daniel.waterbucket.mixin;

import com.daniel.waterbucket.entity.ModEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Explosion.class)
public abstract class PlayerMixin{
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"),method = "collectBlocksAndDamageEntities")
        public List<Entity> entity(World instance, Entity entity, Box box){
        if (entity instanceof MeteoriteEntity meteorite && meteorite.owner != null){
            return instance.getOtherEntities(meteorite.owner,box);
        }
        return instance.getOtherEntities(entity,box);
    }
}

