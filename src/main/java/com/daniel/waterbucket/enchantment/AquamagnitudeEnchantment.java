package com.daniel.waterbucket.enchantment;

import com.daniel.waterbucket.item.Cannon.CannonItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class AquamagnitudeEnchantment extends Enchantment {
    protected AquamagnitudeEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.getItem() instanceof CannonItem) {
            return true;
        }
        return false;
    }
}
