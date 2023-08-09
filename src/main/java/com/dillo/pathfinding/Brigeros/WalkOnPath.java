package com.dillo.pathfinding.Brigeros;

import com.dillo.calls.ArmadilloStates;
import com.dillo.events.DonePathEvent;
import com.dillo.main.teleport.utils.WaitThenCall;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dillo.calls.CurrentState.*;
import static com.dillo.main.utils.keybinds.AllKeybinds.FORWARD;
import static com.dillo.main.utils.keybinds.AllKeybinds.JUMP;

public class WalkOnPath {

    public static List<BlockPos> blockRoute = null;
    public static boolean pathEnd = false;
    private static BlockPos nextBlock = null;
    private static boolean startWalking = false;
    private static int failSafeCounter1 = 0;
    private static String onOffLine = "offline";
    private static int timesTriggered = 0;
    private static Vec3 currPlayerPos = null;

    public static void walkOnPath(List<BlockPos> route) {
        blockRoute = route;

        if (blockRoute.size() > 0) {
            nextBlock = blockRoute.get(0);
            blockRoute.remove(0);

            if (nextBlock.getY() > ids.mc.thePlayer.posY) {
                LookAt.smoothLook(
                        LookAt.getRotation(new Vec3(nextBlock.getX() + 0.5, nextBlock.getY() + 0.5, nextBlock.getZ() + 0.5)),
                        150
                );
            } else {
                LookAt.smoothLook(
                        LookAt.getRotation(new Vec3(nextBlock.getX() + 0.5, nextBlock.getY() + 1, nextBlock.getZ() + 0.5)),
                        150
                );
            }

            onOffLine = "online";
            WaitThenCall.waitThenCall(200, STARTWALKINGPATH);
        } else {
            onOffLine = "offline";
            SendChat.chat(prefix.prefix + "Path end!");
            KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
            KeyBinding.setKeyBindState(FORWARD.getKeyCode(), false);
            MinecraftForge.EVENT_BUS.post(new DonePathEvent());
            startWalking = false;
            pathEnd = true;
        }
    }

    public static void startWalkingPath() {
        ArmadilloStates.currentState = null;

        if (nextBlock != null) {
            startWalking = true;
        } else {
            SendChat.chat("Stopped walking on path!");
        }
    }

