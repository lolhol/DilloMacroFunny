package com.dillo.dilloUtils.Teleport.TeleportMovePlayer;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.Pathfinding.baritone.automine.handlers.KeybindHandler;
import com.dillo.dilloUtils.LookAt;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MoveToVertex {

  boolean isStart = false;
  Vec3 vertexVector;
  CurrentState newState;

  public void moveToVertex(Vec3 vertexVector, CurrentState newState) {
    LookAt.smoothLook(LookAt.getRotation(vertexVector), 100);
    this.vertexVector = vertexVector;
    this.newState = newState;

    new Thread(() -> {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      //if (ArmadilloStates.isOnline()) {
      isStart = true;
      //}
    })
      .start();
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isStart/* || !ArmadilloStates.isOnline()*/) return;

    Vec3 playerVec = ids.mc.thePlayer.getPositionVector();

    if (
      Math.abs(Math.abs(playerVec.xCoord) - Math.abs(vertexVector.xCoord)) > 0.1 ||
      Math.abs(Math.abs(playerVec.zCoord) - Math.abs(vertexVector.zCoord)) > 0.1 ||
      Math.abs(Math.abs(vertexVector.xCoord) - Math.abs(playerVec.xCoord)) > 0.1 ||
      Math.abs(Math.abs(vertexVector.zCoord) - Math.abs(playerVec.zCoord)) > 0.1
    ) {
      KeybindHandler.updateKeys(true, false, false, false, false, false, true, false);
    } else {
      isStart = false;
      KeybindHandler.updateKeys(false, false, false, false, false, false, true, false);

      new Thread(() -> {
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
        if (ArmadilloStates.isOnline()) ArmadilloStates.currentState = newState;
      })
        .start();
    }
  }
}
