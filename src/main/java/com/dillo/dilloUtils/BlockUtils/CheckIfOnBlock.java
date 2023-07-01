package com.dillo.dilloUtils.BlockUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.List;
import net.minecraft.util.BlockPos;

public class CheckIfOnBlock {

  public static BlockPos checkIfOnBlock() {
    List<BlockPos> blocks = currentRoute.currentRoute;

    for (int i = 0; i < blocks.size(); i++) {
      //SendChat.chat(String.valueOf(Math.abs(blocks.get(i).getX() - ids.mc.thePlayer.posX + 0.5)));

      if (
        Math.abs(blocks.get(i).getX() - ids.mc.thePlayer.posX + 0.5) <= 0.0001 && Math.abs(blocks.get(i).getZ() - ids.mc.thePlayer.posZ + 0.5) <= 0.0001 && Math.abs(blocks.get(i).getY() - ids.mc.thePlayer.posY + 1) <= 0.0001) {
        return blocks.get(i);
      }
    }

    return null;
  }
}
