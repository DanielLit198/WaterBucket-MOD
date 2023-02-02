package com.daniel.waterbucket.entity;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntity {
    public static final EntityType<MeteoriteEntity> WATER_BALL = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"meteorite"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MeteoriteEntity::new)
                    .dimensions(EntityDimensions.fixed(1f,1f)).build());

    public static final EntityType<WaterBucketEntity> WATER_BUCKET_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucket"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, WaterBucketEntity::new)
                    .dimensions(EntityDimensions.fixed(2f,2f)).build());

}
