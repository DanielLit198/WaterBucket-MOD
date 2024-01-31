package com.daniel.waterbucket.item.MlgWorldConveyor;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.entity.chest.ChestEntity;
import com.daniel.waterbucket.init.DimensionInit;
import com.daniel.waterbucket.init.EntityInit;
import com.daniel.waterbucket.init.ItemInit;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MlgWorldConveyor extends Item {

    public MlgWorldConveyor() {
        super(new Settings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            List<ServerPlayerEntity> players = new ArrayList<>(world.getServer().getPlayerManager().getPlayerList());
            for (ServerPlayerEntity player : players) {
                if (player.getWorld().getDimensionKey() != DimensionInit.MLG_TYPE_KEY) {
                    player.getInventory().clear();
                    player.giveItemStack(new ItemStack(ItemInit.nearbyPlayerLocator));
                    player.giveItemStack(new ItemStack(ItemInit.CannonItem));
                    player.giveItemStack(new ItemStack(Items.RED_BED));
                    player.giveItemStack(new ItemStack(Items.WATER_BUCKET));
                    ServerWorld serverWorld = player.getServer().getWorld(DimensionInit.MLG_DIMENSION_KEY);
                    Random random = new Random();
                    player.teleport(serverWorld, random.nextDouble(5000), 250, random.nextDouble(5000), player.getYaw(), user.getPitch());
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
