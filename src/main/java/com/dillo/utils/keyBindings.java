package com.dillo.utils;

import com.dillo.utils.previous.random.ids;
import java.lang.reflect.Method;

public class keyBindings {

  public static boolean invoke(Object object, String methodName) {
    try {
      final Method method = object.getClass().getDeclaredMethod(methodName);
      method.setAccessible(true);
      method.invoke(object);
      return true;
    } catch (Exception ignored) {}
    return false;
  }

  public static void rightClick() {
    if (!invoke(ids.mc, "func_147121_ag")) {
      invoke(ids.mc, "rightClickMouse");
    }
  }
}
