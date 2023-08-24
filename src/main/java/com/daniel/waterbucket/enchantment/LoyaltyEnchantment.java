package com.daniel.waterbucket.enchantment;

import com.daniel.waterbucket.item.Cannon.CannonItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

public abstract class LoyaltyEnchantment extends Enchantment {

    protected LoyaltyEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxPower(int level) {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.getItem() instanceof CannonItem) {
            return true;
        }
        return false;
    }
}
