package com.dillo.main.route.LegitRouteClear;

import com.dillo.events.DoneNukerBlocks;
import com.dillo.keybinds.Keybinds;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.route.Utils.GetBlocksForNuker;
import com.dillo.main.route.Utils.IsAbleToMine;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.RenderBox;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.dillo.main.route.Utils.GetBlocksForNuker.Blockss;
import static com.dillo.main.route.Utils.IsAbleToMine.isBlockInRoute;
import static com.dillo.utils.renderUtils.RenderBox.drawFilledInBlock;

public class LegitRouteClear {

    public static boolean isClearing = false;
    private static List<BlockPos> neededClear = new ArrayList<>();

    public static void clearRouteLegit() {
        if (currentRoute.currentRoute.size() > 1) {
            SendChat.chat(prefix.prefix + "Displaying needed to break blocks!");
            GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "legit");
        } else {
            Keybinds.isNuking = false;
            isClearing = false;
            SendChat.chat(prefix.prefix + "No route selected! :L ");
        }
    }

    public static void startLegit() {
        neededClear = Blockss;
        isClearing = true;
    }

    public static void stopClearLegit() {
        isClearing = false;
        neededClear.clear();
        isClearing = false;
        SendChat.chat(prefix.prefix + "Stopped Clearing.");
    }

    @SubscribeEvent
    public void onDoneGetting(DoneNukerBlocks event) {
        neededClear = Blockss;
        isClearing = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (isClearing) {
            if (neededClear.size() > 0) {
                BlockPos firstBlock = null;
                BlockPos secBlock = null;
                BlockPos thirdBlock = null;

                firstBlock = neededClear.get(0);

                drawFilledInBlock(firstBlock, Color.red, 0.5F, event.partialTicks);

                RenderBox.drawBox(
                        firstBlock.getX(),
                        firstBlock.getY(),
                        firstBlock.getZ(),
                        Color.red,
                        0.5F,
                        event.partialTicks,
                        false
                );

                if (neededClear.size() > 1) {
                    secBlock = neededClear.get(1);
                    if (!secBlock.equals(firstBlock)) {
                        RenderBox.drawBox(
                                secBlock.getX(),
                                secBlock.getY(),
                                secBlock.getZ(),
                                Color.white,
                                0.2F,
                                event.partialTicks,
                                false
                        );
                    } else {
                        neededClear.remove(1);
                    }
                }

                if (neededClear.size() > 2) {
                    thirdBlock = neededClear.get(2);
                    if (!thirdBlock.equals(firstBlock) && !thirdBlock.equals(secBlock)) {
                        RenderBox.drawBox(
                                thirdBlock.getX(),
                                thirdBlock.getY(),
                                thirdBlock.getZ(),
                                Color.white,
                                0.2F,
                                event.partialTicks,
                                false
                        );
                    } else {
                        neededClear.remove(2);
                    }
                }

                if (!IsAbleToMine.isAbleToMine(firstBlock) || isBlockInRoute(firstBlock)) {
                    neededClear.remove(firstBlock);
                }

                if (secBlock != null && (!IsAbleToMine.isAbleToMine(secBlock) || isBlockInRoute(secBlock))) {
                    neededClear.remove(secBlock);
                }

                if (thirdBlock != null && (!IsAbleToMine.isAbleToMine(thirdBlock) || isBlockInRoute(thirdBlock))) {
                    neededClear.remove(thirdBlock);
                }
            } else {
                isClearing = false;
                SendChat.chat(prefix.prefix + "Route is clear!");
            }
        }
    }

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
}
