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

public class AddAnswer extends Command {

  public AddAnswer() {
    super("addAccusation");
  }

  @DefaultHandler
  public void handle(String detection, String answer) {
    List<String> returnList = new ArrayList<>();

    detection = detection.replace("*", " ");
    returnList.add(detection);

    answer = answer.replace("*", " ");
    returnList.add(answer);

    AnswerPPL.returnList.add(returnList);
    writeStringFile(answersFile, gson.toJson(AnswerPPL.returnList));

    SendChat.chat(prefix.prefix + "Added! (btw use '*' if you want to have spaces)");
  }
}
