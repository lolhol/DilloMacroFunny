package com.dillo.utils.previous.getUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class checkIfItemInInv {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static boolean checkIfItemInInv(Item item) {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

      if (stack != null && stack.getItem() == item) {
        return true;
      }
    }

    return false;
  }
}
