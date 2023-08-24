package com.daniel.waterbucket.world.gen;

import com.daniel.waterbucket.init.EntityInit;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class EntitySpawn {
    public static void spawn(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.THE_VOID), SpawnGroup.AMBIENT,
                EntityInit.CHEST_ENTITY,100,2,5);

        SpawnRestriction.register(EntityInit.CHEST_ENTITY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }
}
