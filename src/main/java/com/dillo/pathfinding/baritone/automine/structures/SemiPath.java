package com.dillo.pathfinding.baritone.automine.structures;

import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathMode;

import java.util.LinkedList;

public class SemiPath extends Path {

    public SemiPath(LinkedList<BlockNode> blocksInPath, PathMode mode) {
        super(blocksInPath, mode);
    }
}
