package com.dillo.dilloUtils.RouteChecker;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class CustomRayTrace extends MovingObjectPosition {

  private boolean skip;

  public CustomRayTrace(BlockPos block1, EnumFacing sideHit, Vec3 hitVec, boolean skip) {
    super(hitVec, sideHit, block1);
    this.skip = skip;
  }

  public boolean shouldSkip() {
    return skip;
  }
}
