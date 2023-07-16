package com.dillo;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.Events.DoneNukerBlocks;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.GetCurGemPrice;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.MITGUI.GUIUtils.CurTimeVein.CurTime;
import com.dillo.MITGUI.Overlay;
import com.dillo.Pathfinding.DestroyBlock;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.RemoteControl.*;
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
import com.dillo.commands.UtilCommands.*;
import com.dillo.dilloUtils.BlockESP.BlockOnRouteESP;
import com.dillo.dilloUtils.BlockUtils.JumpLook;
import com.dillo.dilloUtils.*;
import com.dillo.dilloUtils.FailSafes.*;
import com.dillo.dilloUtils.ReFuelDrill.ReFuelDrill;
import com.dillo.dilloUtils.ReFuelDrill.ReFuelDrillTriger;
import com.dillo.dilloUtils.ReFuelDrill.ThrowAtEnd;
import com.dillo.dilloUtils.RouteUtils.LegitRouteClear.LegitRouteClear;
import com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain;
import com.dillo.dilloUtils.RouteUtils.ViewClearLines.ViewClearLines;
import com.dillo.dilloUtils.Teleport.IsOnBlock;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.dilloUtils.TpUtils.WalkForward;
import com.dillo.dilloUtils.Utils.GetOnArmadillo;
import com.dillo.keybinds.Keybinds;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.renderUtils.renderModules.*;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.ArrayList;

import static com.dillo.dilloUtils.FailSafes.AnswerPPL.makeAcusation;

@Mod(modid = "autogg", name = "autogg", version = "1.0.0", clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class armadillomacro {

  public static File modFile = null;

  public static ArrayList<KeyBinding> keybinds = new ArrayList<>();

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
      new WalkToCustom(),
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
      new CheckIfCanTpToEvery()
    );

    registerEvents(
      new LookAt(),
      new ArmadilloMain(),
      new StateDillo(),
      new JumpLook(),
      new RenderSingleLineTwoPoints(),
      new RenderOneBlockMod(),
      new RenderMultipleBlocksMod(),
      new RenderPoints(),
      new BlockOnRouteESP(),
      new WaitThenCall(),
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
      new GetOnArmadillo(),
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
      new CheckFile(),
      new WalkToCustom(),
      new YawLook(),
      new PassReNew()
    );

    registerKeybinds(keybinds);
    makeAcusation(new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json"));

    new Thread(GetCurGemPrice::getCurrGemPrice).start();
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    modFile = event.getSourceFile();
  }

  private void registerKeybinds(ArrayList<KeyBinding> keybinds) {
    for (KeyBinding keybind : keybinds) {
      ClientRegistry.registerKeyBinding(keybind);
    }
  }

  private void registerEvents(Object... events) {
    for (Object event : events) {
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
