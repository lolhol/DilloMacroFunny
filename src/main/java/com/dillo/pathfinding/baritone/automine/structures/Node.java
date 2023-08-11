package com.dillo.pathfinding.baritone.automine.structures;

import net.minecraft.util.BlockPos;

public class Node {

    public BlockPos pos;
    public double hCost;
    public double gCost = -1;
    public double fCost;
    public Node parentNode;
    public Node(BlockPos pos) {
        this.pos = pos;
    }
}
