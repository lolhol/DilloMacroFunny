package com.dillo.dilloUtils.BlockUtils.fileUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import java.io.File;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;

public class ClearRoute {

  public static void clearBlockRoute(File file) {
    currentRoute.currentRoute = new ArrayList<BlockPos>();
    ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
  }
}
