package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.enchantment.ChargedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class EnchantmentInit {
    public static Enchantment charged = Registry.register(Registry.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"charged"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));
    public static Enchantment loyalty = Registry.register(Registry.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"loyalty"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));

    public static Enchantment rapid = Registry.register(Registry.ENCHANTMENT, new Identifier(Waterbucket.MOD_ID,"rapid"),
            new ChargedEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND));

    public static void init(){

    }
}
