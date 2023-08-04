package com.dillo.config;

import com.dillo.gui.hud.ModuleEditor;
import com.dillo.utils.previous.random.ids;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import java.io.File;

public class config extends Vigilant {

  // MAIN

  @Property(type = PropertyType.BUTTON, name = "Open Customize HUD GUI", description = "", category = "Main")
  public void displayHUD() {
    ids.mc.displayGuiScreen(new ModuleEditor());
  }

  @Property(
    type = PropertyType.SLIDER,
    name = "Head Speed",
    description = "Basically the speed of head movement of macro. (1 is fastest 10 is slowest)",
    category = "Main",
    min = 1,
    max = 1000
  )
  public static int headMovement = 400;

  @Property(
    type = PropertyType.SLIDER,
    name = "Head Movement Up Amount",
    description = "In the macro, it moves the head up as the swipe progresses here you can modify how high it goes.",
    category = "Main",
    min = 0,
    max = 100
  )
  public static int headMoveUp = 20;

  @Property(
    type = PropertyType.SELECTOR,
    name = "Mob Killer Weapon Type",
    description = "Customize what weapon the mob killer uses",
    category = "Main",
    options = { "Aurora Staff", "FireVeil", "ShortBow", "Hyperion", "Custom" }
  )
  public static int killerWeapon = 1;

  @Property(
    type = PropertyType.TEXT,
    name = "Custom Weapon",
    description = "Customize what weapon the mob killer uses",
    category = "Main"
  )
  public static String customWeapon = "";

