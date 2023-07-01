package com.dillo.utils.ScoreboardUtils;

import com.dillo.utils.StringUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.scoreboard.*;

public class GetCurArea {

  public static String getArea() {
    SendChat.chat(cleanSB(getScoreboard().get(getScoreboard().size() - 1)));

    return "";
  }

  public static String cleanSB(String scoreboard) {
    char[] nvString = StringUtils.removeFormatting(scoreboard).toCharArray();
    StringBuilder cleaned = new StringBuilder();

    for (char c : nvString) {
      if ((int) c > 20 && (int) c < 127) {
        cleaned.append(c);
      }
    }

    return cleaned.toString();
  }

  @SuppressWarnings({ "ExecutionException", "IllegalArgumentException" })
  public static List<String> getScoreboard() {
    List<String> lines = new ArrayList<>();
    if (ids.mc.theWorld == null) return lines;
    Scoreboard scoreboard = ids.mc.theWorld.getScoreboard();
    if (scoreboard == null) return lines;

    ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
    if (objective == null) return lines;

    Collection<Score> scores = scoreboard.getSortedScores(objective);
    List<Score> list = scores
      .stream()
      .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#"))
      .collect(Collectors.toList());

    if (list.size() > 15) {
      scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
    } else {
      scores = list;
    }

    for (Score score : scores) {
      ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
      lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
    }

    return lines;
  }
}
