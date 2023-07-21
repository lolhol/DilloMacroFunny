package com.dillo;

import static com.dillo.dilloUtils.FailSafes.AnswerPPL.makeAcusation;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.Events.*;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.GetCurGemPrice;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.MITGUI.GUIUtils.CurTimeVein.CurTime;
import com.dillo.MITGUI.Overlay;
import com.dillo.Pathfinding.Brigeros.DestroyBlock;
import com.dillo.Pathfinding.Brigeros.WalkOnPath;
import com.dillo.Pathfinding.stevebot.core.StevebotApi;
import com.dillo.Pathfinding.stevebot.core.data.blocks.BlockLibrary;
import com.dillo.Pathfinding.stevebot.core.data.blocks.BlockProvider;
import com.dillo.Pathfinding.stevebot.core.data.blocks.BlockUtils;
import com.dillo.Pathfinding.stevebot.core.data.items.ItemLibrary;
import com.dillo.Pathfinding.stevebot.core.data.items.ItemUtils;
import com.dillo.Pathfinding.stevebot.core.minecraft.MinecraftAdapter;
import com.dillo.Pathfinding.stevebot.core.minecraft.OpenGLAdapter;
import com.dillo.Pathfinding.stevebot.core.pathfinding.PathHandler;
import com.dillo.Pathfinding.stevebot.core.pathfinding.actions.ActionUtils;
import com.dillo.Pathfinding.stevebot.core.player.*;
import com.dillo.Pathfinding.stevebot.core.rendering.Renderer;
import com.dillo.RemoteControl.*;
import com.dillo.adapter.MinecraftAdapterImpl;
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
import com.dillo.commands.baritone.StartAutoSetupWithBaritone;
import com.dillo.commands.baritone.WalkToBlockWithBaritone;
import com.dillo.dilloUtils.*;
import com.dillo.dilloUtils.BlockESP.BlockOnRouteESP;
import com.dillo.dilloUtils.BlockUtils.JumpLook;
import com.dillo.dilloUtils.FailSafes.*;
import com.dillo.dilloUtils.ReFuelDrill.ReFuelDrill;
import com.dillo.dilloUtils.ReFuelDrill.ReFuelDrillTriger;
import com.dillo.dilloUtils.ReFuelDrill.ThrowAtEnd;
import com.dillo.dilloUtils.RouteUtils.AutoSetup.SetupMain;
import com.dillo.dilloUtils.RouteUtils.LegitRouteClear.LegitRouteClear;
import com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain;
import com.dillo.dilloUtils.RouteUtils.PlaceBlocks.PlaceCobbleModule;
import com.dillo.dilloUtils.RouteUtils.RouteDeletr.RouteDeletrMain;
import com.dillo.dilloUtils.RouteUtils.ViewClearLines.ViewClearLines;
import com.dillo.dilloUtils.Teleport.IsOnBlock;
import com.dillo.dilloUtils.Teleport.TeleportMovePlayer.MoveToVertex;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.dilloUtils.TpUtils.WalkForward;
import com.dillo.dilloUtils.Utils.GetOnArmadillo;
import com.dillo.keybinds.Keybinds;
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
  private static BlockProvider blockProvider;
  private static ItemLibrary itemLibrary;
  private static PlayerCamera playerCamera;
  private static PlayerMovement playerMovement;
  private static PlayerInput playerInput;
  private static PlayerInventory playerInventory;
  private static Renderer renderer;
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
      new YawLook(),
      new PassReNew(),
      new DriveLook(),
      new TeleportToBlock(),
      new PlaceCobbleModule(),
      new SetupMain(),
      destroyer,
      vertexMover
    );

    registerKeybinds(keybinds);
    makeAcusation(new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json"));

    new Thread(GetCurGemPrice::getCurrGemPrice).start();

    //////////////////////////////
    // STEVE PATHFINDING MODULE //
    //////////////////////////////

    setup();
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
        playerCamera.onRenderTickEvent(event.phase == TickEvent.Phase.START);
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
        SendChat.chat("!!!");
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
      renderer.onEventRender(PlayerUtils.getPlayerPosition());
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
