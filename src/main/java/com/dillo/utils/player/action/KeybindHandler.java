package com.dillo.utils.player.action;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class KeybindHandler {

    static Minecraft mc = Minecraft.getMinecraft();

    public static KeyBinding keybindA = mc.gameSettings.keyBindLeft;
    public static KeyBinding keybindD = mc.gameSettings.keyBindRight;
    public static KeyBinding keybindW = mc.gameSettings.keyBindForward;
    public static KeyBinding keybindS = mc.gameSettings.keyBindBack;
    public static KeyBinding keybindAttack = mc.gameSettings.keyBindAttack;
    public static KeyBinding keybindUseItem = mc.gameSettings.keyBindUseItem;
    public static KeyBinding keyBindShift = mc.gameSettings.keyBindSneak;
    public static KeyBinding keyBindJump = mc.gameSettings.keyBindJump;

    public static void setKeyBindState(KeyBinding key, boolean pressed) {
        if (pressed) {
            if (mc.currentScreen != null) {
                realSetKeyBindState(key, false);
                return;
            }
        }
        realSetKeyBindState(key, pressed);
    }

    private static void realSetKeyBindState(KeyBinding key, boolean pressed) {
        if (pressed) {
            if (!key.isKeyDown()) {
                KeyBinding.onTick(key.getKeyCode());
            }
            KeyBinding.setKeyBindState(key.getKeyCode(), true);
        } else {
            KeyBinding.setKeyBindState(key.getKeyCode(), false);
        }
    }
}
