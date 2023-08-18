package com.dillo.utils.previous.random;

import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SwapToSlot {

  private static final int MIN_DELAY_AMOUNT = 20;
  private static final int MAX_DELAY_AMOUNT = 80;
  private static final Random random = new Random();

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
    try {
      int delay = MIN_DELAY_AMOUNT + random.nextInt(MAX_DELAY_AMOUNT - MIN_DELAY_AMOUNT + 1);
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
