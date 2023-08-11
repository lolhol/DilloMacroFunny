package com.dillo.pathfinding.baritone.automine.structures;

import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;

@AllArgsConstructor
public class Path {

    @Getter
    LinkedList<BlockNode> blocksInPath;

    @Getter
    PathMode mode;
}
