package com.dillo;

import static com.dillo.main.failsafes.AnswerPPL.makeAcusation;

import com.dillo.adapter.MinecraftAdapterImpl;
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
import com.dillo.commands.UtilCommands.*;
import com.dillo.commands.UtilCommands.Test;
import com.dillo.commands.baritone.StartAutoSetupWithBaritone;
import com.dillo.commands.baritone.WalkToBlockWithBaritone;
import com.dillo.events.*;
import com.dillo.gui.GUIUtils.CurRatesUtils.GetCurGemPrice;
import com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.gui.GUIUtils.CurTimeVein.CurTime;
import com.dillo.gui.Overlay;
import com.dillo.keybinds.Keybinds;
import com.dillo.main.esp.route.BlockOnRouteESP;
import com.dillo.main.failsafes.*;
import com.dillo.main.failsafes.AminStuff.WarpOutFail;
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
import com.dillo.main.utils.looks.DriveLook;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.main.utils.looks.YawLook;
import com.dillo.pathfinding.Brigeros.DestroyBlock;
import com.dillo.pathfinding.Brigeros.WalkOnPath;
import com.dillo.pathfinding.RegistersStevebot;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockLibrary;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockProvider;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockUtils;
import com.dillo.pathfinding.stevebot.core.data.items.ItemLibrary;
import com.dillo.pathfinding.stevebot.core.data.items.ItemUtils;
import com.dillo.pathfinding.stevebot.core.minecraft.MinecraftAdapter;
import com.dillo.pathfinding.stevebot.core.pathfinding.PathHandler;
import com.dillo.pathfinding.stevebot.core.pathfinding.actions.ActionUtils;
import com.dillo.pathfinding.stevebot.core.player.*;
import com.dillo.pathfinding.stevebot.core.rendering.Renderer;
import com.dillo.remote.*;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.renderUtils.renderModules.*;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = "autogg", name = "autogg", version = "1.0.0", clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class armadillomacro {

  private static EventManager eventManager;
  private static ModEventProducer eventProducer;
  private static BlockLibrary blockLibrary;
  public static BlockProvider blockProvider;
  private static ItemLibrary itemLibrary;
  public static PlayerCamera playerCamera;
  private static PlayerMovement playerMovement;
  public static PlayerInput playerInput;
  private static PlayerInventory playerInventory;
  public static MobKillerMain mobKiller = new MobKillerMain();
  public static Renderer renderer;
  private static PathHandler pathHandler;

  public static MoveToVertex vertexMover = new MoveToVertex();

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

    RouteDeletrMain destroyer = new RouteDeletrMain();

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
      new RouteDestroyr(destroyer)
    );

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
      new CheckFile(),
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
      new RegistersStevebot(),
      new PlayerLocChangeTrigger(),
      new com.dillo.main.teleport.utils.Test()
    );

    registerKeybinds(keybinds);
    makeAcusation(new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json"));

    new Thread(GetCurGemPrice::getCurrGemPrice).start();
    //////////////////////////////////
    // STEVEBOT PATHFINDING MODULE  //
    /////////////////////////////////

    eventProducer.onInit();
  }

  private void setup() {
    // minecraft
    MinecraftAdapter minecraftAdapter = new MinecraftAdapterImpl();
    //OpenGLAdapter openGLAdapter = new OpenGLAdapterImpl();

    ActionUtils.initMinecraftAdapter(minecraftAdapter);

    // events
    eventManager = new EventManagerImpl();
    eventProducer = new ModEventProducer(eventManager);

    eventManager.addListener(listenerPostInit);
    eventManager.addListener(listenerBreakBlock);
    eventManager.addListener(listenerPlaceBlock);
    eventManager.addListener(listenerRenderTick);
    eventManager.addListener(listenerRenderWorld);
    eventManager.addListener(listenerPlayerTick);
    eventManager.addListener(listenerClientTick);
    eventManager.addListener(listenerConfigChanged);

    // block library
    blockLibrary = new BlockLibrary(minecraftAdapter);

    // block provider
    blockProvider = new BlockProvider(minecraftAdapter, blockLibrary);

    // block utils
    BlockUtils.initialize(minecraftAdapter, blockProvider, blockLibrary);

    // item library
    itemLibrary = new ItemLibrary(minecraftAdapter);

    // item utils
    ItemUtils.initialize(minecraftAdapter, itemLibrary);

    // renderer
    //renderer = new Renderer(openGLAdapter, blockProvider);

    // player camera
    //playerCamera = new PlayerCamera(minecraftAdapter);

    // player input
    playerInput = new PlayerInput(minecraftAdapter);

    // player movement
    playerMovement = new PlayerMovement(playerInput, playerCamera);

    // player inventory
    playerInventory = new PlayerInventory(minecraftAdapter);

    // player utils
    PlayerUtils.initialize(minecraftAdapter, playerInput, playerCamera, playerMovement, playerInventory);

    // path handler
    pathHandler = new PathHandler(minecraftAdapter, renderer);
    EssentialAPI.getCommandRegistry().registerCommand(new WalkToCustom(new StevebotApi(pathHandler)));
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
    itemLibrary.insertBlocks(blockLibrary.getAllBlocks());
    blockLibrary.insertItems(itemLibrary.getAllItems());
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    setup();
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
      MinecraftForge.EVENT_BUS.register(event);
    }

    MinecraftForge.EVENT_BUS.register(new DoneNukerBlocks());
  }

  private void registerCommands(Command... commands) {
    for (Command command : commands) {
      EssentialAPI.getCommandRegistry().registerCommand(command);
    }
  }

  private final EventListener<PostInitEvent> listenerPostInit = new EventListener<PostInitEvent>() {
    @Override
    public Class<PostInitEvent> getEventClass() {
      return PostInitEvent.class;
    }

    @Override
    public void onEvent(PostInitEvent event) {
      blockLibrary.onEventInitialize();
      itemLibrary.onEventInitialize();
    }
  };

  private final EventListener<BlockEvent.BreakEvent> listenerBreakBlock = new EventListener<BlockEvent.BreakEvent>() {
    @Override
    public Class<BlockEvent.BreakEvent> getEventClass() {
      return BlockEvent.BreakEvent.class;
    }

    @Override
    public void onEvent(BlockEvent.BreakEvent event) {
      blockProvider.getBlockCache().onEventBlockBreak(event.pos.getX(), event.pos.getY(), event.pos.getZ());
    }
  };

  private final EventListener<BlockEvent.PlaceEvent> listenerPlaceBlock = new EventListener<BlockEvent.PlaceEvent>() {
    @Override
    public Class<BlockEvent.PlaceEvent> getEventClass() {
      return BlockEvent.PlaceEvent.class;
    }

    @Override
    public void onEvent(BlockEvent.PlaceEvent event) {
      blockProvider.getBlockCache().onEventBlockPlace(event.pos.getX(), event.pos.getY(), event.pos.getZ());
    }
  };

  private final EventListener<TickEvent.RenderTickEvent> listenerRenderTick =
    new EventListener<TickEvent.RenderTickEvent>() {
      @Override
      public Class<TickEvent.RenderTickEvent> getEventClass() {
        return TickEvent.RenderTickEvent.class;
      }

      @Override
      public void onEvent(TickEvent.RenderTickEvent event) {
        try {
          playerCamera.onRenderTickEvent(event.phase == TickEvent.Phase.START);
        } catch (NullPointerException e) {}
      }
    };

  private final EventListener<TickEvent.ClientTickEvent> listenerClientTick =
    new EventListener<TickEvent.ClientTickEvent>() {
      @Override
      public Class<TickEvent.ClientTickEvent> getEventClass() {
        return TickEvent.ClientTickEvent.class;
      }

      @Override
      public void onEvent(TickEvent.ClientTickEvent event) {
        pathHandler.onEventClientTick();
      }
    };

  private final EventListener<RenderWorldLastEvent> listenerRenderWorld = new EventListener<RenderWorldLastEvent>() {
    @Override
    public Class<RenderWorldLastEvent> getEventClass() {
      return RenderWorldLastEvent.class;
    }

    @Override
    public void onEvent(RenderWorldLastEvent event) {
      try {
        renderer.onEventRender(PlayerUtils.getPlayerPosition());
      } catch (NullPointerException e) {}
    }
  };

  private final EventListener<TickEvent.PlayerTickEvent> listenerPlayerTick =
    new EventListener<TickEvent.PlayerTickEvent>() {
      @Override
      public Class<TickEvent.PlayerTickEvent> getEventClass() {
        return TickEvent.PlayerTickEvent.class;
      }

      @Override
      public void onEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
          playerInput.onEventPlayerTick();
        }
      }
    };

  private final EventListener<ConfigChangedEvent.PostConfigChangedEvent> listenerConfigChanged =
    new EventListener<ConfigChangedEvent.PostConfigChangedEvent>() {
      @Override
      public Class<ConfigChangedEvent.PostConfigChangedEvent> getEventClass() {
        return ConfigChangedEvent.PostConfigChangedEvent.class;
      }

      @Override
      public void onEvent(ConfigChangedEvent.PostConfigChangedEvent event) {
        playerInput.onEventConfigChanged();
      }
    };
}
