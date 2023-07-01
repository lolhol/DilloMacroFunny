package com.dillo.utils.previous.random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class swapToSlot {

  public static void swapToItem(Item item) {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];
      if (stack != null && stack.getItem() == item) {
        ids.mc.thePlayer.inventory.currentItem = i;
        break;
      }
    }
  }

  public static void swapToSlot(int slot) {
    ids.mc.thePlayer.inventory.currentItem = slot;
  }
}
