package com.daniel.waterbucket.item.NearbyPlayerLocator;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.tick.Tick;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NearbyPlayerLocator extends Item {
    // 记录上次使用时间的 Map，用于实现冷却时间
    private final Map<UUID, Long> cooldownMap = new HashMap<>();
    public NearbyPlayerLocator() {
        super(new Settings().group(ItemGroup.MISC));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            int maxTick = 60;
            if (stack.hasNbt() && stack.getNbt().getBoolean("tp")) {
                PlayerEntity player = (PlayerEntity) entity;
                player.sendMessage(Text.of("剩余 " + (maxTick - stack.getNbt().getInt("tick")) / 20 + " 秒"), true);
                stack.getNbt().putInt("tick",stack.getNbt().getInt("tick") +1 );
                if (maxTick <= stack.getNbt().getInt("tick")) {
                    entity.teleport(stack.getNbt().getDouble("x"), stack.getNbt().getDouble("y"),stack.getNbt().getDouble("z"));
                    stack.getNbt().putBoolean("tp",false);
                    player.sendMessage(Text.of("成功传送"), true);
                    if (!player.isCreative()) stack.decrement(1);
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.hasNbt())stack.setNbt(new NbtCompound());
        if (!world.isClient) {
            //传送功能
            if (player.isSneaking()) {
                stack.getNbt().putInt("tick",0);
                if (stack.hasNbt() && !stack.getNbt().getBoolean("tp")) {
                    BlockPos pos = findValidTeleportLocation(world,getNearestPlayer(world,player));
                    System.out.println(pos);
                    if (pos != null) {
                        stack.getNbt().putBoolean("tp",true);
                        stack.getNbt().putDouble("x",pos.getX());
                        stack.getNbt().putDouble("y",pos.getY());
                        stack.getNbt().putDouble("z",pos.getZ());
                        getNearestPlayer(world,player).sendMessage(Text.of(player.getName().getString()+" 3秒后传送到你附近，请做好准备"),true);
                    } else player.sendMessage(Text.of("这里没有别的玩家"), true);
                }
            } else {
                // 获取上次使用时间和当前时间
                long currentTime = System.currentTimeMillis();
                long lastTime = cooldownMap.getOrDefault(player.getUuid(), 0L);

                // 计算距离上次使用的时间间隔（秒）
                int cooldownTime = 3; // 冷却时间为 10 秒
                int secondsSinceLastUse = (int) ((currentTime - lastTime) / 1000);

                if (secondsSinceLastUse >= cooldownTime) {
                    // 获取附近最近的玩家
                    PlayerEntity nearestPlayer = getNearestPlayer(world, player);

                    if (nearestPlayer != null) {
                        // 获取最近玩家的位置坐标
                        int x = (int) nearestPlayer.getX();
                        int y = (int) nearestPlayer.getY();
                        int z = (int) nearestPlayer.getZ();

                        // 计算距离
                        int distance = (int) player.distanceTo(nearestPlayer);

                        // 将信息发送给使用该物品的玩家
                        Text message = Text.of("最近的玩家 "+ nearestPlayer.getName().getString() +" 位置： X: " + x + ", Y: " + y + ", Z: " + z + ", 距离你 " + distance + " 米");
                        player.sendMessage(message, true);
                        // 更新上次使用时间
                        cooldownMap.put(player.getUuid(), currentTime);

                    } else {
                        player.sendMessage(Text.of("这里没有别的玩家"), true);
                        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
                    }
                } else {
                    // 提示冷却时间剩余
                    int remainingCooldown = cooldownTime - secondsSinceLastUse;
                    player.sendMessage(Text.of("物品冷却中，请等待 " + remainingCooldown + " 秒后再试"), true);
                }
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    private PlayerEntity getNearestPlayer(World world, PlayerEntity player) {
        double closestDistanceSq = Double.MAX_VALUE;
        PlayerEntity nearestPlayer = null;

        for (PlayerEntity otherPlayer : world.getPlayers()) {
            if (otherPlayer != player) {
                double distanceSq = player.squaredDistanceTo(otherPlayer);

                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    nearestPlayer = otherPlayer;
                }
            }
        }
        return nearestPlayer;
    }
    private BlockPos findValidTeleportLocation(World world, PlayerEntity player) {
        if (player == null)return null;
        BlockPos playerPos = player.getBlockPos();
        int maxDistance = 50;
        for (int i = 0; i < maxDistance; i++) {
            BlockPos targetPos = playerPos.offset(Direction.random(world.random), i);
            // 检查传送位置是否安全
            if (isSafeTeleportLocation(world, targetPos)) {
                return targetPos;
            }
        }
        return null; // 没有找到有效位置
    }
    // 判断传送位置是否安全（不会窒息或脚下是虚空）
    private boolean isSafeTeleportLocation(World world, BlockPos pos) {
        return world.isAir(pos.up()) && !world.getBlockState(pos).getMaterial().isSolid();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);


        tooltip.add(Text.of("下蹲可以传送其他玩家"));

    }
}
