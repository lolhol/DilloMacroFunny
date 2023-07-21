package com.dillo.commands.UtilCommands;

import com.dillo.Pathfinding.stevebot.core.StevebotApi;
import com.dillo.Pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.Pathfinding.stevebot.core.data.blockpos.FastBlockPos;
import com.dillo.Pathfinding.stevebot.core.player.PlayerUtils;
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
  public void handle(int x, int y, int z) {
    //SendChat.chat(String.valueOf(ids.mc.thePlayer.isRiding()));
    // NewSpinDrive.putAllTogether();
    // ArmadilloStates.offlineState = "online";
    // ArmadilloStates.currentState = "spinDrive";
    //List<String> scoreBoard = getScoreboard();
    //String currentServer = MatchTimeDate.matchServer(cleanSB(scoreBoard.get(scoreBoard.size() - 1)));
    //SendChat.chat(currentServer);
    //getArea();

    /*MoveToVertex moveToV = new MoveToVertex();
    VertexGetter vertexGetter = new VertexGetter();
    VertexGetterConfig config = new VertexGetterConfig(
      ids.mc.thePlayer.getPositionVector(),
      new BlockPos(x, y, z),
      1.54F
    );
    MinecraftForge.EVENT_BUS.register(moveToV);

    moveToV.moveToVertex(vertexGetter.getVertex(config), null);*/
    api.path(
      new BaseBlockPos(PlayerUtils.getPlayerBlockPos()),
      new BaseBlockPos(new FastBlockPos(x, y, z)),
      false,
      false
    );
    //SendChat.chat(String.valueOf(ids.mc.thePlayer.getHorizontalFacing()));
    //clickSlotShift(1, 0);

    //smoothLook2(new YawLook.RotationYaw(40, 0), 1000);

    //serverSmoothLook(new LookAt.Rotation(0.0F, curRotation() + 100), 1000);
    //startRender = !startRender;
    //curRotation();
    //serverSmoothLook(new LookAt.Rotation(0.0F, 221), 1000);
    //startRender = !startRender;

    //if (!startRender) {
    //reset();
    //}
    //addYaw(config.headMovement * 100L, config.headRotationMax);
    //LookYaw.lookToYaw(config.headMovement * 100L, config.headRotationMax);
    //currentRoute.currentBlock = new BlockPos(x, y, z);
    //currentRoute.currentBlock = currentRoute.currentRoute.get(0);
    //putAllTogether();
    // SendChat.chat(String.valueOf(getYawNeededVec(new Vec3(x, y, z), displacement)))
    //isStructureBetween(ids.mc.thePlayer.getPosition(), new BlockPos(x, y, z));

    //TeleportToBlock.teleportToBlock(new BlockPos(x, y, z), 500, 500, null);
  }
}
