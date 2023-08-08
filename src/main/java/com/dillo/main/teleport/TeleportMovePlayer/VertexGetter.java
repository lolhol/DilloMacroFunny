package com.dillo.main.teleport.TeleportMovePlayer;

import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class VertexGetter {

  public VertexGetterClass getVertex(VertexGetterConfig config) {
    Vec3 playerVec = ids.mc.thePlayer.getPositionVector();
    List<VertexGetterClass> list = fromPlayerPosToListOVertexes(playerVec);

    for (VertexGetterClass vertex : list) {
      if (
        canSee(vertex.vec, config.nextBlock) &&
        isUnObstructed(vertex.vec) &&
        !isBlocksRightLeft(vertex, ids.mc.thePlayer.getPositionVector())
      ) {
        return vertex;
      }
    }

    return null;
  }

  boolean isBlocksRightLeft(VertexGetterClass vertex, Vec3 centerblock) {
    Vec3 direction = centerblock.subtract(vertex.vec).normalize();

    final double gap = 0.5;

    for (float rot = -90; rot <= 90; rot += 180) {
      for (int y = 0; y <= 1; y++) {
        Vec3 narmal = direction.rotateYaw(rot);
        BlockPos block = BlockUtils.fromVec3ToBlockPos(
          new Vec3(narmal.xCoord * gap, narmal.yCoord + y, narmal.zCoord * gap)
        );

        if (ids.mc.theWorld.getBlockState(block).getBlock() != Blocks.air) return true;
      }
    }

    return false;
  }

  boolean canSee(Vec3 startVec, BlockPos destBlock) {
    Vec3 resultORayTrace = adjustLook(startVec, destBlock, new net.minecraft.block.Block[] { Blocks.air }, false);
    return resultORayTrace != null;
  }

  boolean isUnObstructed(Vec3 vec) {
    return (
      ids.mc.theWorld.getBlockState(BlockUtils.fromVec3ToBlockPos(vec)).getBlock() == Blocks.air &&
      ids.mc.theWorld.getBlockState(BlockUtils.fromVec3ToBlockPos(vec.addVector(0, 1, 0))).getBlock() == Blocks.air &&
      ids.mc.theWorld.getBlockState(BlockUtils.fromVec3ToBlockPos(vec.addVector(0, 2, 0))).getBlock() == Blocks.air
    );
  }

  List<VertexGetterClass> fromPlayerPosToListOVertexes(Vec3 playerHeightVec) {
    List<VertexGetterClass> returnList = new ArrayList<>();
    returnList.add(
      new VertexGetterClass(
        playerHeightVec.addVector(PositionsMoves.RIGHT.dx, 0, PositionsMoves.RIGHT.dz),
        PositionsMoves.RIGHT
      )
    );
    returnList.add(
      new VertexGetterClass(
        playerHeightVec.addVector(PositionsMoves.LEFT.dx, 0, PositionsMoves.LEFT.dz),
        PositionsMoves.LEFT
      )
    );
    returnList.add(
      new VertexGetterClass(
        playerHeightVec.addVector(PositionsMoves.BACKRIGHT.dx, 0, PositionsMoves.BACKRIGHT.dz),
        PositionsMoves.BACKRIGHT
      )
    );
    returnList.add(
      new VertexGetterClass(
        playerHeightVec.addVector(PositionsMoves.BACKLEFT.dx, 0, PositionsMoves.BACKLEFT.dz),
        PositionsMoves.BACKLEFT
      )
    );

    return returnList;
  }

  @Getter
  @AllArgsConstructor
  public class VertexGetterClass {

    public Vec3 vec;
    public PositionsMoves move;
  }
}
