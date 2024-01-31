package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.enchantment.ChargedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EnchantmentInit {
    public static Enchantment charged = Registry.register(Registries.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"charged"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));
    public static Enchantment loyalty = Registry.register(Registries.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"loyalty"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));

    public static Enchantment rapid = Registry.register(Registries.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"rapid"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));
    public static Enchantment aquamagnitude = Registry.register(Registries.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"aquamagnitude"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));

    public static void init(){

    }
}
