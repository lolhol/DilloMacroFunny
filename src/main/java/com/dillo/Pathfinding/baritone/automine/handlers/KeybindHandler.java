package com.dillo.Pathfinding.baritone.automine.handlers;

import com.dillo.Pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.Pathfinding.baritone.automine.config.WalkBaritoneConfig;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;

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

  AutoMineBaritone debugBaritone = new AutoMineBaritone(new WalkBaritoneConfig(0, 256, 5));

  public static void updateKeys(
    boolean wBool,
    boolean sBool,
    boolean aBool,
    boolean dBool,
    boolean atkBool,
    boolean useBool,
    boolean shiftBool
  ) {
    if (mc.currentScreen != null) {
      resetKeybindState();
      return;
    }
    realSetKeyBindState(keybindW, wBool);
    realSetKeyBindState(keybindS, sBool);
    realSetKeyBindState(keybindA, aBool);
    realSetKeyBindState(keybindD, dBool);
    realSetKeyBindState(keybindAttack, atkBool);
    realSetKeyBindState(keybindUseItem, useBool);
    realSetKeyBindState(keyBindShift, shiftBool);
  }

  public static void setKeyBindState(KeyBinding key, boolean pressed) {
    if (pressed) {
      if (mc.currentScreen != null) {
        realSetKeyBindState(key, false);
        return;
      }
    }
    realSetKeyBindState(key, pressed);
  }

  public static void updateKeys(
    boolean w,
    boolean s,
    boolean a,
    boolean d,
    boolean atk,
    boolean useItem,
    boolean shift,
    boolean jump
  ) {
    if (mc.currentScreen != null) {
      resetKeybindState();
      return;
    }
    realSetKeyBindState(keybindW, w);
    realSetKeyBindState(keybindS, s);
    realSetKeyBindState(keybindA, a);
    realSetKeyBindState(keybindD, d);
    realSetKeyBindState(keybindAttack, atk);
    realSetKeyBindState(keybindUseItem, useItem);
    realSetKeyBindState(keyBindShift, shift);
    realSetKeyBindState(keyBindJump, jump);
  }

  public static void updateKeys(boolean wBool, boolean sBool, boolean aBool, boolean dBool, boolean atkBool) {
    if (mc.currentScreen != null) {
      resetKeybindState();
      return;
    }
    realSetKeyBindState(keybindW, wBool);
    realSetKeyBindState(keybindS, sBool);
    realSetKeyBindState(keybindA, aBool);
    realSetKeyBindState(keybindD, dBool);
    realSetKeyBindState(keybindAttack, atkBool);
  }

  public static void resetKeybindState() {
    realSetKeyBindState(keybindA, false);
    realSetKeyBindState(keybindS, false);
    realSetKeyBindState(keybindW, false);
    realSetKeyBindState(keybindD, false);
    realSetKeyBindState(keyBindShift, false);
    realSetKeyBindState(keyBindJump, false);
    realSetKeyBindState(keybindAttack, false);
    realSetKeyBindState(keybindUseItem, false);
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
