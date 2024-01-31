package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import com.daniel.waterbucket.item.GroundWater.GroundWaterBucket;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import com.daniel.waterbucket.item.MlgWorldConveyor.MlgWorldConveyor;
import com.daniel.waterbucket.item.NearbyPlayerLocator.NearbyPlayerLocator;
import com.daniel.waterbucket.item.SpongeBall.SpongeBall;
import com.daniel.waterbucket.item.SpongeBall.WetSpongeBall;
import com.daniel.waterbucket.item.WaterBuckets.WaterBuckets;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ItemInit {
    public static Item meteorite = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"meteorite"),new Meteorite());
    public static Item CannonItem = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"can"),new CannonItem());
    public static Item WaterBuckets = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"buckets"),new WaterBuckets());
    public static Item GroundWaterBucket = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"ground"),new GroundWaterBucket());

    public static Item spongeBall = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"sponge_ball"),new SpongeBall());
    public static Item wetSpongeBall = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"wet_sponge_ball"),new WetSpongeBall());
    public static Item worldConveyor = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"conveyor"),new MlgWorldConveyor());
    public static Item nearbyPlayerLocator = Registry.register(Registries.ITEM,new Identifier(Waterbucket.MOD_ID,"locator"),new NearbyPlayerLocator());


    public static void init(){

    }
}
