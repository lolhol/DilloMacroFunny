package com.dillo.utils.ScoreboardUtils;

import com.dillo.pathfinding.baritone.automine.utils.TablistUtils;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.Entity;

public class GetAllPlayersTab {

  public static List<String> getAllPlayerNamesList() {
    return TablistUtils.getTabListPlayersSkyblock();
  }
}
