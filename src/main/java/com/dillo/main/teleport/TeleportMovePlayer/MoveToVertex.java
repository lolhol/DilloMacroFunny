package com.dillo.main.teleport.TeleportMovePlayer;

import static com.dillo.main.utils.keybinds.AllKeybinds.SNEAK;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.keybinds.KeybindHandler;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.previous.random.ids;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MoveToVertex {

  boolean isStart = false;
  VertexGetter.VertexGetterClass vertexVector;
  CurrentState newState;
  BindCombination combo;
  boolean isTp = false;
  int amountOTicks = 0;
  int curTicks = 0;

  public void moveToVertex(
    VertexGetter.VertexGetterClass vertexVector,
    CurrentState newState,
    boolean isTp,
    int amountOTicks
  ) {
    this.vertexVector = vertexVector;
    this.newState = newState;
    this.isTp = isTp;
    this.amountOTicks = amountOTicks;

    EnumFacing facing = ids.mc.thePlayer.getHorizontalFacing();

    float pitch = ids.mc.thePlayer.rotationPitch;
    float yaw = 0.0F;

    switch (facing) {
      case SOUTH:
        yaw = SidePitchYaws.SOUTH.yaw;
        break;
      case WEST:
        yaw = SidePitchYaws.WEST.yaw;
        break;
      case NORTH:
        yaw = SidePitchYaws.NORTH.yaw;
        break;
      case EAST:
        yaw = SidePitchYaws.EAST.yaw;
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

      if (ArmadilloStates.isOnline()) {
        isStart = true;
      }
    })
      .start();
  }

  void reset() {
    KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
    isStart = false;
    curTicks = 0;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isStart || !ArmadilloStates.isOnline()) {
      isStart = false;
      curTicks = 0;
      return;
    }

    if (curTicks < amountOTicks) {
      curTicks++;
    } else {
      ArmadilloStates.currentState = newState;
      reset();
    }

    Vec3 playerVec = ids.mc.thePlayer.getPositionVector();

    if (
      Math.abs(Math.abs(playerVec.xCoord) - Math.abs(vertexVector.vec.xCoord)) > 0.5 ||
      Math.abs(Math.abs(playerVec.zCoord) - Math.abs(vertexVector.vec.zCoord)) > 0.5 ||
      Math.abs(Math.abs(vertexVector.vec.xCoord) - Math.abs(playerVec.xCoord)) > 0.5 ||
      Math.abs(Math.abs(vertexVector.vec.zCoord) - Math.abs(playerVec.zCoord)) > 0.5
    ) {
      KeybindHandler.updateKeys(combo.w, combo.s, combo.a, combo.d, false, false, true, false);
    } else {
      if (isTp) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);
      isStart = false;
      KeybindHandler.updateKeys(false, false, false, false, false, false, !isTp, false);

      if (!isTp) {
        new Thread(() -> {
          try {
            Thread.sleep(250);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }

          KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
          if (ArmadilloStates.isOnline()) ArmadilloStates.currentState = newState;
        })
          .start();
      } else {
        new Thread(() -> {
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }

          if (ArmadilloStates.isOnline()) ArmadilloStates.currentState = newState;
        })
          .start();
      }
    }
  }

  public BindCombination getKeyComboFromEnum(EnumFacing facing, VertexGetter.VertexGetterClass vertexVector) {
    switch (facing) {
      case SOUTH:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(true, false, false, true);
          case LEFT:
            return new BindCombination(true, false, true, false);
          case BACKRIGHT:
            return new BindCombination(false, true, false, true);
          case BACKLEFT:
            return new BindCombination(false, true, true, false);
        }
      case WEST:
        switch (vertexVector.move) {
          case RIGHT:
            return new BindCombination(false, true, false, true);
          case LEFT:
            return new BindCombination(true, false, false, true);
          case BACKRIGHT:
            return new BindCombination(false, true, true, false);
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
            return new BindCombination(false, true, true, false);
          case BACKLEFT:
            return new BindCombination(false, true, false, true);
          case BACKRIGHT:
            return new BindCombination(true, false, false, true);
        }
    }

    return null;
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

  @Getter
  @AllArgsConstructor
  class BindCombination {

    boolean w;
    boolean s;
    boolean d;
    boolean a;
  }
}
