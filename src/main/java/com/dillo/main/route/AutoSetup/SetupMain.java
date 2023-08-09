package com.dillo.main.route.AutoSetup;

import com.dillo.events.MillisecondEvent;
import com.dillo.events.utilevents.RouteClearDoneWalking;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.route.Nuker.NukerMain;
import com.dillo.main.route.Utils.GetBlocksForNuker;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.player.PlayerUtils;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.dillo.armadillomacro.pathHandler;
import static com.dillo.commands.baritone.StartAutoSetupWithBaritone.main;
import static com.dillo.main.route.Nuker.NukerMain.*;
import static com.dillo.main.route.Nuker.StartNuker.stopNuker;
import static com.dillo.utils.BlockUtils.getBlock;
import static com.dillo.utils.BlockUtils.makeNewBlock;

public class SetupMain {

    public static boolean isTurnedOn = false;
    public static List<BlockPos> neededToMineBlocks = new ArrayList<>();
    public static boolean isFirstDone = false;
    public static boolean alrNuking = false;
    public static boolean usingBaritone = false;
    public boolean isAutoSetupOn = false;
    boolean isStartCheckForDonePath;
    NukerMain nuker = new NukerMain();
    List<BlockPos> baritoneFailBlocks = new ArrayList<BlockPos>();

    StevebotApi api = new StevebotApi(pathHandler);

    public static void baritoneFailed() {
        for (int i = -1; 1 <= i; i++) {
            for (int j = -1; 0 <= j; j++) {
                for (int k = -1; 1 <= k; k++) {
                    BlockPos curBlock = makeNewBlock(i, j, k, ids.mc.thePlayer.getPosition());
                    nuking.add(0, curBlock);
                }
            }
        }

        usingBaritone = false;
        unpauseNuker();
    }

    public static void updateVariablesAutoSetup(List<BlockPos> blocksFound) {
        neededToMineBlocks = blocksFound;
    }

    public void reset() {
        neededToMineBlocks.clear();
        prev = 2;
        isTurnedOn = false;
        isFirstDone = false;
        alrNuking = false;
        usingBaritone = false;
        this.isStartCheckForDonePath = false;
        stopNuker();
    }

    @SubscribeEvent
    public void onDonePath(RouteClearDoneWalking event) {
        if (!isStartCheckForDonePath) return;
        reStart();
    }

    @SubscribeEvent
    public void onMillisecond(MillisecondEvent event) {
        if (isTurnedOn) {
            isAutoSetupOn = true;
            if (usingBaritone) return;

            if (!isFirstDone) {
                getVariables();
                isFirstDone = true;
            }

            if (neededToMineBlocks.size() > 0) {
                if (!alrNuking) {
                    alrNuking = true;
                    startAutoSetupNuker(neededToMineBlocks, true);
                }

                if (nuker.isDone()) {
                    usingBaritone = true;
                    pauseNuker();

                    BlockPos block = getBaritoneWalkBlock(nuking, ids.mc.thePlayer.getPosition());

                    if (block != null) {
                        if (!main.baritoneFailBlocks.contains(block)) {
                            isStartCheckForDonePath = true;
                            api.path(
                                    new BaseBlockPos(PlayerUtils.getPlayerBlockPos()),
                                    new BaseBlockPos(block.getX(), block.getY(), block.getZ()),
                                    true,
                                    false
                            );
                        } else {
                            SendChat.chat("RESET");
                            usingBaritone = false;
                        }
                    } else {
                        SendChat.chat("RESET!!!");
                        reset();
                    }
                }
            }
        } else {
            isAutoSetupOn = false;
        }
    }

    public boolean isAutoSetupOnline() {
        return isAutoSetupOn;
    }

    public void addBlockToBaritoneFailList(BlockPos block) {
        usingBaritone = false;
        main.baritoneFailBlocks.add(block);
    }

    public void reEnable() {
        usingBaritone = false;
        prev = 100;
        unpauseNuker();
    }

    public void reStart() {
        main.baritoneFailBlocks.clear();
        usingBaritone = false;
        prev = 100;
        isStartCheckForDonePath = false;
        unpauseNuker();
    }

    public void getVariables() {
        GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "AUTOSETUP");
    }

    void startFromBlock(BlockPos block) {
        if (neededToMineBlocks.contains(block)) {
            int position = neededToMineBlocks.indexOf(block);
            List<BlockPos> newArr = new ArrayList<>();

            for (int i = position - 15; i < neededToMineBlocks.size(); i++) {
                if (i < 0) continue;
                newArr.add(neededToMineBlocks.get(i));
            }

            neededToMineBlocks = newArr;
        }
    }

    BlockPos getBaritoneWalkBlock(List<BlockPos> curNukerBlocks, BlockPos lastKnownPos) {
        BlockPos returnBlock = curNukerBlocks.get(0);

        if (!isCanWalkTo(returnBlock)) {
            Iterable<BlockPos> blocks = BlockPos.getAllInBox(
                    returnBlock.subtract(new Vec3i(-1, -1, -1)),
                    returnBlock.add(1, 1, 1)
            );

            for (BlockPos block : blocks) {
                if (isCanWalkTo(block)) {
                    returnBlock = block;
                }
            }
        }

        assert returnBlock != null;
        RenderOneBlockMod.renderOneBlock(BlockUtils.fromBlockPosToVec3(returnBlock), true);

        return returnBlock;
    }

    boolean isCanWalkTo(BlockPos block) {
        return (
                (
                        getBlock(block) == Blocks.air &&
                                getBlock(makeNewBlock(0, 1, 0, block)) == Blocks.air &&
                                getBlock(makeNewBlock(0, -1, 0, block)) != Blocks.air
                ) ||
                        (
                                getBlock(block) != Blocks.air &&
                                        getBlock(makeNewBlock(0, -1, 0, block)) != Blocks.air &&
                                        getBlock(makeNewBlock(0, 1, 0, block)) == Blocks.air &&
                                        getBlock(makeNewBlock(0, 2, 0, block)) == Blocks.air
                        )
        );
    }
}
