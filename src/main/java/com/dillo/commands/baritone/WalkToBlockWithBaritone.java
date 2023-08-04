package com.dillo.commands.baritone;

import static com.dillo.armadillomacro.pathHandler;

import com.dillo.pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.pathfinding.baritone.automine.config.MiningType;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.player.PlayerUtils;
import com.dillo.utils.previous.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WalkToBlockWithBaritone extends Command {

  public WalkToBlockWithBaritone() {
    super("walkBaritone");
  }

  StevebotApi api = new StevebotApi(pathHandler);

  @DefaultHandler
  public void handle(int x, int y, int z) {
    /*SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    autoMineBaritone.goTo(blockToWalk);*/

    api.path(new BaseBlockPos(PlayerUtils.getPlayerBlockPos()), new BaseBlockPos(x, y, z), true, false);
  }

  final List<Block> blocksAllowedToMine = new ArrayList<Block>() {
    {
      add(Blocks.stone);
      add(Blocks.gold_ore);
      add(Blocks.emerald_ore);
      add(Blocks.redstone_ore);
      add(Blocks.iron_ore);
      add(Blocks.coal_ore);
      add(Blocks.stained_glass_pane);
      add(Blocks.stained_glass);
      add(Blocks.air);
    }
  };

  private BaritoneConfig getMineBehaviour() {
    return new BaritoneConfig(MiningType.DYNAMIC, false, true, false, 200, 8, null, blocksAllowedToMine, 256, 256);
  }
}
