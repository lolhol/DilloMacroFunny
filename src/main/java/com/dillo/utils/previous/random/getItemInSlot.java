package com.dillo.utils.previous.random;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class getItemInSlot {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static int getItemSlot(Item item) {
        if (checkIfItemInInv.checkIfItemInInv(item)) {
            for (int i = 0; i < 8; i++) {
                ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

                if (stack != null && stack.getItem() == item) {
                    return i;
                }
            }
        }

        return -1;
    }
}
