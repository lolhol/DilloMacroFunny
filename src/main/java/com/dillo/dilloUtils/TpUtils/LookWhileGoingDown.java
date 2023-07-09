package com.dillo.dilloUtils.TpUtils;

import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.dilloUtils.LookAt;
import java.util.Objects;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LookWhileGoingDown {

  private static CurrentState state = null;
  private static BlockPos blockPos = null;
  private static long maxTimeTake = 0;
  private static boolean onOff = false;

  public static void lookUntilState(CurrentState endState, BlockPos blockPosition, long time) {
    state = endState;
    blockPos = blockPosition;
    maxTimeTake = time;
    onOff = true;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (onOff && ArmadilloStates.isOnline()) {
        if (ArmadilloStates.currentState != ARMADILLO) {
          LookAt.smoothLook(LookAt.getRotation(blockPos), maxTimeTake);
        } else {
          maxTimeTake = 0;
          blockPos = null;
          state = null;
          onOff = false;
        }
      } else {
        onOff = false;
      }
    }
  }
}
