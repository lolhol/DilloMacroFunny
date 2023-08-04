package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.regJump;
import static com.dillo.config.config.ping;
import static com.dillo.main.macro.main.NewSpinDrive.jump;
import static com.dillo.main.route.MobKiller.Utils.getAllNonHumanEntities;
import static com.dillo.main.utils.looks.DriveLook.*;

import com.dillo.config.config;
import com.dillo.events.utilevents.CurJumpProgress;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.AxisAlignedBB;
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
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    int range = 10;

    List<Entity> allEntities = getAllNonHumanEntities(
      new AxisAlignedBB(
        ids.mc.thePlayer.getPositionVector().xCoord - range,
        ids.mc.thePlayer.getPositionVector().yCoord - range,
        ids.mc.thePlayer.getPositionVector().zCoord - range,
        ids.mc.thePlayer.getPositionVector().xCoord + range,
        ids.mc.thePlayer.getPositionVector().yCoord + range,
        ids.mc.thePlayer.getPositionVector().zCoord + range
      )
    );

    allEntities.forEach((Entity entity) -> {
      if (entity instanceof EntityCreature) RenderMultipleBlocksMod.renderMultipleBlocks(
        entity.getPositionVector(),
        true
      );
    });

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
    });
  }
}
