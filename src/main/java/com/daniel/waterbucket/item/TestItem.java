package com.daniel.waterbucket.item;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class TestItem extends Item {
    //圆心坐标
    double a;
    double b;
    double c;
    //半径
    double r;
    //法向量
    double vx;
    double vy;
    double vz;

    public TestItem() {
        super(new Settings().group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        a = user.getX();
        b = user.getY();
        c = user.getZ();
        r = 4;
        vx = user.getRotationVector().getX();
        vy = user.getRotationVector().getY();
        vz = user.getRotationVector().getZ();
        double[] n = {vx,vy,vz};
        double[] u = {n[1],-n[0],0};
        double[] v = {(n[1] * u[2])-(n[2] * u[1]),
                (n[2] * u[0])-(n[0] * u[2]),
                (n[0] * u[1])-(n[1] * u[0]),
        };
        //单位化向量:
        double us = Math.sqrt((u[0]*u[0])+(u[1]*u[1])+(u[2]*u[2]));
        double vs = Math.sqrt((v[0]*v[0])+(v[1]*v[1])+(v[2]*v[2]));
        double[] uu = {u[0]/us,u[1]/us,0};
        double[] vv = {v[0]/vs,v[1]/vs,v[2]/vs};
        /*
        x=x_0+r(cos t)(d/|u|)+r(sin t)(b f-c e)/|v|
        y=y_0+r(cos t)(e/|u|)+r(sin t)(c d-a f)/|v|
        z=z_0+r(cos t)(f/|u|)+r(sin t)(a e-b d)/|v|
         */
        for (int t = 0; t < 60; t++) {
            double x = a + r * Math.cos(t) * uu[0] + r * Math.sin(t) * vv[0];
            double y = b + r * Math.cos(t) * uu[1] + r * Math.sin(t) * vv[1];
            double z = c + r * Math.cos(t) * uu[2] + r * Math.sin(t) * vv[2];

            world.addParticle(ParticleTypes.CLOUD,x,y,z,0,0,0);
        }


        return super.use(world, user, hand);
    }


}



