package com.daniel.waterbucket.enchantment;

import com.daniel.waterbucket.init.ItemInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.PowerEnchantment;
import net.minecraft.enchantment.PunchEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ChargedEnchantment extends Enchantment {
    public ChargedEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if (other instanceof PowerEnchantment || other instanceof PunchEnchantment) {
            return false;
        }
        return super.canAccept(other);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.getItem() == ItemInit.CannonItem){
            return true;
        }
        return false;
    }
}
