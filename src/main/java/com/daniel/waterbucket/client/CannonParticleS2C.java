package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.ParticleInit;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

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
            Vector3f pos = buf.readVector3f();
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
                    client.world.addParticle(ParticleInit.Water,  true, pos.x() + a, pos.y() + b, pos.z() + c, 0, 0, 0);
                    client.world.addParticle(ParticleTypes.CLOUD, true, pos.x() + e, pos.y() + t, pos.z() + r, 0, 0, 0);

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

        //地下水
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Waterbucket.MOD_ID, "ground"), (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            Vector3f f = buf.readVector3f();
            float times = buf.readFloat();
            int counts = buf.readInt();
            client.execute(() -> {
                double x = f.x * times * 1;
                double z = f.z * times * 1;
                //o:水柱重复次数
                for (int o = 0; o < counts; o++) {
                   //i:水柱高度
                    for (int i = 0; i < 20; i++) {
                        int range = 1 + (i/2);
                        //j:水粒子数量
                        for (int j = 0; j < 100; j++) {
                            Random e = new Random();
                            Random w = new Random();
                            double a = Math.sqrt(range) * e.nextGaussian() + 0;
                            double b = Math.sqrt(range) * w.nextGaussian() + 0;

                            int v = new Random().nextInt(2) == 1 ? 1 : -1;
                            int n = new Random().nextInt(2) == 1 ? 1 : -1;
                            double m = new Random().nextDouble(0.3 ) * v;
                            double k = new Random().nextDouble(0.3 ) * n;

                            double y =  new Random().nextDouble(5);
                            double py = new Random().nextDouble(5);
                            if (i >= 18) {
                                for (int l = 0; l < 5; l++) {
                                                  client.world.addParticle(ParticleInit.Cloud, true, pos.getX() + a + x, pos.getY() + py, pos.getZ() + b + z, m * i * 0.1, i * (0.1 + (y * 0.06)) * 0.9, k * i * 0.1);
                                }
                            }else client.world.addParticle(ParticleInit.WaterBucket, true,pos.getX() + a + x + 0.5, pos.getY() + py, pos.getZ() + b + z + 0.5, m * i * 0.1, i * (0.1 + (y * 0.05)), k * i * 0.1);
                        }
                        for (int j = 0; j < 80; j++) {
                            Random e = new Random();
                            Random w = new Random();
                            double a = Math.sqrt(range) * e.nextGaussian() + 0;
                            double b = Math.sqrt(range) * w.nextGaussian() + 0;

                            int v = new Random().nextInt(2) == 1 ? 1 : -1;
                            int n = new Random().nextInt(2) == 1 ? 1 : -1;
                            double m = (new Random().nextDouble(0.5) + 0.1) * v;
                            double k = (new Random().nextDouble(0.5) + 0.1) * n;
                            int r = i;
                            if (r <= 10) r = 18;
                            double y = new Random().nextDouble(5);
                            client.world.addParticle(ParticleInit.WaterBucket, true,pos.getX() + a + x + 0.5, pos.getY(), pos.getZ() + b + z + 0.5, m, r * (0.1 + (y * 0.05)), k);
                        }
                    }
                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < 120; i++) {
                            double y = new Random().nextDouble(30);
                            double r = 1 + new Random().nextInt(3) + y * 2;
                            double wc = new Random().nextDouble(2);
                            double cc = new Random().nextDouble(2);
                            double wa = wc * Math.PI;
                            double ca = cc * Math.PI;
                            double xw = pos.getX() + r * Math.cos(wa);
                            double zw = pos.getZ() + r * Math.sin(wa);
                            double xc = pos.getX() + r * Math.cos(ca);
                            double zc = pos.getZ() + r * Math.sin(ca);

                            double q = 0.04;
                            client.world.addParticle(ParticleInit.WaterBucket, true,pos.getX() + x,pos.getY()+1,pos.getZ() + z,(xw - pos.getX()) * q,y *0.08*j*0.5,(zw - pos.getZ()) * q);
                                  client.world.addParticle(ParticleInit.Cloud, true,pos.getX() + x,pos.getY()+1,pos.getZ() + z,(xc - pos.getX()) * q,y *0.08*j*0.5,(zc - pos.getZ()) * q);
                        }
                    }
                }
            });
        });
    }



}
