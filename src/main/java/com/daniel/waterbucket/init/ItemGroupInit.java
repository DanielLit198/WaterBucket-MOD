package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupInit {
    public static ItemGroup WATERBUCKET = Registry.register(Registries.ITEM_GROUP,new Identifier(Waterbucket.MOD_ID,"waterbucket"),
            FabricItemGroup.builder().displayName(Text.translatable("item_group.waterbucket"))
                    .icon(() -> new ItemStack(Items.WATER_BUCKET)).entries((displayContext, entries) -> {
                        entries.add(ItemInit.CannonItem);
                        entries.add(ItemInit.WaterBuckets);
                        entries.add(ItemInit.meteorite);
                        entries.add(ItemInit.nearbyPlayerLocator);
                        entries.add(ItemInit.worldConveyor);
                        entries.add(ItemInit.spongeBall);
                        entries.add(ItemInit.wetSpongeBall);
                    }).build());


    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
        });
    }
}
