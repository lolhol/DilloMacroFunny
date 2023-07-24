package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.mobKiller;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.player.PlayerUtils;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;

public class WalkToCustom extends Command {

  public static boolean startRender = false;
  StevebotApi api;

  public WalkToCustom(StevebotApi api) {
    super("walkToCustom");
    this.api = api;
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    SendChat.chat("Killing!");
    mobKiller.killMobsAround(6, null);
    //SendChat.chat("Path Finding!");
    //api.path(new BaseBlockPos(PlayerUtils.getPlayerBlockPos()), new BaseBlockPos(x, y, z), false, false);
  }
}
