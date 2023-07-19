package com.dillo.commands.baritone;

import static com.dillo.Pathfinding.baritone.automine.utils.HypixelUtils.MineUtils.gemPriority;

import com.dillo.Pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.Pathfinding.baritone.automine.config.WalkBaritoneConfig;
import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.ArrayList;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WalkToBlockWithBaritone extends Command {

  public WalkToBlockWithBaritone() {
    super("walkBaritone");
  }

  @DefaultHandler
  public void handle() {
    /*SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    autoMineBaritone.goTo(blockToWalk);*/

    SendChat.chat("SSS");

    AutoMineBaritone autoMineBaritone = new AutoMineBaritone(new WalkBaritoneConfig(0, 256, 5));
    ArrayList<ArrayList<BlockData<?>>> filter = new ArrayList<>();
    ArrayList<BlockData<?>> glass = new ArrayList<BlockData<?>>() {
      {
        add(new BlockData<>(Blocks.diamond_ore, gemPriority[2]));
      }
    };
    filter.add(glass);
    ArrayList<BlockData<?>> panes = new ArrayList<BlockData<?>>() {
      {
        add(new BlockData<>(Blocks.diamond_block, gemPriority[2]));
      }
    };
    filter.add(panes);
    autoMineBaritone.mineFor(filter);
  }
}
