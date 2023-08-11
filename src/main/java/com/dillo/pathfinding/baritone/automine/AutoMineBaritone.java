package com.dillo.pathfinding.baritone.automine;

import static com.dillo.commands.baritone.StartAutoSetupWithBaritone.main;
import static com.dillo.main.route.AutoSetup.SetupMain.baritoneFailed;

import com.dillo.pathfinding.baritone.automine.calculations.AStarPathFinder;
import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathFinderBehaviour;
import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathMode;
import com.dillo.pathfinding.baritone.automine.calculations.exceptions.NoPathException;
import com.dillo.pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.pathfinding.baritone.automine.config.MiningType;
import com.dillo.pathfinding.baritone.automine.config.PathFindSetting;
import com.dillo.pathfinding.baritone.automine.events.ChunkLoadEvent;
import com.dillo.pathfinding.baritone.automine.handlers.KeybindHandler;
import com.dillo.pathfinding.baritone.automine.logging.Logger;
import com.dillo.pathfinding.baritone.automine.movement.PathExecutor;
import com.dillo.pathfinding.baritone.automine.structures.Path;
import com.dillo.pathfinding.baritone.automine.structures.SemiPath;
import com.dillo.pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoMineBaritone {

  Minecraft mc = Minecraft.getMinecraft();

  public enum BaritoneState {
    PATH_FINDING,
    EXECUTING,
    IDLE,
    FAILED,
  }

  volatile BaritoneState state = BaritoneState.IDLE;

  PathFindSetting pathSetting;
  BaritoneConfig config;

  AStarPathFinder pathFinder;
  PathExecutor executor;
  BlockPos playerFloorPos;

  ArrayList<ArrayList<BlockData<?>>> targetBlockType;

  BlockPos targetBlockPos;

  volatile Path path;
  //int chunkLoadCount;

  ExecutorService exec = Executors.newCachedThreadPool();

  public AutoMineBaritone(BaritoneConfig config) {
    this.config = config;
    executor = new PathExecutor();
    pathFinder = new AStarPathFinder(getPathBehaviour());
  }

  public void mineFor(BlockPos block) {
    Logger.playerLog("Starting to mine");
    registerEventListener();
    targetBlockPos = block;
    pathSetting = new PathFindSetting(config.isMineWithPreference(), PathMode.MINE, true);
    path = null;

    //targetBlockType = blockTypes;
    startPathFinding();
  }

  public void goTo(BlockPos blockPos) {
    Logger.playerLog("Going to: " + blockPos.toString());
    registerEventListener();
    pathSetting = new PathFindSetting(config.isMineWithPreference(), PathMode.GOTO, true);
    path = null;
    targetBlockPos = blockPos;
    startPathFinding();
  }

  public void clearBlacklist() {
    pathFinder.clearBlackList();
  }

  public BaritoneState getState() {
    return this.state;
  }

  private void registerEventListener() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void unregisterEventListeners() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  public void disableBaritone() {
    main.reEnable();
    Logger.playerLog("Disabled baritone");
    state = BaritoneState.IDLE;
    executor.reset();
    terminate();
  }

  // failed = true -> Will actually terminate whole thing, otherwise just restart and pretend nothing has happened...
  private void failBaritone(boolean failed) {
    executor.reset();
    if (path != null && path.getBlocksInPath() != null && !path.getBlocksInPath().isEmpty()) {
      pathFinder.addToBlackList(path.getBlocksInPath().getFirst().getPos());
    }

    if (failed) {
      state = BaritoneState.FAILED;
      terminate();
    } else {
      Logger.log("Restarting pathfind");
      startPathFinding();
    }
  }

  private void terminate() {
    unregisterEventListeners();
    KeybindHandler.resetKeybindState();
  }

  // logic is bit intricate here, sorry
  @SubscribeEvent
  public void TickEvent(TickEvent.ClientTickEvent event) {
    if (mc.thePlayer == null || state == BaritoneState.IDLE || state == BaritoneState.FAILED) {
      return;
    }

    switch (state) {
      case PATH_FINDING:
        KeybindHandler.setKeyBindState(KeybindHandler.keyBindShift, config.isShiftWhenMine());
        break;
      case EXECUTING:
        if (executor.hasSuccessfullyFinished()) {
          Logger.log("Executor has finished");
          // TODO: Fix this
          main.reStart();
          if (path instanceof SemiPath) {
            startSemiPathFinding();
          } else {
            disableBaritone();
          }
        } else if (executor.hasFailed()) {
          Logger.log("Executor has failed");

          if (main.isAutoSetupOnline()) {
            main.addBlockToBaritoneFailList(targetBlockPos);
            main.reEnable();
          }
          // add a function here to handle fail.

          failBaritone(true);
        } else if (!executor.isExecuting()) {
          Logger.log("Executor is starting to execute a path");
          executor.executePath(path, config);
        }
    }
  }

  public BlockPos getCurrentBlockPos() {
    return path != null && path.getBlocksInPath() != null && path.getBlocksInPath().getFirst() != null
      ? path.getBlocksInPath().getFirst().getPos()
      : null;
  }

  @SubscribeEvent
  public void ChunkLoadEvent(ChunkLoadEvent event) {}

  private void startSemiPathFinding() {
    //chunkLoadCount = 0;
    startPathFinding();
    executor.reset();
  }

  private void startPathFinding() {
    state = BaritoneState.PATH_FINDING;
    Logger.playerLog("Started pathfinding");

    KeybindHandler.resetKeybindState();
    //KeybindHandler.setKeyBindState(KeybindHandler.keyBindShift, config.isShiftWhenMine());

    if (getPathBehaviour().isStaticMode()) pathFind(); else exec.submit(this::pathFind);
  }

  private void pathFind() {
    /*if (!config.isMineFloor()) {
      if (playerFloorPos != null) {
        pathFinder.removeFromBlackList(playerFloorPos);
      }

      playerFloorPos = BlockUtils.getPlayerLoc().down();
      pathFinder.addToBlackList(playerFloorPos);
    }*/

    try {
      switch (pathSetting.getPathMode()) {
        case MINE:
          //if (pathSetting.isFindWithBlockPos()) {
          path = pathFinder.getPath(PathMode.MINE, targetBlockPos);
          //} else {
          //path = pathFinder.getPath(PathMode.MINE, true, nuking);
          //}
          break;
        case GOTO: // can add more options later
          path = pathFinder.getPath(PathMode.GOTO, targetBlockPos);
          break;
      }
      state = BaritoneState.EXECUTING;
    } catch (NoPathException e) {
      Logger.playerLog("Pathfind failed: " + e);
      baritoneFailed();
      failBaritone(true);
    }
  }

  private PathFinderBehaviour getPathBehaviour() {
    return new PathFinderBehaviour(
      config.getForbiddenPathfindingBlocks() == null ? null : config.getForbiddenPathfindingBlocks(),
      config.getAllowedPathfindingBlocks() == null ? null : config.getAllowedPathfindingBlocks(),
      config.getMaxY(),
      config.getMinY(),
      config.getMineType() == MiningType.DYNAMIC ? 30 : 5,
      config.getMineType() == MiningType.STATIC
    );
  }
}
