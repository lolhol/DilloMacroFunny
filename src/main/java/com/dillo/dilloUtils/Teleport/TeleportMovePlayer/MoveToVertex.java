package com.dillo.dilloUtils.Teleport.TeleportMovePlayer;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.Pathfinding.baritone.automine.handlers.KeybindHandler;
import com.dillo.dilloUtils.LookAt;
import com.dillo.utils.previous.random.ids;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MoveToVertex {

  boolean isStart = false;
  VertexGetter.VertexGetterClass vertexVector;
  CurrentState newState;
  BindCombination combo;

  public void moveToVertex(VertexGetter.VertexGetterClass vertexVector, CurrentState newState) {
    this.vertexVector = vertexVector;
    this.newState = newState;

    EnumFacing facing = ids.mc.thePlayer.getHorizontalFacing();

    float pitch = 0.0F;
    float yaw = 0.0F;

    switch (facing) {
      case SOUTH:
        yaw = SidePitchYaws.SOUTH.yaw;
        pitch = SidePitchYaws.SOUTH.pitch;
        break;
      case WEST:
        yaw = SidePitchYaws.WEST.yaw;
        pitch = SidePitchYaws.WEST.pitch;
        break;
      case NORTH:
        yaw = SidePitchYaws.NORTH.yaw;
        pitch = SidePitchYaws.NORTH.pitch;
        break;
      case EAST:
        yaw = SidePitchYaws.EAST.yaw;
        pitch = SidePitchYaws.EAST.pitch;
        break;
    }

    LookAt.smoothLook(new LookAt.Rotation(pitch, yaw), 100);

    combo = getKeyComboFromEnum(facing, vertexVector);

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
      Math.abs(Math.abs(playerVec.xCoord) - Math.abs(vertexVector.vec.xCoord)) > 0.1 ||
      Math.abs(Math.abs(playerVec.zCoord) - Math.abs(vertexVector.vec.zCoord)) > 0.1 ||
      Math.abs(Math.abs(vertexVector.vec.xCoord) - Math.abs(playerVec.xCoord)) > 0.1 ||
      Math.abs(Math.abs(vertexVector.vec.zCoord) - Math.abs(playerVec.zCoord)) > 0.1
    ) {
      KeybindHandler.updateKeys(combo.w, combo.s, combo.a, combo.d, false, false, true, false);
    } else {
      isStart = false;
      KeybindHandler.updateKeys(false, false, false, false, false, false, true, false);

      new Thread(() -> {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
        if (ArmadilloStates.isOnline()) ArmadilloStates.currentState = newState;
      })
        .start();
    }
  }

  public BindCombination getKeyComboFromEnum(EnumFacing facing, VertexGetter.VertexGetterClass vertexVector) {
    switch (facing) {
      case SOUTH:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(true, false, false, true);
          case LEFT:
            return new BindCombination(false, true, false, true);
          case BACKRIGHT:
            return new BindCombination(true, false, true, false);
          case BACKLEFT:
            return new BindCombination(false, true, true, false);
        }
      case WEST:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(false, true, false, true);
          case LEFT:
            return new BindCombination(false, true, true, false);
          case BACKRIGHT:
            return new BindCombination(true, false, false, true);
          case BACKLEFT:
            return new BindCombination(true, false, true, false);
        }
      case NORTH:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(false, true, true, false);
          case LEFT:
            return new BindCombination(true, false, true, false);
          case BACKRIGHT:
            return new BindCombination(false, true, false, true);
          case BACKLEFT:
            return new BindCombination(true, false, false, true);
        }
      case EAST:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(true, false, true, false);
          case LEFT:
            return new BindCombination(true, false, false, true);
          case BACKLEFT:
            return new BindCombination(false, true, true, false);
          case BACKRIGHT:
            return new BindCombination(false, true, false, true);
        }
    }

    return null;
  }

  @Getter
  @AllArgsConstructor
  class BindCombination {

    boolean w;
    boolean s;
    boolean d;
    boolean a;
  }

  enum SidePitchYaws {
    SOUTH(0.0F, 0.0F),
    WEST(90.0F, 0.0F),
    NORTH(180.0F, 0.0F),
    EAST(-90F, 0.0F);

    public final float yaw;
    public final float pitch;

    SidePitchYaws(float yaw, float pitch) {
      this.pitch = pitch;
      this.yaw = yaw;
    }
  }
}
