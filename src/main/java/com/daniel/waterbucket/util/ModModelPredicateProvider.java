package com.daniel.waterbucket.util;

import com.daniel.waterbucket.init.EnchantmentInit;
import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerBow(ItemInit.CannonItem);
        registerSpongeBall(ItemInit.spongeBall);
    }
    private static void registerSpongeBall(Item ball){
        FabricModelPredicateProviderRegistry.register(ball,new Identifier("wet"),
                (stack, world, entity, seed) -> {
                    if (stack.hasNbt() && stack.getNbt().getBoolean("wet")) {
                        return 1;
                    }
                    return 0;
                });
    }

    private static void registerBow(Item bow) {
        FabricModelPredicateProviderRegistry.register(bow, new Identifier("pull"),
                (stack,world,entity,seed) -> {
                if (entity == null) {
                    return 0.0f;
                }
                if (entity.getActiveItem() != stack) {
                    return 0.0f;
                }
                int level = EnchantmentHelper.getLevel(EnchantmentInit.charged, stack);
                int level2 = EnchantmentHelper.getLevel(EnchantmentInit.rapid,stack);
                //o是快速射击
                float o = 0;
                if (level2 == 1){
                    o = 45f;
                } else if (level2 == 2) {
                    o = 75f;
                }else if (level2 == 3){
                    o = 100f;
                }
                //i是充能
                float i = 20 - o;
                if (level == 1) {
                    i = 50.0f - o;
                }else if (level == 2){
                    i = 80.0f - o;
                }else if (level == 3){
                    i = 105.0f - o;
                }
                if (i < 5) i = 5;
                return (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / i;
            });

        FabricModelPredicateProviderRegistry.register(bow, new Identifier("pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }
}