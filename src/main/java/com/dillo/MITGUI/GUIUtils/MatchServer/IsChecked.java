package com.dillo.MITGUI.GUIUtils.MatchServer;

import static com.dillo.utils.ScoreboardUtils.GetCurArea.cleanSB;
import static com.dillo.utils.ScoreboardUtils.GetCurArea.getScoreboard;

import java.util.HashSet;
import java.util.List;

public class IsChecked {

  public static HashSet<String> isChecked = new HashSet<>();

  public static boolean isChecked() {
    List<String> scoreBoard = getScoreboard();
    String currentServer = MatchTimeDate.matchServer(cleanSB(scoreBoard.get(scoreBoard.size() - 1)));
    return isChecked.contains(currentServer);
  }
}
