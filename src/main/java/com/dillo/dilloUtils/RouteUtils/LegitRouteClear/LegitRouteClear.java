package com.dillo.dilloUtils.RouteUtils.LegitRouteClear;

import com.dillo.Events.DoneNukerBlocks;
import com.dillo.Events.DonePathEvent;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker;
import com.dillo.dilloUtils.RouteUtils.Utils.IsAbleToMine;
import com.dillo.keybinds.Keybinds;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.RenderBox;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker.Blockss;

public class LegitRouteClear {
    private static List<BlockPos> neededClear = new ArrayList<>();
    public static boolean isClearing = false;

    public static class ThreeBlocks {
        public BlockPos block1 = null;
        public BlockPos block2 = null;
        public BlockPos block3 = null;

        public void ThreeBlocks(List<BlockPos> fromBlocks) {
            int i = 0;

            while (fromBlocks.size() > 0 && (block1 == null || block2 == null || block3 == null)) {
                BlockPos block = fromBlocks.get(0);

                if (block1 == null) {
                    if (IsAbleToMine.isAbleToMine(block)) {
                        block1 = block;
                    } else {
                        fromBlocks.remove(0);
                    }
                }

                BlockPos secBlock = fromBlocks.get(1);
                if (block2 == null) {
                    if (IsAbleToMine.isAbleToMine(secBlock)) {
                        block2 = secBlock;
                    } else {
                        fromBlocks.remove(1);
                    }
                }

                BlockPos thirdBlock = fromBlocks.get(2);
                if (block3 == null) {
                    if (IsAbleToMine.isAbleToMine(thirdBlock)) {
                        block3 = thirdBlock;
                    } else {
                        fromBlocks.remove(2);
                    }
                }
            }
        }
    }

    public static void clearRouteLegit() {
        if (currentRoute.currentRoute.size() > 1) {
            SendChat.chat(prefix.prefix + "Displaying needed to break blocks!");
            GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "legit");
        } else {
            Keybinds.isNuking = false;
            SendChat.chat(prefix.prefix + "No route selected! :L ");
        }
    }

    public static void startLegit() {
        neededClear = Blockss;
        isClearing = true;
    }

    @SubscribeEvent
    public void onDoneGetting(DoneNukerBlocks event) {
        neededClear = Blockss;
        isClearing = true;
    }

    public static void stopClearLegit() {
        isClearing = false;
        neededClear.clear();
        SendChat.chat(prefix.prefix + "Stopped Clearing.");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (isClearing) {
            if (neededClear.size() > 0) {
                BlockPos firstBlock = null;
                BlockPos secBlock = null;
                BlockPos thirdBlock = null;

                firstBlock = neededClear.get(0);
                RenderBox.drawBox(firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), Color.red, 0.2F, event.partialTicks);

                if (neededClear.size() > 1) {
                    secBlock = neededClear.get(1);
                    RenderBox.drawBox(secBlock.getX(), secBlock.getY(), secBlock.getZ(), Color.white, 0.2F, event.partialTicks);
                }

                if (neededClear.size() > 2) {
                    thirdBlock = neededClear.get(2);

                    RenderBox.drawBox(thirdBlock.getX(), thirdBlock.getY(), thirdBlock.getZ(), Color.white, 0.2F, event.partialTicks);
                }

                if (!IsAbleToMine.isAbleToMine(firstBlock)) {
                    neededClear.remove(firstBlock);
                }

                if (secBlock != null && !IsAbleToMine.isAbleToMine(secBlock)) {
                    neededClear.remove(secBlock);
                }

                if (thirdBlock != null && !IsAbleToMine.isAbleToMine(thirdBlock)) {
                    neededClear.remove(thirdBlock);
                }
            } else {
                isClearing = false;
                SendChat.chat(prefix.prefix + "Route is clear!");
            }
        }
    }
}
