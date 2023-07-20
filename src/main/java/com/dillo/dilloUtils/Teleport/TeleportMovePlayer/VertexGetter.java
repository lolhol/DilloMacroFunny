package com.dillo.dilloUtils.Teleport.TeleportMovePlayer;

import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class VertexGetter {

  public Vec3 getVertex(VertexGetterConfig config) {
    Vec3 playerVec = ids.mc.thePlayer.getPositionVector().addVector(0, config.playerHeight, 0);

    for (Vec3 vec : fromPlayerPosToListOVertexes(playerVec)) {
      if (canSee(vec, config.nextBlock)) {
        return vec;
      }
    }

    return null;
  }

  boolean canSee(Vec3 startVec, BlockPos destBlock) {
    Vec3 resultORayTrace = adjustLook(startVec, destBlock, new Block[] { Blocks.air }, false);
    return resultORayTrace != null;
  }

  List<Vec3> fromPlayerPosToListOVertexes(Vec3 playerHeightVec) {
    List<Vec3> returnList = new ArrayList<>();
    returnList.add(playerHeightVec.addVector(PositionsMoves.RIGHT.dx, 0, PositionsMoves.RIGHT.dz));
    returnList.add(playerHeightVec.addVector(PositionsMoves.LEFT.dx, 0, PositionsMoves.LEFT.dz));
    returnList.add(playerHeightVec.addVector(PositionsMoves.BACKRIGHT.dx, 0, PositionsMoves.BACKRIGHT.dz));
    returnList.add(playerHeightVec.addVector(PositionsMoves.BACKLEFT.dx, 0, PositionsMoves.BACKLEFT.dz));

    return returnList;
  }
}
