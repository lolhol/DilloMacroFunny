package com.dillo.utils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.item.ItemStack;

public class InInvItemsCheck {

  public static boolean checkItems() {
    int itemsFound = 0;
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String getSBAtrFromStack = getSBAtr.getSkyBlockID(stack);

      if (getSBAtrFromStack != null) {
        if (
          getSBAtrFromStack.contains("GAUNTLET") ||
          getSBAtrFromStack.contains("DRILL") ||
          getSBAtrFromStack.contains("VOID") ||
          getSBAtrFromStack.contains("ROD") ||
          getSBAtrFromStack.contains("PICKON")
        ) {
          itemsFound++;
        }
      }
    }

    if (itemsFound >= 3) {
      return true;
    } else {
      return false;
    }
  }
}
