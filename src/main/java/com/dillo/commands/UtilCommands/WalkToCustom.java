package com.dillo.commands.UtilCommands;

import static com.dillo.dilloUtils.Teleport.SmartTP.smartTP;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.util.BlockPos;

public class WalkToCustom extends Command {

  public static boolean startRender = false;

  public WalkToCustom() {
    super("walkToCustom");
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

    ArmadilloStates.offlineState = KillSwitch.ONLINE;
    smartTP(new BlockPos(x, y, z));
    SendChat.chat("Teleporting!");
    // SendChat.chat(String.valueOf(getYawNeededVec(new Vec3(x, y, z), displacement)))
    //isStructureBetween(ids.mc.thePlayer.getPosition(), new BlockPos(x, y, z));
    /*RenderMultipleLines.renderMultipleLines(null, null, false);

        List<BlockPos> foundRoute = FindPathToBlock.pathfinderTest(new BlockPos(x, y, z));

        if (foundRoute != null) {
            for (int i = 0; i < foundRoute.size(); i++) {
                if (i != foundRoute.size() - 1) {
                    RenderMultipleLines.renderMultipleLines(foundRoute.get(i), foundRoute.get(i + 1), true);
                }
            }

            foundRoute.remove(0);

            ArmadilloStates.offlineState = "online";
            SendChat.chat(String.valueOf(foundRoute.size()));
            WalkOnPath.walkOnPath(foundRoute);
        }*/

    //TeleportToBlock.teleportToBlock(new BlockPos(x, y, z), 500, 500, null);
  }

  private static float getYawBlock(BlockPos block) {
    double dX = block.getX() + 0.5 - ids.mc.thePlayer.posX;
    double dZ = block.getZ() + 0.5 - ids.mc.thePlayer.posZ;

    double angle = Math.atan2(dZ, dX);
    float rotationYaw = (float) Math.toDegrees(angle) - 90.0f;

    if (rotationYaw < 0.0f) {
      rotationYaw += 360.0f;
    }

    float playerYaw = ids.mc.thePlayer.rotationYaw;

    if (ids.mc.thePlayer.rotationYaw > 360) {
      playerYaw = (float) (ids.mc.thePlayer.rotationYaw - (Math.floor(ids.mc.thePlayer.rotationYaw / 360)) * 360);
    }

    // SendChat.chat(" !!! " + playerYaw + " !!! ");

    if (playerYaw > 180) {
      playerYaw -= 360;
    }

    rotationYaw = rotationYaw - playerYaw;

    return Math.abs(rotationYaw);
  }
}
