package com.dillo.commands.AnswerCommands;

import static com.dillo.dilloUtils.BlockUtils.fileUtils.ReWriteFile.gson;
import static com.dillo.dilloUtils.CheckFile.answersFile;
import static com.dillo.dilloUtils.CheckFile.writeStringFile;

import com.dillo.dilloUtils.FailSafes.AnswerPPL;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class RemoveAccusation extends Command {

  public RemoveAccusation() {
    super("removeAccusation");
  }

  @DefaultHandler
  public void handle(String detection) {
    detection = detection.replace("*", " ");

    for (List<String> list : AnswerPPL.returnList) {
      if (list.get(0).equals(detection)) {
        AnswerPPL.returnList.remove(list);
        SendChat.chat(prefix.prefix + "Removed! (btw use '*' if you want to have spaces)");
        break;
      }
    }

    writeStringFile(answersFile, gson.toJson(AnswerPPL.returnList));
  }
}
