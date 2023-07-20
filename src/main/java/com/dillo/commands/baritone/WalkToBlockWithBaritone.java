package com.dillo.commands.baritone;

import static com.dillo.Pathfinding.baritone.automine.utils.HypixelUtils.MineUtils.gemPriority;

import com.dillo.Pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.Pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.Pathfinding.baritone.automine.config.MiningType;
import com.dillo.Pathfinding.baritone.automine.config.WalkBaritoneConfig;
import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
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

  @DefaultHandler
  public void handle(int x, int y, int z) {
    /*SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    autoMineBaritone.goTo(blockToWalk);*/

    SendChat.chat("SSS");

    AutoMineBaritone autoMineBaritone = new AutoMineBaritone(getMineBehaviour());
    autoMineBaritone.mineFor(new BlockPos(x, y, z));
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
