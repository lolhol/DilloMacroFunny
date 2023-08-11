package com.dillo.commands.AnswerCommands;

import com.dillo.main.failsafes.AnswerPPL;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static com.dillo.main.files.init.CheckFile.answersFile;
import static com.dillo.main.files.init.CheckFile.writeStringFile;
import static com.dillo.main.files.readwrite.ReWriteFile.gson;

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
