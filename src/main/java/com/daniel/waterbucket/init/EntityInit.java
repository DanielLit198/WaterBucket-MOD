package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.entity.cannon.WaterBucketEntity;
import com.daniel.waterbucket.entity.cannon.WaterBucketRenderer;
import com.daniel.waterbucket.entity.chest.ChestEntity;
import com.daniel.waterbucket.entity.chest.ChestEntityRenderer;
import com.daniel.waterbucket.entity.spongeball.SpongeBallEntity;
import com.daniel.waterbucket.entity.tank.TankEntity;
import com.daniel.waterbucket.entity.tank.bulletEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteEntity;
import com.daniel.waterbucket.entity.waterball.MeteoriteRenderer;
import com.daniel.waterbucket.item.SpongeBall.SpongeBall;
import com.daniel.waterbucket.item.TankItem.Bullet;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityInit {
    public static final EntityType<ChestEntity> CHEST_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"chest"),
            FabricEntityTypeBuilder.<ChestEntity>create(SpawnGroup.AMBIENT,ChestEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f,0.8f)).build());
    public static final EntityType<TankEntity> TANK = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"tank"),
            FabricEntityTypeBuilder.<TankEntity>create(SpawnGroup.MISC,TankEntity::new)
                    .dimensions(EntityDimensions.fixed(1f,1f)).build());
    public static final EntityType<MeteoriteEntity> WATER_BALL = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"meteorite"),
            FabricEntityTypeBuilder.<MeteoriteEntity>create(SpawnGroup.MISC, MeteoriteEntity::new)
                    .dimensions(EntityDimensions.fixed(3f,3f)).trackRangeBlocks(64).trackedUpdateRate(10).build());
    public static final EntityType<WaterBucketEntity> WATER_BUCKET_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"waterbucket"),
            FabricEntityTypeBuilder.<WaterBucketEntity>create(SpawnGroup.MISC,WaterBucketEntity::new)
                    .dimensions(EntityDimensions.fixed(1f,1f)).trackRangeBlocks(64).trackedUpdateRate(10).build());
    public static final EntityType<SpongeBallEntity> SPONGE_BALL = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"sponge_ball"),
            FabricEntityTypeBuilder.<SpongeBallEntity>create(SpawnGroup.MISC,SpongeBallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f,0.25f)).trackRangeBlocks(64).trackedUpdateRate(10).build());
    //tank
    public static final EntityType<bulletEntity> bullet = Registry.register(
            Registry.ENTITY_TYPE,new Identifier(Waterbucket.MOD_ID,"bullet"),
            FabricEntityTypeBuilder.<bulletEntity>create(SpawnGroup.MISC, bulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f,0.25f)).trackRangeBlocks(64).trackedUpdateRate(10).build());

    public static void init(){
        FabricDefaultAttributeRegistry.register(CHEST_ENTITY, MobEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(TANK, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,150));


    }
}
