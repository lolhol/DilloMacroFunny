package com.dillo;

import com.dillo.calls.ArmadilloMain;
import com.dillo.commands.*;
import com.dillo.commands.AnswerCommands.AddAnswer;
import com.dillo.commands.AnswerCommands.RemoveAccusation;
import com.dillo.commands.ConfigCommands.AddConfig;
import com.dillo.commands.ConfigCommands.SelectConfig;
import com.dillo.commands.HelpCommands.HelpStructureCheck;
import com.dillo.commands.HelpCommands.MainHelp;
import com.dillo.commands.RouteCommands.*;
import com.dillo.commands.RouteMakerUtils.CalcRouteAvgGemPerc;
import com.dillo.commands.RouteMakerUtils.CheckIfCanTpToEvery;
import com.dillo.commands.RouteMakerUtils.GemESP;
import com.dillo.commands.UtilCommands.DetectEntityUnderCommand;
import com.dillo.commands.UtilCommands.RouteChecker;
import com.dillo.commands.UtilCommands.StartClearLegit;
import com.dillo.commands.UtilCommands.Test;
import com.dillo.commands.baritone.StartAutoSetupWithBaritone;
import com.dillo.commands.baritone.WalkToBlockWithBaritone;
import com.dillo.config.AutoSaveConfig;
import com.dillo.events.*;
import com.dillo.gui.GUIUtils.CurRatesUtils.GetCurGemPrice;
import com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.gui.GUIUtils.CurTimeVein.CurTime;
import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.Overlay;
import com.dillo.gui.hud.ModuleEditorTrigger;
import com.dillo.gui.overlays.overlay.AlrCheckedLobby;
import com.dillo.gui.overlays.overlay.OnRouteCheck;
import com.dillo.gui.overlays.overlay.ProfitTracker;
import com.dillo.gui.overlays.overlay.TimePerVein;
import com.dillo.keybinds.Keybinds;
import com.dillo.main.esp.chat.FilterChat;
import com.dillo.main.esp.other.BigDildoDillo;
import com.dillo.main.esp.other.StopRenderStand;
import com.dillo.main.esp.route.BlockOnRouteESP;
import com.dillo.main.failsafes.AminStuff.BedrockFail;
import com.dillo.main.failsafes.AminStuff.WarpOutFail;
import com.dillo.main.failsafes.*;
import com.dillo.main.failsafes.RouteFailsafes.RemoveBlockFailsafe;
import com.dillo.main.files.init.CheckFile;
import com.dillo.main.macro.main.GetOffArmadillo;
import com.dillo.main.macro.main.StateDillo;
import com.dillo.main.macro.refuel.ReFuelDrill;
import com.dillo.main.macro.refuel.ReFuelDrillTriger;
import com.dillo.main.macro.refuel.ThrowAtEnd;
import com.dillo.main.route.AutoSetup.SetupMain;
import com.dillo.main.route.LegitRouteClear.LegitRouteClear;
import com.dillo.main.route.MobKiller.MobKillerMain;
import com.dillo.main.route.Nuker.NukerMain;
import com.dillo.main.route.PlaceBlocks.PlaceCobbleModule;
import com.dillo.main.route.RouteDeletr.RouteDeletrMain;
import com.dillo.main.route.ViewClearLines.ViewClearLines;
import com.dillo.main.teleport.TeleportMovePlayer.MoveToVertex;
import com.dillo.main.teleport.utils.*;
import com.dillo.main.utils.JumpProgressRegister;
import com.dillo.main.utils.funny.CancelAllBadSounds;
import com.dillo.main.utils.jump.GetProjectedTime;
import com.dillo.main.utils.looks.DriveLook;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.main.utils.looks.YawLook;
import com.dillo.pathfinding.Brigeros.DestroyBlock;
import com.dillo.pathfinding.Brigeros.WalkOnPath;
import com.dillo.remote.*;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.renderUtils.renderModules.*;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.dillo.main.failsafes.AnswerPPL.makeAcusation;

