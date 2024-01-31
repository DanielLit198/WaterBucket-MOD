package com.daniel.waterbucket.mixin;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ParticleManager.class)
public abstract class ParticleCountMixin {
   /**
    * @author
    * @reason
    */
   @Overwrite
   public void tick() {
      this.particles.forEach((sheet, queue) -> {
         this.world.getProfiler().push(sheet.toString());
         this.tickParticles(queue);
         this.world.getProfiler().pop();
      });
      if (!this.newEmitterParticles.isEmpty()) {
         List<EmitterParticle> list = Lists.newArrayList();
         Iterator<EmitterParticle> var2 = this.newEmitterParticles.iterator();

         while(var2.hasNext()) {
            EmitterParticle emitterParticle = var2.next();
            emitterParticle.tick();
            if (!emitterParticle.isAlive()) {
               list.add(emitterParticle);
            }
         }

         this.newEmitterParticles.removeAll(list);
      }

      Particle particle;
      if (!this.newParticles.isEmpty()) {
         while((particle = this.newParticles.poll()) != null)
            this.particles.computeIfAbsent(particle.getType(), (sheet) -> EvictingQueue.create(300000)).add(particle);
      }

   }
   @Shadow
   @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

   @Shadow protected ClientWorld world;

   @Shadow protected abstract void tickParticles(Collection<Particle> particles);

   @Shadow @Final private Queue<EmitterParticle> newEmitterParticles;

   @Shadow @Final private Queue<Particle> newParticles;

//   @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
//   private void onAddParticle(CallbackInfo ci) {
//      // 检查当前粒子数量，如果超过您的限制，则取消粒子的添加
//      if (this.particles.size() > 50000) {
//         ci.cancel();
//      }
//   }

}
