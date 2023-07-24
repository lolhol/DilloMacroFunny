package com.dillo.main.failsafes;

import static com.dillo.calls.CurrentState.ANSWER_ACCUSATION;
import static com.dillo.config.config.hackAccusationAnswer;
import static com.dillo.main.files.readwrite.WriteFile.gson;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.main.teleport.utils.WaitThenCall;
import com.dillo.remote.GetRemoteControl;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.random.ids;
import com.google.common.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnswerPPL {

  public static File answersFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json");
  public static List<List<String>> returnList = new ArrayList<>();
  public static boolean answering = false;
  public static CurrentState prevState = null;
  public static String messageThatTriggered = null;

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (hackAccusationAnswer && !answering && ArmadilloStates.isOnline()) {
      String message = event.message.getUnformattedText();
      //SendChat.chat(message + "!!!");

      for (List<String> accusation : returnList) {
        if (message.contains(accusation.get(0)) && message.contains(GetRemoteControl.getCurrentUsername())) {
          //SendChat.chat("DETECTED ACCUSATION");
          detectedAccusation(accusation.get(1));
          return;
        }
      }
    }
  }

  private static void detectedAccusation(String message) {
    answering = true;
    prevState = ArmadilloStates.currentState;
    ArmadilloStates.currentState = null;
    messageThatTriggered = message;
    WaitThenCall.waitThenCall(message.length() * 200L, ANSWER_ACCUSATION);
  }

  public static void answerAccusation() {
    ids.mc.thePlayer.addChatMessage(new ChatComponentText(messageThatTriggered));
    answering = false;
    ArmadilloStates.currentState = prevState;
  }

  public static void makeAcusation(File answersFile) {
    StringBuilder content = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(answersFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!content.toString().equals("null") && !content.toString().equals("") && !content.toString().equals(" ")) {
      Type listType = new TypeToken<List<List<String>>>() {}.getType();
      List<List<String>> array = gson.fromJson(content.toString(), listType);
      returnList = array;
    }
  }
}
