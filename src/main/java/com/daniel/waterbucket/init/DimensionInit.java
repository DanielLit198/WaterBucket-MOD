package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimensionInit {
    public static final RegistryKey<World> MLG_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY,new Identifier(Waterbucket.MOD_ID, "mlg"));
    public static final RegistryKey<DimensionType> MLG_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,MLG_DIMENSION_KEY.getValue());

    public static void init(){

    }
}