    public static boolean cannotJump(BlockPos originBlock) {
        if (
                DistanceFromTo.distanceFromTo(originBlock, ids.mc.thePlayer.getPosition()) -
                        DistanceFromTo.distanceFromTo(
                                new BlockPos(ids.mc.thePlayer.posX + 1, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ),
                                ids.mc.thePlayer.getPosition()
                        ) <
                        0.01
        ) {
            BlockPos blockPX = new BlockPos(originBlock.getX() + 1, originBlock.getY(), originBlock.getZ());
            BlockPos blockMX = new BlockPos(originBlock.getX() - 1, originBlock.getY(), originBlock.getZ());
            BlockPos blockPXY = new BlockPos(originBlock.getX() + 1, originBlock.getY() + 1, originBlock.getZ());
            BlockPos blockMXY = new BlockPos(originBlock.getX() - 1, originBlock.getY() + 1, originBlock.getZ());

            if (
                    ids.mc.theWorld.getBlockState(blockPX).getBlock() != Blocks.air ||
                            ids.mc.theWorld.getBlockState(blockMX).getBlock() != Blocks.air
            ) {
                return true;
            } else {
                if (
                        ids.mc.theWorld.getBlockState(blockPXY).getBlock() != Blocks.air ||
                                ids.mc.theWorld.getBlockState(blockMXY).getBlock() != Blocks.air
                ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void destroyBlock(BlockPos block) {
        onOffLine = "offline";
        startWalking = false;
        LookAt.smoothLook(LookAt.getRotation(new Vec3(block.getX() + 0.5, block.getY(), block.getZ() + 0.5)), 400);

        List<BlockPos> blocks = new ArrayList<BlockPos>();
        blocks.add(block);

        if (
                ids.mc.theWorld.getBlockState(new BlockPos(block.getX(), block.getY() + 1, block.getZ())).getBlock() != Blocks.air
        ) {
            blocks.add(new BlockPos(block.getX(), block.getY() + 1, block.getZ()));
        }

        blocks.add(new BlockPos(block.getX() + 1, block.getY(), block.getZ()));
        blocks.add(new BlockPos(block.getX() + 1, block.getY() + 1, block.getZ()));
        blocks.add(new BlockPos(block.getX(), block.getY(), block.getZ() - 1));
        blocks.add(new BlockPos(block.getX(), block.getY() + 1, block.getZ() - 1));

        DestroyBlock.destroyBlock(blocks, RESUMEWALKING, true);
    }

    public static boolean isOnBlock(BlockPos pos, double minDist) {
        return (
                DistanceFromTo.distanceFromTo(
                        new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ),
                        pos
                ) <
                        minDist
        );
    }

    public static boolean canBreakBlock(BlockPos pos) {
        if (ids.mc.theWorld.getBlockState(pos).getBlock() != Blocks.air) {
            return true;
        }

        return false;
    }

    public static void stopWalking() {
        onOffLine = "offline";
        nextBlock = null;
        startWalking = false;
        blockRoute = null;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (startWalking) {
                if (!isOnBlock(nextBlock, 1.2)) {
                    LookAt.smoothLook(
                            LookAt.getRotation(new Vec3(nextBlock.getX() + 0.5, nextBlock.getY() + 1, nextBlock.getZ() + 0.5)),
                            20
                    );
                }
            }

            if (Objects.equals(onOffLine, "online")) {
                if (failSafeCounter1 >= 20) {
                    if (currPlayerPos == null) {
                        currPlayerPos = new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
                    }

                    failSafeCounter1 = 0;

                    //SendChat.chat(Math.abs(currPlayerPos.xCoord - ids.mc.thePlayer.posX) + " " + Math.abs(currPlayerPos.zCoord - ids.mc.thePlayer.posZ));

                    if (
                            Math.abs(currPlayerPos.xCoord - ids.mc.thePlayer.posX) < 0.5 &&
                                    Math.abs(currPlayerPos.zCoord - ids.mc.thePlayer.posZ) < 0.5
                    ) {
                        if (timesTriggered >= 10) {
                            SendChat.chat(prefix.prefix + "Detected stuck! Restarting!");
                            stopWalking();
                            KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
                            KeyBinding.setKeyBindState(FORWARD.getKeyCode(), false);
                            timesTriggered = 0;

                            WaitThenCall.waitThenCall(1000, RESTARTPATHFINDER);
                        }

                        timesTriggered++;
                    } else {
                        timesTriggered = 0;
                    }

                    currPlayerPos = new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
                }

                failSafeCounter1++;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (startWalking) {
            if (!isOnBlock(nextBlock, 0.2)) {
                if (
                        !isPathObstructed(ids.mc.thePlayer, nextBlock) ||
                                DistanceFromTo.distanceFromTo(nextBlock, ids.mc.thePlayer.getPosition()) > 2
                ) {
                    if (canBreakBlock(nextBlock)) {
                        KeyBinding.setKeyBindState(FORWARD.getKeyCode(), true);

                        if (!isOnBlock(nextBlock, 2)) {
                            destroyBlock(nextBlock);
                        }
                    } else {
                        if (ids.mc.theWorld.getBlockState(nextBlock).getBlock() == Blocks.water) {
                            KeyBinding.setKeyBindState(JUMP.getKeyCode(), true);
                        } else {
                            KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
                        }

                        if (ids.mc.thePlayer.posY == nextBlock.getY()) {
                            if (!FORWARD.isKeyDown()) {
                                KeyBinding.setKeyBindState(FORWARD.getKeyCode(), true);
                            }
                        } else if (ids.mc.thePlayer.posY < nextBlock.getY()) {
                            //SendChat.chat("!!");
                            if (!cannotJump(nextBlock)) {
                                KeyBinding.setKeyBindState(JUMP.getKeyCode(), true);
                                KeyBinding.setKeyBindState(FORWARD.getKeyCode(), true);
                            } else {
                                destroyBlock(nextBlock);
                            }
                        } else if (ids.mc.thePlayer.posY > nextBlock.getY()) {
                            KeyBinding.setKeyBindState(FORWARD.getKeyCode(), true);
                        }
                    }
                } else {
                    destroyBlock(nextBlock);
                }
            } else {
                KeyBinding.setKeyBindState(FORWARD.getKeyCode(), false);
                KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
                walkOnPath(blockRoute);
            }
        }
    }

    public boolean isPathObstructed(EntityPlayer player, BlockPos destination) {
        World world = player.getEntityWorld();
        BlockPos startPos = player.getPosition(); // Get the player's current position

        // Calculate the direction from the player's position to the destination
        Vec3 startVec = new Vec3(startPos.getX() + 0.5, startPos.getY() + 0.5, startPos.getZ() + 0.5);
        Vec3 endVec = new Vec3(destination.getX() + 0.5, destination.getY() + 0.5, destination.getZ() + 0.5);

        MovingObjectPosition result = world.rayTraceBlocks(startVec, endVec, false);

        if (result == null || result.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
            // No obstruction found
            return false;
        }

        if (result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            // Obstruction found
            return true;
        }

        // Handle other types of hits (e.g., entities)
        return false;
    }
}
