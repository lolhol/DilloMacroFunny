package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.regJump;

import com.dillo.pathfinding.stevebot.core.StevebotApi;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class WalkToCustom extends Command {

  public static boolean startRender = false;
  StevebotApi api;

  public WalkToCustom(StevebotApi api) {
    super("walkToCustom");
    this.api = api;
  }

  @DefaultHandler
  public void handle() {
    /*new Thread(() -> {
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
      .start();*/

    regJump.startStop(startRender);
    startRender = !startRender;
  }
}
