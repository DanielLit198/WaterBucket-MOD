package com.daniel.waterbucket.event;

import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.init.ItemInit;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class ChargingInterruption {
    public static void event(){
//        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
//            if (entity instanceof PlayerEntity hitPlayer)
//                if (hitPlayer.getItemUseTime() > 0 && hitPlayer.getStackInHand(Hand.MAIN_HAND).getItem() instanceof CannonItem || hitPlayer.getStackInHand(Hand.OFF_HAND).getItem() instanceof CannonItem){
//                hitPlayer.stopUsingItem();
//
//            }
//            return ActionResult.PASS;
//        });
    }
}
