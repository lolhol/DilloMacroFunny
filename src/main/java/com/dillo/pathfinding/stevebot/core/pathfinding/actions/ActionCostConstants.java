package com.dillo.pathfinding.stevebot.core.pathfinding.actions;

public abstract class ActionCostConstants {

    // walk
    public final double WALK_SPRINT_STRAIGHT = 7.900369003690037;
    public final double WALK_SPRINT_DIAGONAL = 10.73076923076923;
    public final double WALK_SPRINT_DIAGONAL_TOUCHES = 20.0;

    // step down
    public final double STEP_DOWN_STRAIGHT = 9.285714285714286;
    public final double STEP_DOWN_DIAGONAL = 13.333333333333334;

    // step up
    public final double STEP_UP_STRAIGHT = 23.0;
    public final double STEP_UP_DIAGONAL = 26.666666666666668;

    // swim
    public final double SWIM_STRAIGHT = 18.1;
    public final double SWIM_DIAGONAL = 26.0;

    // enter water
    public final double ENTER_WATER_STRAIGHT = 8.5;
    public final double ENTER_WATER_DIAGONAL = 13.0;

    // exit water
    public final double EXIT_WATER_STRAIGHT = 28.0;
    public final double EXIT_WATER_DIAGONAL = 35.333333333333336;

    // jump
    public final double JUMP_STRAIGHT = 39.333333333333336;
    public final double JUMP_DIAGONAL = 37.55555555555556;

    // sprint jump
    public final double SPRINT_JUMP = 33.0;

    // bridge
    public final double BRIDGE_FREE = 30.0;

    // pass the door
    public final double PASS_DOOR = 17.333333333333332;

    // pillar up
    public final double PILLAR_UP = 20.714285714285715;

    // drop down
    public final double DROP_DOWN_STRAIGHT = 35.0;
    public final double DROP_DOWN_DIAGONAL = 38.0;

    // break block
    public final double CONSTANT_BLOCK_BREAK_MOD = 15.0;

    // TODO: Add more action cost constants as needed

}
