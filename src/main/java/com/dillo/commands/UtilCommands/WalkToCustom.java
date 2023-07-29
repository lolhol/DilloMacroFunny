package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.mobKiller;
import static com.dillo.main.macro.main.NewSpinDrive.newSpinDrive;
import static com.dillo.main.macro.main.NewSpinDrive.putAllTogether;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.alrMoved;
import static com.dillo.main.teleport.utils.IsOnBlock.yaw;
import static com.dillo.main.utils.looks.LookYaw.curRotation;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.utils.looks.LookAt;
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
  public void handle() {
    new Thread(() -> {
      ArmadilloStates.offlineState = KillSwitch.ONLINE;

      SendChat.chat("Rotating!");
      yaw = 30F;
      LookAt.smoothLook(new LookAt.Rotation(yaw, curRotation()), 40);

      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      putAllTogether();

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      newSpinDrive();
    })
      .start();
  }
}
