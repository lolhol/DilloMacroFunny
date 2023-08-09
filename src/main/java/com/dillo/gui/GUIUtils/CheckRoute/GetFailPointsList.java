package com.dillo.gui.GUIUtils.CheckRoute;

import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GetFailPointsList {

    public static List<BlockPos> failList = new ArrayList<>();
    public static List<Integer> failListPoints = new ArrayList<>();

    public static void addToFailList(BlockPos block, int pos) {
        failList.add(block);
        failListPoints.add(pos);
    }

    public static void clearFailList() {
        failList.clear();
        failListPoints.clear();
    }
}
