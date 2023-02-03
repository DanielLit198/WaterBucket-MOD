package com.daniel.waterbucket.item;

import com.daniel.waterbucket.client.WaterbucketClient;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

public class TestItem extends Item {
    int i = 0;

    public TestItem() {
        super(new FabricItemSettings().group(ItemGroup.MISC));
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        double a = 0;
//        double b = 0;
//
//        double r = 2;
//        double fr = 3;
//        for (int i = 0; i < 3; i++) {
//            b = b + 0.1;
//            r = r + b;
//            fr = fr + b;
//            for (int j = 0; j < 360; j++) {
//                a = a + 0.01 * Math.PI;
//                double x = user.getX() + r * Math.cos(a);
//                double z = user.getZ() + r * Math.sin(a);
//
//                double fx = user.getX() + fr * Math.cos(a);
//                double fz = user.getZ() + fr * Math.sin(a);
//                world.addParticle(ParticleTypes.CLOUD,true, x, user.getY() + 2, z, fx - x, 0, fz - z);
//            }
//        }
        this.draw(world, user);
        return super.use(world, user, hand);
    }

    public void draw(World world, PlayerEntity player) {
        //粗细的机制：增加固定数量的粒子线，给粒子线的每个粒子随机变动，就可以指定粗细了
        Random random = new Random();
        Vec3d velocity = player.getRotationVector();
        double length = 100; //长度
        double width = 1; //粗细
        double validLength = length; //实际长度，在半路触发了，那这个点就是实际长度
        Vec3d basePos = player.getPos().add(0, 2, 0); //为了测试，增加高度，实体需要改
        //循环出数条粒子线，第一条是直线，之后都会随机变动
        for (int k = 0; k < 30 * width; k++) {
            Vec3d pos = basePos;
            for (int j = 0; j < length; j++) {
                if (k == 0) {
                    if(j % 10 ==0){
                        //圈圈
                        for (int i = 0; i < 360; i++) {
                            //半径 半径越大 速度越快
                            double r = (double) j/30;
                            double x = Math.sin(i) * r;
                            double z = Math.cos(i) * r;
                            world.addParticle(ParticleTypes.CLOUD, true, pos.getX(), pos.getY(), pos.getZ(), x, 0, z);

                        }
                    }
                    BlockPos blockPos = new BlockPos(pos);
                    if (!world.isAir(blockPos)) {
                        BlockState blockState = world.getBlockState(blockPos);
//                        //这里用破坏方块测试，可以换
//                        blockState.getBlock().onBreak(world, blockPos, blockState, player);
//                        world.breakBlock(blockPos, true);
                        Explosion explosion = new Explosion(world,player,pos.getX(),pos.getY()-5,pos.getZ(),30,false, Explosion.DestructionType.BREAK);
                        explosion.collectBlocksAndDamageEntities();
                        validLength = j;
                    }
                }
                if (validLength >j) {
                    world.addParticle(WaterbucketClient.Water, true, pos.x, pos.y, pos.z, 0, 0, 0);
                } else if (validLength == j) {
                    world.addParticle(ParticleTypes.POOF, true, pos.x, pos.y, pos.z, 0, 0, 0);
                    break;
                }
                pos = pos.add(velocity).add((random.nextDouble() - 0.5) * width, (random.nextDouble() - 0.5) * width, (random.nextDouble() - 0.5) * width);
            }
        }
    }
}
// 圆形
//        for (int j = 0; j < 360; j++) {[谷歌接口报错]：
//        //半径 半径越大 速度越快
//        double r = 1;
//        double x = Math.sin(j)*r;
//        double z = Math.cos(j)*r;
//        world.addParticle(ParticleTypes.CLOUD,true, user.getX(), user.getY(), user.getZ(), x, 0, z);
//        }