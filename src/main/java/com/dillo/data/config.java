package com.dillo.data;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import java.io.File;

public class config extends Vigilant {

  /*@Property(
            type = PropertyType.SWITCH, name = "Top-To-Bottom", description = "Mine Gems form top to bottom (turn on if u want money!)", category = "Main"
    )
    public static boolean topToBottom = false;

    @Property(
            type = PropertyType.SLIDER, name = "Dillo Block Cut-off", description = "How many gems have to remain before going on to the next vein", category = "Main", min = 1, max = 5
    )
    public static boolean dilloCutOff = false;*/

  @Property(
    type = PropertyType.SWITCH,
    name = "Render Route",
    description = "Basically renders the blocks you have on the route",
    category = "Main"
  )
  public static boolean renderRoute = true;

  @Property(
          type = PropertyType.SWITCH,
          name = "Alr Checker Lobby",
          description = "If u ran /checkRoute and it came out false in a lobby it will mark that lobby as checked and will display if u alr checked.",
          category = "Main"
  )
  public static boolean alrCheckedLobby = true;

  @Property(
          type = PropertyType.SWITCH,
          name = "Render Big Text When Route Check can be used",
          description = "When all blocks are loaded (~~ when ur 200 blocks away from the furthest block)",
          category = "Main"
  )
  public static boolean onRouteCheck = true;

  @Property(
    type = PropertyType.SLIDER,
    name = "Render Route Block Dist",
    description = "Basically the distance to the rendered block (for FPS)",
    category = "Main",
    min = 10,
    max = 70
  )
  public static int routeDist = 40;

  @Property(
    type = PropertyType.SLIDER,
    name = "Width Of Rendered Lines",
    description = "The lower, the thinner the line gets.",
    category = "Main",
    min = 1,
    max = 10
  )
  public static int espWidth = 5;

  @Property(
    type = PropertyType.SELECTOR,
    name = "Color Of Block ESP",
    description = "Choose the colors.",
    category = "Main",
    options = { "Red", "Green", "Blue" }
  )
  public static int blockESPColor = 0;

  @Property(
    type = PropertyType.SLIDER,
    name = "TP Wait Time",
    description = "Waits for ____ amount of ms.",
    category = "Main",
    min = 0,
    max = 2000
  )
  public static int tpWait = 200;

  @Property(
    type = PropertyType.SLIDER,
    name = "Teleport Head Movement Speed",
    description = "The _____ of ms it takes for the macro to turn to the next block.",
    category = "Main",
    min = 0,
    max = 2000
  )
  public static int tpHeadMoveSpeed = 200;

  @Property(
    type = PropertyType.SLIDER,
    name = "Head Speed",
    description = "Basically the speed of head movement of macro. (1 is fastest 10 is slowest)",
    category = "Main",
    min = 1,
    max = 10
  )
  public static int headMovement = 9;

  @Property(
    type = PropertyType.SWITCH,
    name = "Ignore Panes",
    description = "Might make the macro more efficient dono.",
    category = "Main"
  )
  public static boolean ignorePanes = true;

  @Property(
    type = PropertyType.SLIDER,
    name = "Dillo Cutoff",
    description = "Basically the amount of blocks that remain for the dillo to start TP. (dono how good it is tho)",
    category = "Main",
    min = 1,
    max = 10
  )
  public static int cutOff = 1;

  @Property(
    type = PropertyType.SLIDER,
    name = "Distance to ground when unmounting",
    description = "The distance to ground for the macro to go to the next tp stage.",
    category = "Main",
    min = 1,
    max = 10
  )
  public static int distanceToGround = 4;