  @Property(
    type = PropertyType.SWITCH,
    name = "Server Rotations",
    description = "Will server rotate instead of legit rotating. (doesnt rly do anything jst looks cool aka QOL)",
    category = "Main"
  )
  public static boolean serverRotations = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Render Route",
    description = "Basically renders the blocks you have on the route",
    category = "Main"
  )
  public static boolean renderRoute = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Include Mithril",
    description = "Will include mithril when dilloing.",
    category = "Main"
  )
  public static boolean isIncludeMithril = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Head Max Rotation",
    description = "The maximum rotation that the macro will take",
    category = "Main",
    min = 100,
    max = 500
  )
  public static int headRotationMax = 400;

  @Property(
    type = PropertyType.SLIDER,
    name = "Wait before starting the dillo movement (rod / drill switch delay)",
    description = "idk where to set this to but i assume its ab ur ping. (dont set too high tho). This module is made to prevent low procs bc of the dillo starting too fast and the dill not updating.",
    category = "Main",
    min = 1,
    max = 1000
  )
  public static int rod_drill_switch_time = 200;

  @Property(
    type = PropertyType.SLIDER,
    name = "Route Check % of gems exist.",
    description = "Glass % per vein.",
    category = "Main",
    min = 1,
    max = 100
  )
  public static int untouched = 20;

  @Property(
    type = PropertyType.SWITCH,
    name = "Actually Switch to aotv",
    description = "Switches to aotv on tp",
    category = "Main"
  )
  public static boolean actuallySwitchAOTV = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Faster Getting on dillo mode",
    description = "More Blatant (spam-clicks) but hopefully makes the process faster.",
    category = "Main"
  )
  public static boolean fasterDillo = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Anti-Spam",
    description = "Will COMPLETELY disable everything that macro sends in chat as a notification. This includes everything.",
    category = "Main"
  )
  public static boolean antiSpam = false;

  @Property(type = PropertyType.SWITCH, name = "Re-Fuel", description = "Will re-fuel drill", category = "Main")
  public static boolean refuel = true;

  @Property(
    type = PropertyType.SLIDER,
    name = "Ping",
    description = "Set ur ping here.",
    category = "Main",
    min = 1,
    max = 500
  )
  public static int ping = 80;

  //Route Utils
  @Property(
    type = PropertyType.SWITCH,
    name = "Route Check Check If able to tp",
    description = "WARNING THIS MAY LAG! Depending on ur route length this may lag.",
    category = "Route Utils"
  )
  public static boolean isAbleToTeleportChecks = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Fail CheckRoute Route Points Display",
    description = "If there are going to be any points on route that fail the route check, there is going to be a thing that will display them.",
    category = "Route Utils"
  )
  public static boolean failCheckRouteDisplay = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Alr Checked Lobby",
    description = "If u ran /checkRoute and it came out false in a lobby it will mark that lobby as checked and will display if u alr checked.",
    category = "Route Utils"
  )
  public static boolean alrCheckedLobby = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Alr checked lobby auto warp out",
    description = "Does /is if lobby is alr checked",
    category = "Route Utils"
  )
  public static boolean alrCheckedLobbyWarpOut = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Alr Checked Lobby Sound",
    description = "Plays annoying sound.",
    category = "Route Utils"
  )
  public static boolean alrCheckedLobbySound = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Render Big Text When Route Check can be used",
    description = "When all blocks are loaded (~~ when ur 120 blocks away from the furthest block)",
    category = "Route Utils"
  )
  public static boolean onRouteCheck = true;

  //Fun

  @Property(
    type = PropertyType.SWITCH,
    name = "Stop Rendering Armadillo",
    description = "Will stop rendering the armadillo.",
    category = "Fun"
  )
  public static boolean armadilloDefeatr = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Remove Proc Chat",
    description = "Will Remove procs :D.",
    category = "Fun"
  )
  public static boolean removeProcs = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Make armadillo big dildo",
    description = "Will make armadillo look like a dildo... (funny + will look... sussy...)",
    category = "Fun"
  )
  public static boolean armadildo = false;

  @Property(type = PropertyType.SWITCH, name = "Time / Vein display", description = "What it seems.", category = "Fun")
  public static boolean timeVein = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Early next block look",
    description = "Kinda QOL will just look at the next block while getting off dillo.",
    category = "Fun"
  )
  public static boolean earlyLook = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Show Server Look",
    description = "Will show how ur head rotates with server look.",
    category = "Fun"
  )
  public static boolean isShowServerLook = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Dillo Attempt to clear on the spot",
    description = "Basically will attempt to clear the vein on the spot",
    category = "Main"
  )
  public static boolean attemptToClearOnSpot = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Width Of Rendered Lines",
    description = "The lower, the thinner the line gets.",
    category = "Fun",
    min = 1,
    max = 10
  )
  public static int espWidth = 5;

  @Property(
    type = PropertyType.SELECTOR,
    name = "Color Of Block ESP",
    description = "Choose the colors.",
    category = "Fun",
    options = { "Red", "Green", "Blue" }
  )
  public static int blockESPColor = 0;

  @Property(
    type = PropertyType.SLIDER,
    name = "Render Route Block Dist",
    description = "Basically the distance to the rendered block (for FPS)",
    category = "Fun",
    min = 10,
    max = 70
  )
  public static int routeDist = 40;

  @Property(
    type = PropertyType.SWITCH,
    name = "TPS Overlay",
    description = "Turn on the TPS overlay (the position is not changeable YET)",
    category = "Fun"
  )
  public static boolean TPSOverlay = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Current Action Overlay",
    description = "Self Explanatory (for qol)",
    category = "Fun"
  )
  public static boolean currentActionOverlay = true;

  @Property(type = PropertyType.SWITCH, name = "Debug text", description = "Will show debug text.", category = "Fun")
  public static boolean debugText = false;

  // Teleportation Options

  @Property(
    type = PropertyType.SLIDER,
    name = "Check If on block time",
    description = "The time that the macro will check if ur on the next block, the lower this is the faster the macro can reroute / re-tp.",
    category = "Teleportation Options",
    min = 10,
    max = 100
  )
  public static int checkOnBlockTime = 50;

  @Property(
    type = PropertyType.SLIDER,
    name = "Smart Tp Range",
    description = "The range of Smart Tp Module -> (eg: 32 = 32x20x32) | the more u set this to the lower the scan time will be",
    category = "Teleportation Options",
    min = 10,
    max = 64
  )
  public static int smartTpRange = 32;

  @Property(
    type = PropertyType.SWITCH,
    name = "Smart Tp",
    description = "Actually finds a teleport 'path' to the next vein (through player placed cobblestones). WARNING! MAY TAKE A LONG TIME IN THE PREC AREA!",
    category = "Teleportation Options"
  )
  public static boolean smartTeleport = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Smart Tp depth",
    description = "The depth that the smart tp works. Depending on ur route u may need to customise this.",
    category = "Teleportation Options",
    min = 10,
    max = 64
  )
  public static int smartTpDepth = 10;

  @Property(
    type = PropertyType.SLIDER,
    name = "TP Wait Time",
    description = "Waits for ____ amount of ms.",
    category = "Teleportation Options",
    min = 1,
    max = 2000
  )
  public static int tpWait = 200;

  @Property(
    type = PropertyType.SLIDER,
    name = "Walk Forward when tping amount of time",
    description = "Customise for how many ticks u move forward (1 tick = 1/20 second)",
    category = "Teleportation Options",
    min = 1,
    max = 20
  )
  public static int forwardForTicks = 3;

  @Property(
    type = PropertyType.SLIDER,
    name = "Teleport Head Movement Speed",
    description = "The _____ of ms it takes for the macro to turn to the next block.",
    category = "Teleportation Options",
    min = 0,
    max = 2000
  )
  public static int tpHeadMoveSpeed = 200;

  @Property(
    type = PropertyType.SWITCH,
    name = "Within block radius check",
    description = "Might slow performance but what this does is it basically checks if the closest block to u is within 240 blocks. (this will prob work for admin bedrock boxing u 2)",
    category = "Teleportation Options"
  )
  public static boolean withinBlockRadiusChecks = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Re-Teleport",
    description = "If the macro will tp somewhere random (prev vers had that :> ) then it will attempt a re-tp",
    category = "Teleportation Options"
  )
  public static boolean reTeleport = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Re-Tp Amount Of times",
    description = "The amount of times that macro will attempt to re-teleport.",
    category = "Teleportation Options",
    min = 1,
    max = 5
  )
  public static int reTpTimes = 2;

  @Property(
    type = PropertyType.SWITCH,
    name = "Walk forward when TP",
    description = "Walks a bit when u tp. This A) makes admins less suspect of u and B) sometimes makes it so u can actually tp. WARNING MAY MESS UP SOME ROUTES!",
    category = "Teleportation Options"
  )
  public static boolean walkOnTP = false;

  // Failsafes

  @Property(
    type = PropertyType.SWITCH,
    name = "Turn on ticks failsafe.",
    description = "Ticks affect the macro in a lot of ways so i would recommend to turn this on.",
    category = "Failsafes"
  )
  public static boolean tickFailsafe = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Tick Failsafe trigger",
    description = "Happens rarely but ehhh. Basically the amount of ticks that the server has ( /sec) for the macro to stop and wait (untill ticks go back up) (changing is not recommended)",
    category = "Failsafes",
    min = 1,
    max = 20
  )
  public static int ticksFail = 12;

  @Property(
    type = PropertyType.SWITCH,
    name = "Restart Macro",
    description = "After a certain amount of time, if ur not doing anything the macro will restart itself.",
    category = "Failsafes"
  )
  public static boolean restartMacro = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Player Failsafe",
    description = "(ONG???) Player failsafe. :O (fr no swear)",
    category = "Failsafes"
  )
  public static boolean playerFailsafe = true;

  @Property(
    type = PropertyType.SLIDER,
    name = "Player Failsafe Range",
    description = "Range of player to trigger. (blocks)",
    category = "Failsafes",
    min = 1,
    max = 100
  )
  public static int playerFailsafeRange = 10;

  @Property(
    type = PropertyType.SWITCH,
    name = "Hack Accusations",
    description = "Auto answer messages containing ur name and words that u put in the file (there are defaults). Modify the file at './MiningInTwo/chatAnswers'.",
    category = "Failsafes"
  )
  public static boolean hackAccusationAnswer = false;

  // Online / Remote Control

  @Property(
    type = PropertyType.SWITCH,
    name = "Remote control",
    description = "THIS IS EXPERIMENTAL AND THE SITE IS STILL UNDER DEVELOPMENT. (AKA -> DOES NOT WORK)",
    category = "Online / Remote Control"
  )
  public static boolean remoteControl = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time between fetches",
    description = "THIS IS EXPERIMENTAL AND THE SITE IS STILL UNDER DEVELOPMENT. (AKA -> DOES NOT WORK)",
    category = "Online / Remote Control",
    min = 1,
    max = 60
  )
  public static int timeBetweenFetches = 30;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time between executions",
    description = "THIS IS EXPERIMENTAL AND THE SITE IS STILL UNDER DEVELOPMENT. (AKA -> DOES NOT WORK)",
    category = "Online / Remote Control",
    min = 1,
    max = 30
  )
  public static int timeBetweenExcecutions = 5;

  @Property(
    type = PropertyType.SLIDER,
    name = "Pause",
    description = "THIS IS EXPERIMENTAL AND THE SITE IS STILL UNDER DEVELOPMENT. (AKA -> DOES NOT WORK)",
    category = "Online / Remote Control",
    min = 1,
    max = 120
  )
  public static int timeForPause = 10;

  @Property(
    type = PropertyType.SLIDER,
    name = "Walk Time",
    description = "THIS IS EXPERIMENTAL AND THE SITE IS STILL UNDER DEVELOPMENT. (AKA -> DOES NOT WORK)",
    category = "Online / Remote Control",
    min = 1,
    max = 30
  )
  public static int walkTime = 10;

  // Structure Finder

  @Property(
    type = PropertyType.SWITCH,
    name = "Structure Scan",
    description = "THIS MAY NOT WORK I HAVENT TESTED BUT THIS IS BASICALLY THE GUMTUNE CLIENT STRUCTURE FINDER!",
    category = "Structure Scanner"
  )
  public static boolean stuctureFinder = false;

  // Overlay Options

  @Property(
    type = PropertyType.SLIDER,
    name = "Overlay X",
    description = "The pos of overlay in X direction.",
    category = "Overlay Options",
    min = 1,
    max = 700
  )
  public static int overlayX = 10;

  @Property(
    type = PropertyType.SLIDER,
    name = "Overlay Y",
    description = "The pos of overlay in Y direction.",
    category = "Overlay Options",
    min = 1,
    max = 700
  )
  public static int overlayY = 10;

  // Profit Tracker

  @Property(
    type = PropertyType.SWITCH,
    name = "Profit Tracker NPC price",
    description = "Instead of getting the price from bazaar it will get them from a stationary value - NPC sell price.",
    category = "Profit Tracker"
  )
  public static boolean npcPrice = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Hypixel Bazaar Fetches",
    description = "Basically if you turn this off the mod will not request hypixel bazaar data every ___ seconds and will only request it once and on start. (Turning this off means that the tracker will be less accurate.)",
    category = "Profit Tracker"
  )
  public static boolean hypixelBZFetches = true;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time between hypixel bz api fetches",
    description = "The lower u put this the the more lag ur gona get but more accurate rates. (seconds)",
    category = "Profit Tracker",
    min = 1,
    max = 1000
  )
  public static int hypixelBzFetchTime = 500;

  @Property(
    type = PropertyType.SLIDER,
    name = "Profit tracker size",
    description = "size || WARNING MAY NOT WORK PROPERLY! ",
    category = "Profit Tracker",
    min = 1,
    max = 10
  )
  public static int profitTrackerSize = 2;

  //  Nuker

  @Property(
    type = PropertyType.SLIDER,
    name = "Nuker BPS",
    description = "IF U PUT THIS TO OVER 20 THIS MAY KICK U TO LOBBY! IDK Y BUT IT HAPPENED TO ME A FEW TIMES! I RECOMMEND U USE THE LEGIT CLEAR MODULE INSTEAD!",
    category = "Nuker",
    min = 20,
    max = 80
  )
  public static int nukerBPS = 20;

  @Property(
    type = PropertyType.SWITCH,
    name = "Place Cobble | DONT USE!",
    description = "WARNING DID NOT TEST!! DO NOT USE! (hypothetically should work BUT I DID NOT TEST CAS NO ALTS)",
    category = "Nuker"
  )
  public static boolean isNukerPlaceCobble = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Polar Nuker Detection",
    description = "Basically detects blocks like polar faster setup time but more obv that ur macroin. (also does not dig out under vein cas i dont want u to use this :D)",
    category = "Nuker"
  )
  public static boolean polarBlockDetection = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Nuker Server Rotations",
    description = "As far as ik this is purely QOL and will not flag. BUT that is prob just me idk SO BE CAREFULL!",
    category = "Nuker"
  )
  public static boolean nukerServerRotations = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Nuker Range",
    description = "The block range of the nuker",
    category = "Nuker",
    min = 1,
    max = 5
  )
  public static int nukerRange = 4;

  @Property(
    type = PropertyType.SWITCH,
    name = "Dig out under vein",
    description = "Will also make a 'little' hole around and under the cord in order for bad mobs to go there instead of hitting u.",
    category = "Nuker"
  )
  public static boolean nukerDigUnder = true;

  @Property(
    type = PropertyType.SWITCH,
    name = "Check if unobstructed",
    description = "WARNING! THIS MAY CAUSE MASSIVE AMOUNTS OF LAG!",
    category = "Nuker"
  )
  public static boolean nukerUnObstructedChecks = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Nuker FOV",
    description = "Nuker field of view to actually nuke. I think this works. I could not find a scenario where it does not.",
    category = "Nuker",
    min = 20,
    max = 360
  )
  public static int nukerFOV = 150;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time/Vein X",
    description = ".",
    category = "Locations",
    min = 0,
    max = 1000
  )
  public static int timeVeinX = 150;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time/Vein Y",
    description = ".",
    category = "Locations",
    min = 0,
    max = 1000
  )
  public static int timeVeinY = 150;

  @Property(
    type = PropertyType.SLIDER,
    name = "Profit tracker X",
    description = "X",
    category = "Locations",
    min = 0,
    max = 1000
  )
  public static int profitTrackerX = 50;

  @Property(
    type = PropertyType.SLIDER,
    name = "Profit tracker Y",
    description = "Y",
    category = "Locations",
    min = 0,
    max = 1000
  )
  public static int profitTrackerY = 50;

  // TO DO:

  @Property(
    type = PropertyType.SLIDER,
    name = "Restart Macro after",
    description = "Set the amount of seconds you want the macro restart failsafe to be triggered. (may not work)",
    category = "Failsafes",
    min = 1,
    max = 30
  )
  public static int restartTrigerTime = 10;

  public static config INSTANCE = new config();

  public config() {
    super(new File("./config/MiningInTwoConf.toml"));
    initialize();
  }
}
