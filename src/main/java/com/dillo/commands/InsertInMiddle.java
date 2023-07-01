package com.dillo.commands;

import com.dillo.dilloUtils.BlockUtils.fileUtils.ReWriteFile;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class InsertInMiddle extends Command {

  public InsertInMiddle() {
    super("insertInMiddle");
  }

  @DefaultHandler
  public void handle(int number) {
    if (number - 1 < currentRoute.currentRoute.size()) {
      BlockPos blockUnder = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 1, ids.mc.thePlayer.posZ);
      if (!currentRoute.currentRoute.contains(blockUnder)) {
        if (ids.mc.theWorld.getBlockState(blockUnder).getBlock() == Blocks.cobblestone) {
          currentRoute.currentRoute.add(number - 1, blockUnder);
          ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
        } else {
          SendChat.chat(prefix.prefix + "Stand on a cobble block in order to insert it!");
        }
      } else {
        SendChat.chat(prefix.prefix + "This block already exists in your route!");
      }
    } else {
      SendChat.chat(prefix.prefix + "Please select a valid point!");
    }
  }
}
