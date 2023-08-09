package com.dillo.gui.GUIUtils.MatchServer;

import java.util.HashSet;
import java.util.List;

import static com.dillo.utils.ScoreboardUtils.GetCurArea.cleanSB;
import static com.dillo.utils.ScoreboardUtils.GetCurArea.getScoreboard;

public class IsChecked {

    public static HashSet<String> isChecked = new HashSet<>();

    public static boolean isChecked() {
        List<String> scoreBoard = getScoreboard();

        String currentServer = null;
        if (scoreBoard.size() > 0) {
            currentServer = MatchTimeDate.matchServer(cleanSB(scoreBoard.get(scoreBoard.size() - 1)));
        }

        return currentServer != null && isChecked.contains(currentServer);
    }
}
