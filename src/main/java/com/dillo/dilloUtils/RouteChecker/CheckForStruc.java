package com.dillo.dilloUtils.RouteChecker;

import com.dillo.MITGUI.GUIUtils.MatchServer.MatchTimeDate;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.List;

import static com.dillo.MITGUI.GUIUtils.MatchServer.IsChecked.isChecked;
import static com.dillo.data.config.untouched;
import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;
import static com.dillo.utils.ScoreboardUtils.GetCurArea.cleanSB;
import static com.dillo.utils.ScoreboardUtils.GetCurArea.getScoreboard;

public class CheckForStruc {

  // If you are to use this in your code / replicate this. You MUST credit me. You can do this by going to the
  // "mcmod.info" file and adding the name "godbrigero" to the #credits thing.
  // If you do not do this... i will be VERY mad and will have to take your penis rights
  // By cutting it off.

  // Im actually serious. Add me to the #credits.

  public static boolean isObstructed() {
    List<String> scoreBoard = getScoreboard();

    if (canCheckFurther()) {
      for (BlockPos block : currentRoute.strucList) {
        if (isStructureInWay(block)) {
          return true;
        }
      }
    } else {
      isChecked.add(MatchTimeDate.matchServer(cleanSB(scoreBoard.get(scoreBoard.size() - 1))));
    }

    return false;
  }

  private static boolean isStructureInWay(BlockPos block) {
    int uo = 0;
    int total = 0;

    for (int i = -15; i <= 15; i++) {
      for (int j = -5; j <= 5; j++) {
        for (int k = -15; k <= 15; k++) {
          BlockPos newBlock = makeNewBlock(i, j, k, block);

          if (!isNaturalBlock(newBlock)) {
            uo++;
          }

          total++;
        }
      }
    }

    return (100 / total) * uo < 80;
  }

  private static boolean isNaturalBlock(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    return (
      blockType != Blocks.prismarine &&
      blockType != Blocks.stained_glass &&
      blockType != Blocks.stained_glass_pane &&
      blockType != Blocks.stone &&
      blockType != Blocks.coal_ore &&
      blockType != Blocks.emerald_ore &&
      blockType != Blocks.diamond_ore &&
      blockType != Blocks.gold_ore &&
      blockType != Blocks.iron_ore &&
      blockType != Blocks.lapis_ore &&
      blockType != Blocks.lit_redstone_ore &&
      blockType != Blocks.redstone_ore
    );
  }

  private static boolean canCheckFurther() {
    boolean canCheckFurther = true;

    for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
      BlockPos block = currentRoute.currentRoute.get(i);
      if (isBroken(block)) {
        SendChat.chat(prefix.prefix + "Point " + (int) (i + 1) + " appears to be obstructed! Proceed with caution.");
        canCheckFurther = false;
      }
    }

    return canCheckFurther;
  }

  private static boolean isBroken(BlockPos originalBlock) {
    int glassCount = 0;
    int totalCount = 0;

    for (int i = -1; i <= 1; i++) {
      for (int j = 0; j <= 4; j++) {
        for (int k = -1; k <= 1; k++) {
          BlockPos block = makeNewBlock(i, j, k, originalBlock);

          if (
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane ||
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass
          ) {
            glassCount++;
          }

          totalCount++;
        }
      }
    }

    double percent = ((double) glassCount / totalCount) * 100;

    if (percent < untouched) {
      return true;
    }

    return false;
  }
}
