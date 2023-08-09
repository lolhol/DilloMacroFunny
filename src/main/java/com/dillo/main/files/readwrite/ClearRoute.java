package com.dillo.main.files.readwrite;

import com.dillo.main.files.localizedData.currentRoute;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.util.ArrayList;

public class ClearRoute {

    public static void clearBlockRoute(File file) {
        currentRoute.currentRoute = new ArrayList<BlockPos>();
        ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
    }
}
