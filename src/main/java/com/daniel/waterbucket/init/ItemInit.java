package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import com.daniel.waterbucket.item.Meteorite.Meteorite;
import com.daniel.waterbucket.item.MlgWorldConveyor.MlgWorldConveyor;
import com.daniel.waterbucket.item.NearbyPlayerLocator.NearbyPlayerLocator;
import com.daniel.waterbucket.item.SpongeBall.SpongeBall;
import com.daniel.waterbucket.item.SpongeBall.WetSpongeBall;
import com.daniel.waterbucket.item.TankItem.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemInit {
    public static Item meteorite = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"meteorite"),new Meteorite());
    public static Item CannonItem = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"can"),new CannonItem());
    public static Item spongeBall = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"sponge_ball"),new SpongeBall());
    public static Item wetSpongeBall = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"wet_sponge_ball"),new WetSpongeBall());
    public static Item worldConveyor = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"conveyor"),new MlgWorldConveyor());
    public static Item nearbyPlayerLocator = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"locator"),new NearbyPlayerLocator());

    //tank
    public static Item bullet = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet"),new Bullet(new Item.Settings().group(ItemGroup.COMBAT).maxCount(50)));
    public static Item bullet1 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet1"),new Bullet1(new Item.Settings().group(ItemGroup.COMBAT).maxCount(20)));
    public static Item bullet2 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet2"),new Bullet2(new Item.Settings().group(ItemGroup.COMBAT).maxCount(10)));
    public static Item bullet3 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet3"),new Bullet3(new Item.Settings().group(ItemGroup.COMBAT).maxCount(10)));
    public static Item bullet4 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet4"),new Bullet4(new Item.Settings().group(ItemGroup.COMBAT).maxCount(8)));
    public static Item bullet5 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet5"),new Bullet5(new Item.Settings().group(ItemGroup.COMBAT).maxCount(5)));
    public static Item bullet6 = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"bullet6"),new Bullet6(new Item.Settings().group(ItemGroup.COMBAT).maxCount(5)));

    public static Item tank = Registry.register(Registry.ITEM,new Identifier(Waterbucket.MOD_ID,"tank"),new tank());


    public static void init(){
        GeoItemRenderer.registerItemRenderer(tank,new tanrenderer());
    }
}