@Mod(modid = "autogg", name = "autogg", version = "1.0.0", clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class armadillomacro {

  public static void main(String[] args) {
    new armadillomacro().init(null);
  }

  public static MobKillerMain mobKiller = new MobKillerMain();
  public static RouteDeletrMain destroyer = new RouteDeletrMain();
  public static MoveToVertex vertexMover = new MoveToVertex();
  public static File modFile = null;
  public static ArrayList<KeyBinding> keybinds = new ArrayList<>();
  public static List<Element> allOverlays = new ArrayList<>();
  public static JumpProgressRegister regJump = new JumpProgressRegister();
  private static EventManager eventManager;
  private static ModEventProducer eventProducer;

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    Display.setTitle("MiningInTwo");

    CheckFile.checkFiles();
    keybinds.add(new KeyBinding("Enable Macro", Keyboard.KEY_NONE, "Mining In Two"));
    keybinds.add(new KeyBinding("Enable Nuker", Keyboard.KEY_NONE, "Mining In Two"));
    keybinds.add(new KeyBinding("Quick View Structures", Keyboard.KEY_NONE, "Mining In Two"));
    keybinds.add(new KeyBinding("Add Point", Keyboard.KEY_NONE, "Mining In Two"));
    keybinds.add(new KeyBinding("Test Key", Keyboard.KEY_NONE, "Mining In Two"));

    // Comment

    registerCommands(
      new StartMacroCommand(),
      new DetectEntityUnderCommand(),
      new AddBlockRouteCommand(),
      new ClearBlockRoute(),
      new SelectRouteCommand(),
      new NewRouteCommand(),
      new OpenGUI(),
      new CurrentSelected(),
      new RoutesInFolder(),
      new AddAnswer(),
      new RemoveAccusation(),
      new ImportFromClipboard(),
      new DeleteRoute(),
      new ImportFromWeb(),
      new RouteChecker(),
      new AddStucture(),
      new RemoveStructure(),
      new StructurePoints(),
      new ClearStructures(),
      new StartClearLegit(),
      new ViewHelperLines(),
      new MainHelp(),
      new HelpStructureCheck(),
      new InsertInMiddle(),
      new RemoveBlockRoute(),
      new ReplaceBlockRoute(),
      new ObstructedPoints(),
      new AddConfig(),
      new SelectConfig(),
      new CalcRouteAvgGemPerc(),
      new CheckIfCanTpToEvery(),
      new GemESP(),
      new WalkToBlockWithBaritone(),
      new StartAutoSetupWithBaritone(),
      new RouteDestroyr(destroyer),
      new ImportRouteFromWebsite()
    );

    try {
      registerEvents(
        new LookAt(),
        new ArmadilloMain(),
        new StateDillo(),
        new RenderSingleLineTwoPoints(),
        new RenderOneBlockMod(),
        new RenderMultipleBlocksMod(),
        new RenderPoints(),
        new BlockOnRouteESP(),
        new WaitThenCall(),
        new IsOnBlock(),
        new RenderMultipleLines(),
        new WalkOnPath(),
        new DestroyBlock(),
        new Keybinds(),
        new GetOffArmadillo(),
        new ServerTPSFailsafe(),
        new OverlayMod(),
        new LookWhileGoingDown(),
        new GetRemoteControl(),
        new PauseMacro(),
        new Movements(),
        new PlayerFailsafe(),
        new AnswerPPL(),
        new ItemsPickedUp(),
        new RemoteControl(),
        new RemoteControlChat(),
        new UsePathfinderInstead(),
        new NukerMain(),
        new Overlay(),
        new CurTime(),
        new StructurePoints(),
        new LegitRouteClear(),
        new ViewClearLines(),
        new WalkForward(),
        new Test(),
        new TooFarAwayFailsafe(),
        new ReFuelDrill(),
        new ReFuelDrillTriger(),
        new ThrowAtEnd(),
        new YawLook(),
        new PassReNew(),
        new DriveLook(),
        new TeleportToBlock(),
        new PlaceCobbleModule(),
        new SetupMain(),
        destroyer,
        vertexMover,
        mobKiller,
        new WarpOutFail(),
        new RemoveBlockFailsafe(),
        new PlayerLocChangeTrigger(),
        regJump,
        new GetProjectedTime(),
        new StopRenderStand(),
        new BigDildoDillo(),
        new FilterChat(),
        new ModuleEditorTrigger(),
        new AutoSaveConfig(),
        new BedrockFail(),
        new CancelAllBadSounds()
      );
    } catch (NoClassDefFoundError e) {
      System.out.println(Arrays.toString(e.getStackTrace()) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    registerKeybinds(keybinds);
    makeAcusation(new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json"));

    new Thread(GetCurGemPrice::getCurrGemPrice).start();

    //////////////////////////////////
    // STEVEBOT PATHFINDING MODULE  //
    /////////////////////////////////

    eventProducer.onInit();
    addAllOverlays();
  }

  public void addAllOverlays() {
    allOverlays.add(new TimePerVein());
    allOverlays.add(new ProfitTracker());
    allOverlays.add(new OnRouteCheck());
    allOverlays.add(new AlrCheckedLobby());
  }

  @Mod.EventHandler
  public void postFMLInitialization(FMLPostInitializationEvent event) {
    LocalDateTime now = LocalDateTime.now();
    Duration initialDelay = Duration.between(now, now);
    long initialDelaySeconds = initialDelay.getSeconds();

    ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    threadPool.scheduleAtFixedRate(
      () -> MinecraftForge.EVENT_BUS.post(new SecondEvent()),
      initialDelaySeconds,
      1,
      TimeUnit.SECONDS
    );
    threadPool.scheduleAtFixedRate(
      () -> MinecraftForge.EVENT_BUS.post(new MillisecondEvent()),
      initialDelaySeconds,
      1,
      TimeUnit.MILLISECONDS
    );

    eventProducer.onPostInit();
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    eventProducer.onPreInit();
    modFile = event.getSourceFile();
  }

  private void registerKeybinds(ArrayList<KeyBinding> keybinds) {
    for (KeyBinding keybind : keybinds) {
      ClientRegistry.registerKeyBinding(keybind);
    }
  }

  private void registerEvents(Object... events) {
    for (Object event : events) {
      System.out.println(event.toString() + "!!!");
      MinecraftForge.EVENT_BUS.register(event);
    }

    MinecraftForge.EVENT_BUS.register(new DoneNukerBlocks());
  }

  private void registerCommands(Command... commands) {
    for (Command command : commands) {
      EssentialAPI.getCommandRegistry().registerCommand(command);
    }
  }
}
