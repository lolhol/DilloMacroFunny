package com.dillo.utils.ScoreboardUtils;

import com.dillo.pathfinding.baritone.automine.utils.TablistUtils;

import java.util.List;

public class GetAllPlayersTab {

    public static List<String> getAllPlayerNamesList() {
        return TablistUtils.getTabListPlayersSkyblock();
    }
}
