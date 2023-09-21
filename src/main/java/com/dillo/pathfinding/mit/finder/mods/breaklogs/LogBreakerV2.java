package com.dillo.pathfinding.mit.finder.mods.breaklogs;

import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.packets.getBlockEnum;
import com.dillo.utils.previous.packets.sendStart;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LogBreakerV2 {

  public boolean isOn = false;
  public Utils utils = new Utils();
  List<BlockPos> broken = new ArrayList<>();
  int c = 0;
  int bc = 0;

  public LogBreakerV2() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  public void run(List<BlockPos> blocks, boolean state) {
    this.isOn = state;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isOn) return;

    if (bc >= 40) {
      broken.clear();
      bc = 0;
    } else {
      bc++;
    }

    float reach = ids.mc.playerController.getBlockReachDistance();
    List<BlockPos> blocksAround = utils.getSurroundingLogs(
      reach,
      reach,
      reach,
      BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector()),
      broken
    );

    if (blocksAround.isEmpty()) return;

    if (c < 4) {
      c++;
      return;
    }

    c = 0;

    BlockPos closest = utils.getClosest(blocksAround);
    LookAt.smoothLook(LookAt.getRotation(closest), 300);
    broken.add(closest);

    sendStart.sendStartPacket(closest, getBlockEnum.getEnum(closest));
  }
}
