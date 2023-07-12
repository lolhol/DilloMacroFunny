package com.dillo.commands.RouteMakerUtils;

import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class CalcRouteAvgGemPerc extends Command {

  public CalcRouteAvgGemPerc() {
    super("avgPercent");
  }

  @DefaultHandler
  public void handle() {
    if (currentRoute.currentRoute.size() < 1) {
      SendChat.chat(prefix.prefix + "You haven't selected a route yet.");
      return;
    }

    double total = 0;
    double totalBlocks = 36 * currentRoute.currentRoute.size();

    for (BlockPos block : currentRoute.currentRoute) {
      total += amountOGems(block);
      SendChat.chat(String.valueOf(total));
    }

    if (total < 1) {
      SendChat.chat(prefix.prefix + "No gem blocks found!");
      return;
    }

    double totalPerc = (total / totalBlocks) * 100;
    SendChat.chat(prefix.prefix + "The average percent of gems / vein is " + Math.round(totalPerc) + "%.");
  }

  public int amountOGems(BlockPos block) {
    int finalCount = 0;

    for (int x = -1; x <= 1; x++) {
      for (int y = 0; y <= 4; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos blockPos = makeNewBlock(x, y, z, block);
          Block blockType = ids.mc.theWorld.getBlockState(blockPos).getBlock();

          if (blockType == Blocks.stained_glass_pane || blockType == Blocks.stained_glass) {
            finalCount++;
          }
        }
      }
    }

    return finalCount;
  }
}
