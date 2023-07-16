package com.dillo.commands.UtilCommands;

import com.dillo.Events.PlayerMoveEvent;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.dillo.dilloUtils.LookAt.updateServerLook;

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

    //clickSlotShift(1, 0);

    //smoothLook2(new YawLook.RotationYaw(40, 0), 1000);

    // serverSmoothLook(new LookAt.Rotation(40, curRotation() + 20), 100);
    // startRender = !startRender;
    //currentRoute.currentBlock = new BlockPos(x, y, z);
    //currentRoute.currentBlock = currentRoute.currentRoute.get(0);
    //putAllTogether();
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

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!startRender) return;
    //look(LookAt.getRotation(new Vec3(1, 1, 1)));
    updateServerLook();
  }
}
