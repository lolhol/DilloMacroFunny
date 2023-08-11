package com.dillo.main.teleport.TeleportMovePlayer;

public enum PositionsMoves {
    RIGHT(0.799, 0.799),
    LEFT(-0.799, 0.799),
    BACKRIGHT(0.799, -0.799),
    BACKLEFT(-0.799, -0.799);

    public final double dx;
    public final double dz;

    PositionsMoves(double dx, double dz) {
        this.dx = dx;
        this.dz = dz;
    }
}
