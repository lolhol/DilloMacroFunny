package com.dillo.commands.HelpCommands;

import com.dillo.utils.previous.chatUtils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class HelpStructureCheck extends Command {

    public HelpStructureCheck() {
        super("helpStructureCheck");
    }

    @DefaultHandler
    public void handle() {
        SendChat.chat("++++++++++++ Route Checker Guide ++++++++++++");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("------------ What is it? ------------");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat(
                "This module was designed by me in order to not have to spend like 10h setting up a route just to realize that it has a structure in its way and thus is unusable. Depending on how many structures you added and how many points you have on your route this may LAG."
        );
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("-------- How does this work? --------");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("In order to make sure that this is as accurate as possible there are two parts to this -> ");
        SendChat.chat(" ");
        SendChat.chat(
                "1) First it will go over all your route points and make sure that at least 60% of the blocks in the 3x3x4 area above the block are gemstones."
        );
        SendChat.chat(" ");
        SendChat.chat(
                "2) Second if ALL gems are there (from step one) it will go on to the 'Structure points' that you have added previously. For this it will check about a 15x15x20 area and if about > 50% of the blocks are 'unnatural' then it will alert you that there is a structure. (aka: 'There is a high chance that there is a structure int the routes way...')."
        );
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("------------ How to use? ------------");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat(
                "This module relies on structures spawning in a ~similar spot. For that reason if you see an un added structure that prevents you from mining your route, you can add it with a command (/mitHelp). BUT WAIT THERE IS A SPECIFIC WAY TO ADD A STRUCTURE!"
        );
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("------- How to add structure? -------");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("In order for you to add a structure to the list you want to first find a structure.");
        SendChat.chat(
                "Then it is best if you bridge out to the ~middle of the structure and add a point there. If the structure is big then you might wana add more then one point."
        );
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat(" ");
        SendChat.chat("++++++++++++ Route Checker Guide ++++++++++++");
    }
}
