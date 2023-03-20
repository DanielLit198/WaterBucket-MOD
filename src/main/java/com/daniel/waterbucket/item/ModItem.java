package com.daniel.waterbucket.item;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import com.daniel.waterbucket.item.GroundWater.GroundWaterBucket;
import com.daniel.waterbucket.item.Meteorite.MeteoriteShower;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import com.daniel.waterbucket.item.Cannon.Cannon;
import com.daniel.waterbucket.item.Shooter.Shooter;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItem {
    public static Item meteoriteShower = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"meteoriteshower"),new MeteoriteShower());
    public static Item meteorite = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"meteorite"),new Meteorite());
    public static Item Cannon = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"cannon"),new Cannon());
    public static Item CannonItem = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"can"),new CannonItem());

    public static Item groundWater = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"groundwater"),new GroundWaterBucket());
    public static Item shooter = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"shooter"),new Shooter());

    public static void init(){
        Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"test"),new TestItem());
    }
}
