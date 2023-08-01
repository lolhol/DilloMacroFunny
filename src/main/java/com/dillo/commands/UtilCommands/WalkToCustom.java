package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.regJump;
import static com.dillo.config.config.ping;
import static com.dillo.main.macro.main.NewSpinDrive.jump;
import static com.dillo.main.utils.looks.DriveLook.*;

import com.dillo.config.config;
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

    new Thread(() -> {
      KeyBinding.setKeyBindState(jump.getKeyCode(), true);

      try {
        Thread.sleep(ping * 2L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      startJTime = System.currentTimeMillis();
      addYaw(config.headMovement / 2, 180);
      MinecraftForge.EVENT_BUS.post(new CurJumpProgress(0, 0, startRender));
      resetJump();
      regJump.startStop(startRender);
      projectJump = startRender;

      startRender = !startRender;

      try {
        Thread.sleep(160);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      KeyBinding.setKeyBindState(jump.getKeyCode(), false);
    })
      .start();
  }
}
