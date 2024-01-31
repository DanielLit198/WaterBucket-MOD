package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.entity.chest.ChestEntity;
import com.daniel.waterbucket.entity.spongeball.SpongeBallEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityInit {
    public static final EntityType<ChestEntity> CHEST_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"chest"),
            FabricEntityTypeBuilder.<ChestEntity>create(SpawnGroup.AMBIENT,ChestEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f,0.8f)).build());
    public static final EntityType<MeteoriteEntity> WATER_BALL = Registry.register(
            Registries.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"meteorite"),
            FabricEntityTypeBuilder.<MeteoriteEntity>create(SpawnGroup.MISC, MeteoriteEntity::new)
                    .dimensions(EntityDimensions.fixed(5f,5f)).trackRangeBlocks(64).trackedUpdateRate(10).build());
    public static final EntityType<WaterBucketEntity> WATER_BUCKET_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucket"),
            FabricEntityTypeBuilder.<WaterBucketEntity>create(SpawnGroup.MISC,WaterBucketEntity::new)
                    .dimensions(EntityDimensions.fixed(1f,1f)).trackRangeBlocks(64).trackedUpdateRate(10).build());
    public static final EntityType<SpongeBallEntity> SPONGE_BALL = Registry.register(
            Registries.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"sponge_ball"),
            FabricEntityTypeBuilder.<SpongeBallEntity>create(SpawnGroup.MISC,SpongeBallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f,0.25f)).trackRangeBlocks(64).trackedUpdateRate(10).build());

    public static void init(){
        FabricDefaultAttributeRegistry.register(CHEST_ENTITY, MobEntity.createMobAttributes());



    }
}
