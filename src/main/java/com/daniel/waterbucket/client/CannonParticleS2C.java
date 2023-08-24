package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.ParticleInit;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class CannonParticleS2C {
    public static void init(){
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Waterbucket.MOD_ID, "particle4"), (client, handler, buf, responseSender) -> {
            int[] data = buf.readIntArray();
            double x1 = data[0];
            double y1 = data[1];
            double z1 = data[2];
            double x2 = data[3];
            double y2 = data[4];
            double z2 = data[5];
            client.execute(() -> {
                client.world.addParticle(ParticleTypes.CLOUD, true, x1/100000,y1/100000,z1/100000,
                        (x2/100000 - x1/100000)/20,(y2/100000 - y1/100000)/20,(z2/100000 - z1/100000)/20);

            });
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Waterbucket.MOD_ID, "particle2"), (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            double size = buf.readDouble();
            client.execute(() -> {
                for (int l = 0; l < 100; l++) {
                    Random q = new Random();
                    Random k = new Random();
                    Random j = new Random();
                    Random p = new Random();
                    Random o = new Random();
                    Random u = new Random();
                    double a = Math.sqrt(size) * q.nextGaussian() + 0;
                    double b = Math.sqrt(size) * k.nextGaussian() + 0;
                    double c = Math.sqrt(size) * j.nextGaussian() + 0;
                    double e = Math.sqrt(size) * p.nextGaussian() + 0;
                    double t = Math.sqrt(size) * o.nextGaussian() + 0;
                    double r = Math.sqrt(size) * u.nextGaussian() + 0;
                    client.world.addParticle(ParticleInit.Water, true, pos.getX() + a, pos.getY() + b, pos.getZ() + c, 0, 0, 0);
                    client.world.addParticle(ParticleTypes.CLOUD, true, pos.getX() + e, pos.getY() + t, pos.getZ() + r, 0, 0, 0);

                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Waterbucket.MOD_ID, "particle3"), (client, handler, buf, responseSender) -> {
            int[] pos = buf.readIntArray();
            double a = pos[0];
            double b = pos[1];
            double c = pos[2];
            client.execute(() -> {
                client.world.addParticle(ParticleTypes.CLOUD, true,a/100000,b/100000,c/100000, 0, 0, 0);
            });
        });

    }



}