  @Property(
    type = PropertyType.SWITCH,
    name = "New head movement system.",
    description = "false -> old, true -> new | Dono if this is better but ehg.",
    category = "Main"
  )
  public static boolean newHeadMovement = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Tick Failsafe trigger",
    description = "Happens rarely but ehhh. Basically the amount of ticks that the server has ( /sec) for the macro to stop and wait (untill ticks go back up) (changing is not recommended)",
    category = "Main",
    min = 1,
    max = 20
  )
  public static int ticksFail = 12;

  @Property(
    type = PropertyType.SWITCH,
    name = "Turn on ticks failsafe.",
    description = "Ticks affect the macro in a lot of ways so i would recommend to turn this on.",
    category = "Main"
  )
  public static boolean tickFailsafe = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "TPS Overlay",
    description = "Turn on the TPS overlay (the position is not changeable YET)",
    category = "Main"
  )
  public static boolean TPSOverlay = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Ignore 'already broken' blocks.",
    description = "Basically like 'skip block break progression' but for dillo mining. THIS COULD SLOW DOWN U!",
    category = "Main"
  )
  public static boolean blockBreakSkip = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Distance to detect on dillo",
    description = "Dono if this affects anything but the more u put this, the more time (hypothetically) and more reliable the macro will get on the dillo.",
    category = "Main",
    min = 1,
    max = 10
  )
  public static int onDilloDist = 1;

  @Property(
    type = PropertyType.SWITCH,
    name = "Throw rod on startup.",
    description = "Throws rod and waits a bit on startup. Dono y u would need this but this is to make sure you are OFF the armadillo when u start.",
    category = "Main"
  )
  public static boolean throwRodOnStartup = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Remote control",
    description = "Turns on the remote control via the website. THIS WILL NOT WORK IF U DIDNT PAY ME (L git good).",
    category = "Main"
  )
  public static boolean remoteControl = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time between fetches",
    description = "The time between the macro requests the information from the server. This could potentially be deadly to my server if set to too low (dono tho) and ur wifi. (This is in seconds)",
    category = "Main",
    min = 1,
    max = 60
  )
  public static int timeBetweenFetches = 30;

  @Property(
    type = PropertyType.SLIDER,
    name = "Time between executions",
    description = "The time between the macro executes the commands it received from the server. (seconds)",
    category = "Main",
    min = 1,
    max = 30
  )
  public static int timeBetweenExcecutions = 5;

  @Property(
    type = PropertyType.SLIDER,
    name = "Pause",
    description = "The time of the macro pause when triggered. (seconds)",
    category = "Main",
    min = 1,
    max = 120
  )
  public static int timeForPause = 10;

  @Property(
    type = PropertyType.SLIDER,
    name = "Walk Time",
    description = "Applies for anything that relates to walking in the macro. (seconds)",
    category = "Main",
    min = 1,
    max = 30
  )
  public static int walkTime = 10;

  @Property(
    type = PropertyType.SWITCH,
    name = "Restart Macro",
    description = "After a certain amount of time, if ur not doing anything the macro will restart itself.",
    category = "Main"
  )
  public static boolean restartMacro = false;

  @Property(
    type = PropertyType.SWITCH,
    name = "Smart Tp",
    description = "Actually finds a teleport 'path' to the next vein. PAID ONLY.",
    category = "Main"
  )
  public static boolean smartTeleport = false;

  @Property(
    type = PropertyType.SLIDER,
    name = "Restart Macro after",
    description = "Set the amount of seconds you want the macro restart failsafe to be triggered",
    category = "Main",
    min = 1,
    max = 30
  )
  public static int restartTrigerTime = 10;

  @Property(
          type = PropertyType.SWITCH,
          name = "Walk forward when TP",
          description = "Walks a bit when u tp. This A) makes admins less suspect of u and B) sometimes makes it so u can actually tp. WARNING MAY MESS UP SOME ROUTES!",
          category = "Main"
  )
  public static boolean walkOnTP = false;

  @Property(
          type = PropertyType.SWITCH,
          name = "Structure Scan",
          description = "Scan for dillo structures",
          category = "Main"
  )
  public static boolean stuctureFinder = false;

  @Property(
          type = PropertyType.SLIDER,
          name = "Angle Rotation",
          description = "Rotated angle be4 moving on.",
          category = "Main",
          min = 1,
          max = 1440
  )
  public static int angleRotation = 10;

  @Property(
          type = PropertyType.SWITCH,
          name = "Current Action Overlay",
          description = "Self Explanatory (for qol)",
          category = "Main"
  )
  public static boolean currentActionOverlay = true;

  @Property(
          type = PropertyType.SLIDER,
          name = "Overlay X",
          description = "The pos of overlay in X direction.",
          category = "Main",
          min = 1,
          max = 700
  )
  public static int overlayX = 10;

  @Property(
          type = PropertyType.SLIDER,
          name = "Overlay Y",
          description = "The pos of overlay in Y direction.",
          category = "Main",
          min = 1,
          max = 700
  )
  public static int overlayY = 10;

  @Property(
          type = PropertyType.SWITCH,
          name = "Player Failsafe",
          description = "(ONG???) Player failsafe. :O (fr no swear)",
          category = "Main"
  )
  public static boolean playerFailsafe = true;

  @Property(
          type = PropertyType.SLIDER,
          name = "Player Failsafe Range",
          description = "Range of player to trigger. (blocks)",
          category = "Main",
          min = 1,
          max = 100
  )
  public static int playerFailsafeRange = 10;

  @Property(
          type = PropertyType.SWITCH,
          name = "Hack Accusations",
          description = "Auto answer messages containing ur name and words that u put in the file (there are defaults). Modify the file at './MiningInTwo/chatAnswers'.",
          category = "Main"
  )
  public static boolean hackAccusationAnswer = false;

  @Property(
          type = PropertyType.SWITCH,
          name = "Hypixel Bazaar Fetches",
          description = "Basically if you turn this off the mod will not request hypixel bazaar data every ___ seconds and will only request it once and on start. (Turning this off means that the tracker will be less accurate.)",
          category = "Main"
  )
  public static boolean hypixelBZFetches = true;

  @Property(
          type = PropertyType.SLIDER,
          name = "Time between hypixel bz api fetches",
          description = "The lower u put this the the more lag ur gona get but more accurate rates. (seconds)",
          category = "Main",
          min = 1,
          max = 1000
  )
  public static int hypixelBzFetchTime = 500;

  @Property(
          type = PropertyType.SLIDER,
          name = "Profit tracker X",
          description = "X",
          category = "Main",
          min = 1,
          max = 1000
  )
  public static int profitTrackerX = 50;

  @Property(
          type = PropertyType.SLIDER,
          name = "Profit tracker Y",
          description = "Y",
          category = "Main",
          min = 1,
          max = 1000
  )
  public static int profitTrackerY = 50;

  @Property(
          type = PropertyType.SLIDER,
          name = "Profit tracker size",
          description = "size",
          category = "Main",
          min = 1,
          max = 10
  )
  public static int profitTrackerSize = 2;

  @Property(
          type = PropertyType.SLIDER,
          name = "Nuker BPS",
          description = "BPS",
          category = "Main",
          min = 20,
          max = 80
  )
  public static int nukerBPS = 20;

  @Property(
          type = PropertyType.SWITCH,
          name = "Check if unobstructed",
          description = "Nuker checks to see if block is visible (eg -> if u can break, the nuker can break)",
          category = "Main"
  )
  public static boolean nukerUnObstructedChecks = false;

  @Property(
          type = PropertyType.SWITCH,
          name = "Nuker actually look",
          description = "The neker will actually look at the block being nuked (very anoying ik but it is what it is)",
          category = "Main"
  )
  public static boolean nukerActualLook = false;

  @Property(
          type = PropertyType.SLIDER,
          name = "Nuker FOV",
          description = "Nuker field of view to actually nuke.",
          category = "Main",
          min = 20,
          max = 360
  )
  public static int nukerFOV = 20;

  @Property(
          type = PropertyType.SWITCH,
          name = "Re-Teleport",
          description = "If the macro will tp somewhere random (prev vers had that :<) then it will attempt a re-tp",
          category = "Main"
  )
  public static boolean reTeleport = false;

  @Property(
          type = PropertyType.SLIDER,
          name = "Re-Tp Amount Of times",
          description = "The amount of times that macro will attempt to re-teleport.",
          category = "Main",
          min = 1,
          max = 5
  )
  public static int reTpTimes = 2;

  @Property(
          type = PropertyType.SWITCH,
          name = "Time / Vein display",
          description = "What it seems.",
          category = "Main"
  )
  public static boolean timeVein = true;

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
          type = PropertyType.SWITCH,
          name = "Include Mithril",
          description = "What it seems.",
          category = "Main"
  )
  public static boolean includeMithril = true;

  public static config INSTANCE = new config();

  public config() {
    super(new File("./config/MiningInTwoConf.toml"));
    initialize();
  }
}
