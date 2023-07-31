package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.regJump;
import static com.dillo.main.macro.main.NewSpinDrive.jump;
import static com.dillo.main.utils.looks.DriveLook.*;

import com.dillo.events.utilevents.CurJumpProgress;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

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

    startJTime = System.currentTimeMillis();
    addYaw(400, 180);
    MinecraftForge.EVENT_BUS.post(new CurJumpProgress(0, 0, startRender));
    KeyBinding.setKeyBindState(jump.getKeyCode(), true);
    resetJump();
    regJump.startStop(startRender);
    projectJump = startRender;

    startRender = !startRender;
  }
}
