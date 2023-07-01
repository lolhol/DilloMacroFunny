package com.dillo.commands;

import com.dillo.dilloUtils.BlockUtils.fileUtils.ReWriteFile;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class ReplaceBlockRoute extends Command {

  public ReplaceBlockRoute() {
    super("replaceBlock");
  }

  @DefaultHandler
  public void handle(int number) {
    if (number - 1 < currentRoute.currentRoute.size()) {
      BlockPos newBlock = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 1, ids.mc.thePlayer.posZ);

      if (ids.mc.theWorld.getBlockState(newBlock).getBlock() == Blocks.cobblestone) {
        if (!currentRoute.currentRoute.contains(newBlock)) {
          currentRoute.currentRoute.set(number - 1, newBlock);
          ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
          SendChat.chat(prefix.prefix + "Block replaced!");
        } else {
          SendChat.chat(prefix.prefix + "You have already added this point to your route!");
        }
      } else {
        SendChat.chat(prefix.prefix + "Please stand on a block that is cobblestone!");
      }
    } else {
      SendChat.chat(prefix.prefix + "Please provide a valid number on route.");
    }
  }
}
