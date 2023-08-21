package com.dillo.commands.HelpCommands;

import com.dillo.utils.previous.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class MainHelp extends Command {

    public MainHelp() {
        super("mitHelp");
    }

    @DefaultHandler
    public void handle() {
        SendChat.chat("------- Mining In Two help -------");
        SendChat.chat("                                  ");
        SendChat.chat("                                  ");
        SendChat.chat("+++++ Essential +++++");
        SendChat.chat("/MITmenu -> opens menu.");
        SendChat.chat("/block -> adds a block to route.");
        SendChat.chat("/clear -> clears the route.");
        SendChat.chat("/createRoute <route name> -> will make a new route and select it.");
        SendChat.chat("/selectRoute <name> -> will select the route if the one you provided exists in the folder.");
        SendChat.chat("/avgPercent -> give avg % of gems per route block.");
        SendChat.chat("/addConfig <name> -> will create a config file with that name.");
        SendChat.chat("/selectConfig <name> -> selects a config with that name.");
        SendChat.chat("----------------------------------");
        SendChat.chat("/insertInMiddle <number> -> will insert the block you are standing on to the route.");
        SendChat.chat("/removeBlock <number> -> removes the block from the route that you specified.");
        SendChat.chat("/replaceBlock <number> -> replaces that block with the one you are standing on.");
        SendChat.chat("+++++++++++++++++++++");
        SendChat.chat("                                  ");
        SendChat.chat("+++++ RouteCheck +++++");
        SendChat.chat("/addStructure -> creates a new structure point to later be used in structure checks.");
        SendChat.chat("/clearStructures -> deletes all the structures that you have previously added.");
        SendChat.chat("/removeStructure <point> -> will remove a structure at a specific point.");
        SendChat.chat("/structurePoints -> will display a point at every structure that you added.");
        SendChat.chat(
                "/checkRoute -> WARNING! MAY LAG! This is a complicated process run /helpStructureCheck to see how works."
        );
        SendChat.chat("+++++++++++++++++++++");
        SendChat.chat("                                  ");
        SendChat.chat("+++++ RouteCommands +++++");
        SendChat.chat("/currentRoute -> says the name of the route currently selected.");
        SendChat.chat("/deleteRoute -> deletes the route completely (including the file).");
        SendChat.chat("/currentRoutes -> will show all the routes that you currently have.");
        SendChat.chat(
                "/importRoute <name of new route> -> will attempt to make a new route with whatever you have in ur clipboard"
        );
        SendChat.chat(
                "/importFromWeb <link> <name of new route> -> WARNING! WORKS WITH ONLY PASTERBIN.COM. Does the same thing as /importRoute but from website."
        );
        SendChat.chat("+++++++++++++++++++++");
        SendChat.chat("                                  ");
        SendChat.chat("+++++ RouteClear +++++");
        SendChat.chat(
                "/helperLines -> will display route-clear-help lines to help you clear legit (why is this here? just use /clearRouteLegit."
        );
        SendChat.chat("/clearLegit -> will display the blocks you have to break in order for the route to be clear.");
        SendChat.chat("+++++++++++++++++++++");
        SendChat.chat("                                  ");
        SendChat.chat("+++++ FailSafes +++++");
        SendChat.chat(
                "/addAccusation <detection> <answer> -> this will add an accusation to the list. AKA if player says something that contains (detection) then the macro will respond with (answer)."
        );
        SendChat.chat(
                "/removeAccusation <detection> -> will remove an accusation. Yes, u have to put the EXACT 'detection'."
        );
        SendChat.chat("                                  ");
        SendChat.chat("                                  ");
        SendChat.chat("------- Mining In Two help -------");
    }
}
