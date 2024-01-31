package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimensionInit {
    public static final RegistryKey<World> MLG_DIMENSION_KEY = RegistryKey.of(RegistryKeys.WORLD,new Identifier(Waterbucket.MOD_ID, "mlg"));
    public static final RegistryKey<DimensionType> MLG_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,MLG_DIMENSION_KEY.getValue());

    public static void init(){

    }
}
