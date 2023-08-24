package com.daniel.waterbucket.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_WATERBUCKET = "key.category.waterbucket.water";
    public static final String KEY_Fire = "key.fire";
    public static KeyBinding fireKey;
    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (fireKey.isPressed()) {
                client.player.sendChatMessage("Fire", Text.of("Fire"));
            }
        });
    }
    public static void register() {
        fireKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_Fire,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SPACE,
                KEY_CATEGORY_WATERBUCKET
        ));
        registerKeyInputs();
    }
}
