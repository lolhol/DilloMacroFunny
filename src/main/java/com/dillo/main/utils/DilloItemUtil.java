package com.dillo.main.utils;

import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.getItemInSlot;
import net.minecraft.init.Items;

public class DilloItemUtil {

  public static boolean isGotAllItems() {
    int drill = GetSBItems.getDrillSlot();
    int aotv = GetSBItems.getAOTVSlot();
    int rod = getItemInSlot.getItemSlot(Items.fishing_rod);

    return drill != -1 && aotv != -1 && rod != -1;
  }
}
