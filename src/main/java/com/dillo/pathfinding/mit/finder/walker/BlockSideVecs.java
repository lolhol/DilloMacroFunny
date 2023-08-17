package com.dillo.pathfinding.mit.finder.walker;

public enum BlockSideVecs {
  RIGHT(0.450, 0.450),
  LEFT(-0.450, 0.450),
  BACKRIGHT(0.450, -0.450),
  BACKLEFT(-0.450, -0.450);

  public final double dx;
  public final double dz;

  BlockSideVecs(double dx, double dz) {
    this.dx = dx;
    this.dz = dz;
  }
}
