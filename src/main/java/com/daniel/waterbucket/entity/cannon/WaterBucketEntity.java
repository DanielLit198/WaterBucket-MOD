package com.daniel.waterbucket.entity.cannon;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.client.WaterbucketClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WaterBucketEntity extends MobEntity implements IAnimatable {
    public PlayerEntity player;
    public ItemStack item;
    public boolean sure;
    public int age;
    public Identifier identifier = new Identifier(Waterbucket.MOD_ID,"sure");
    public ArrayList<BlockPos> pos = new ArrayList<>();
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public WaterBucketEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("age",age);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        age = nbt.getInt("age");
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.updateVelocity(0.1f, movementInput);
        if (player != null) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            double xR = player.getRotationVector().getX();
            double yR = player.getRotationVector().getY();
            double zR = player.getRotationVector().getZ();
            double a = Math.acos(zR / Math.sqrt(xR * xR + zR * zR));
            if (-xR < 0) a = a * -1;
            refreshPositionAndAngles(x + 2 * Math.cos(a + 0.25 * Math.PI), y + yR + 1, z + 2 * Math.sin(a + 0.25 * Math.PI), 0, player.getPitch());
            lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(x + xR * 100, y + 1.2 + yR * 100, z + zR * 100));
            if (item != null &&item.hasNbt() && item.getNbt().getBoolean("sure")){
                long[] arrayList = item.getNbt().getLongArray("xyz");
                x = (double)arrayList[0]/10000;
                y = (double)arrayList[1]/10000;
                z = (double)arrayList[2]/10000;
                xR = (double)arrayList[3]/10000;
                yR = (double)arrayList[4]/10000;
                zR = (double)arrayList[5]/10000;
                a = Math.acos(zR / Math.sqrt(xR * xR + zR * zR));
                if (-xR < 0) a = a * -1;
                refreshPositionAndAngles(x + 2 * Math.cos(a + 0.25 * Math.PI), y + yR + 1, z + 2 * Math.sin(a + 0.25 * Math.PI), 0, 0);
                lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(x + xR * 100, y + 1.2 + yR * 100, z + zR * 100));
            }
        }
        super.travel(movementInput);
    }
    @Override
    public void tick() {
        System.out.println(age);
        if (sure) {
            if (age > 160) age = 0;
            if (age <= 40) {
                particle();
            }
            age = age + 1;
        }
        if (age == 110) {
            particle2();
            explosion();
        }
        if (age == 160 && item != null) {
            item.setNbt(new NbtCompound());
            item.decrement(1);
            sure = false;
            discard();
        }
        if (item != null && !item.getNbt().getBoolean("sure")) {
            if (player != null && player.getMainHandStack() != item) {
                discard();
            }
        }
        super.tick();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        sure = true;
        return super.interactMob(player, hand);
    }

    public void explosion(){
        if (world.isClient){
            return;
        }
        double x = getRotationVector().getX();
        double y = getRotationVector().getY();
        double z = getRotationVector().getZ();

        int a = 0;
        int i;
        for (int t = 0; t < 800; t++) {
            a++;
            i = a;
            if (world.getBlockState(new BlockPos(getX() + x * a, getY() + y * a, getZ() + z * a)) != Blocks.AIR.getDefaultState()) {
                for (int c = 0; c < 100; c++) {
                    i = i + 1;
                    Explosion explosion = new Explosion(world, player, getX() + x * i, getY() + y * i - 1, getZ() + z * i, 7, false, Explosion.DestructionType.DESTROY);
                    explosion.collectBlocksAndDamageEntities();
                    for (int j = 0; j < explosion.getAffectedBlocks().toArray().length; j++) {
                        if (world.getBlockState(explosion.getAffectedBlocks().get(j)) != Blocks.AIR.getDefaultState())
                            pos.add(explosion.getAffectedBlocks().get(j));
                    }
                    explosion.affectWorld(true);
                    this.world.playSound(getX() + x * i, getY() + y * i - 1, getZ() + z * i, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
                }
                if (pos != null) {
                    HashSet<BlockPos> hashset_temp = new HashSet<>(pos);
                    pos = new ArrayList<>(hashset_temp);
                    for (int j = 0; j < pos.toArray().length; j++) {
                        this.getWorld().setBlockState(pos.get(j), Blocks.WATER.getDefaultState());
                    }
                }
                break;
            }
        }
    }
    public void particle(){
        int q = new Random().nextInt(2) == 1 ? 1:-1;
        int i = new Random().nextInt(2) == 1 ? 1:-1;
        int j = new Random().nextInt(2) == 1 ? 1:-1;
        double a = new Random().nextDouble(5) * q;
        double b = new Random().nextDouble(5) * i;
        double c = new Random().nextDouble(5) * j;
        int z = new Random().nextInt(2) == 1 ? 1:-1;
        int x = new Random().nextInt(2) == 1 ? 1:-1;
        int v = new Random().nextInt(2) == 1 ? 1:-1;
        double g = new Random().nextDouble(0.5) * z;
        double h = new Random().nextDouble(0.5) * x;
        double k = new Random().nextDouble(0.5) * v;

        Vec3d point = new Vec3d(getX() + getRotationVector().getX() * 1.5, getY() + getRotationVector().getY() * 1.5,getZ() + getRotationVector().getZ() * 1.5);
        world.addParticle(WaterbucketClient.WaterBucket,point.getX() + a,point.getY() + b,point.getZ() + c,-a/3.8,-b/3.8,-c/3.8);
        for (int l = 0; l < age; l++) {
            world.addParticle(WaterbucketClient.WaterBucket,point.getX() + g,point.getY() + h,point.getZ() + k,0,0,0);
        }
    }

    public void particle2(){
        for (int i = 3; i < 100; i++) {
            double size = (i+3) * 0.3;
            if (i > 5) size = 8 * 0.3;
            Vec3d point = new Vec3d(getX() + getRotationVector().getX()*i, getY() + getRotationVector().getY()*i,getZ() + getRotationVector().getZ()*i);
            for (int l = 0; l < 150; l++) {
                int q = new Random().nextInt(2) == 1 ? 1:-1;
                int k = new Random().nextInt(2) == 1 ? 1:-1;
                int j = new Random().nextInt(2) == 1 ? 1:-1;
                int p = new Random().nextInt(2) == 1 ? 1:-1;
                int o = new Random().nextInt(2) == 1 ? 1:-1;
                int u = new Random().nextInt(2) == 1 ? 1:-1;
                double a = new Random().nextDouble(size) * q;
                double b = new Random().nextDouble(size) * k;
                double c = new Random().nextDouble(size) * j;
                double y = new Random().nextDouble(size) * p;
                double t = new Random().nextDouble(size) * o;
                double r = new Random().nextDouble(size) * u;
                world.addParticle(WaterbucketClient.Water,point.getX()+a,point.getY()+b,point.getZ()+c,0,0,0);
                world.addParticle(ParticleTypes.CLOUD,true,point.getX()+y,point.getY()+t,point.getZ()+r,0,0,0);
            }
        }
    }
    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
    @Override
    public void registerControllers(AnimationData animationData) {}
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
