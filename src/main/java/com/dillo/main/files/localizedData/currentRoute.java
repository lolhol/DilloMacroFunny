package com.dillo.main.files.localizedData;

import net.minecraft.util.BlockPos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class currentRoute {

    public static List<BlockPos> currentRoute = new ArrayList<BlockPos>();
    public static String currentRouteSelected = "none";
    public static File currentRouteFile = null;
    public static BlockPos currentBlock = null;
    public static List<BlockPos> strucList = new ArrayList<BlockPos>();
    public static File curConfig = null;
}
