package com.dillo.gui.GUIUtils.DilloRouteUtils;

import static com.dillo.gui.GUIUtils.MatchServer.IsChecked.isChecked;
import static com.dillo.utils.BlockUtils.areAllLoaded;

import com.dillo.main.files.localizedData.currentRoute;

public class IsInBlockRange {

  public static boolean isInCheckRange() {
    return !currentRoute.currentRoute.isEmpty() && areAllLoaded(currentRoute.currentRoute) && !isChecked();
  }
}
