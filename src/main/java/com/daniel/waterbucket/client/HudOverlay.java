package com.daniel.waterbucket.client;

import com.daniel.waterbucket.Waterbucket;
import com.daniel.waterbucket.init.EnchantmentInit;
import com.daniel.waterbucket.item.Cannon.CannonItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HudOverlay implements HudRenderCallback {
    private static final Identifier Full = new Identifier(Waterbucket.MOD_ID, "textures/charged/charged_full_hud.png");
    private static final Identifier Empty = new Identifier(Waterbucket.MOD_ID, "textures/charged/charged_hud.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width/2;
            y = height/2;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, Empty);

        ItemStack itemStack = client.player.getMainHandStack();
        int level = EnchantmentHelper.getLevel(EnchantmentInit.charged, itemStack);
        if (itemStack.getItem() instanceof CannonItem && level >= 1 && client.player.getItemUseTime() > 0 ) {
            int length = 39;
            if (level == 2) length = 25;
            if (level == 1) length = 10;
            for (int i = 0; i < level; i++) {
                DrawableHelper.drawTexture(matrixStack, x - length + (i * 28), y + 10, 0, 0, 22, 22, 22, 22);
            }
        }
        RenderSystem.setShaderTexture(0,Full);
        if (itemStack.getItem() instanceof CannonItem && level >= 1) {
            int length = 39;
            if(level == 2) length = 25;
            if (level == 1) length = 10;
            if (itemStack.hasNbt()) {
                for (int i = 0; i < itemStack.getNbt().getInt("charged"); i++) {
                    DrawableHelper.drawTexture(matrixStack, x - length + (i * 28), y + 10, 0, 0, 22, 22, 22, 22);
                }
            }
        }
    }
}
