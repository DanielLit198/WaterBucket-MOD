package com.daniel.waterbucket;

import com.daniel.waterbucket.event.ChargingInterruption;
import com.daniel.waterbucket.init.*;
import com.daniel.waterbucket.entity.cannon.WaterBucketRenderer;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteRenderer;
import com.daniel.waterbucket.mixin.ParticleCountMixin;
import com.daniel.waterbucket.mixin.ServerPlayNetworkHandlerMixin;
import com.daniel.waterbucket.particle.WaterDropParticle;
import com.daniel.waterbucket.particle.WaterParticle;
import com.daniel.waterbucket.world.gen.EntitySpawn;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class Waterbucket implements ModInitializer {

    public static final String MOD_ID = "waterbucket";

    @Override
    public void onInitialize() {
        ItemGroupInit.init();
        ItemInit.init();
        EntityInit.init();
        ParticleInit.init();
        EnchantmentInit.init();
        DimensionInit.init();
        EntitySpawn.spawn();




    }
}
