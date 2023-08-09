package com.dillo.gui.GUIUtils.DilloRouteUtils;

import com.dillo.main.files.localizedData.currentRoute;

import static com.dillo.gui.GUIUtils.MatchServer.IsChecked.isChecked;
import static com.dillo.utils.BlockUtils.areAllLoaded;

public class IsInBlockRange {

    public static boolean isInCheckRange() {
        if (currentRoute.currentRoute.size() == 0 || !areAllLoaded(currentRoute.currentRoute) || isChecked()) {
            return false;
        }

        return true;
    }
}
