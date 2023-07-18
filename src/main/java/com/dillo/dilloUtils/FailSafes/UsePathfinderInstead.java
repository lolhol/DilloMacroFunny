package com.dillo.dilloUtils.FailSafes;

import static com.dillo.utils.RayTracingUtils.getCollisionVecs;
import static com.dillo.utils.RayTracingUtils.getDistance;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.Events.DonePathEvent;
import com.dillo.Pathfinding.BlockNode;
import com.dillo.Pathfinding.PathFinderV2;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.RayTracingUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.packets.getBlockEnum;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleLines;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class UsePathfinderInstead {

  private static boolean finderInitiated = false;
  private static HashSet<BlockPos> alrCheckedWalk = new HashSet<BlockPos>();
  private static long time = -1;
  private static long lookTim = -1;
  private static boolean startPlacing = false;
  private static boolean startCheckIfFailPlace = false;
  private static int cobbleSlot = -1;

  public static void usePathfinder(BlockPos pos) {
    TeleportToNextBlock.nextBlockInList = pos;
    BlockPos walkBlock = null;

    if (ids.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
      walkBlock = getBlockToWalkTo(pos);
    } else {
      walkBlock = pos;
    }

    if (walkBlock != null) {
      BlockPos playerPos = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
      List<BlockPos> foundRoute = PathFinderV2.pathFinder(
        new BlockNode(walkBlock, 0.0, DistanceFromTo.distanceFromTo(walkBlock, playerPos), null),
        new BlockNode(playerPos, DistanceFromTo.distanceFromTo(walkBlock, playerPos), 0.0, null)
      );
      if (foundRoute != null) {
        RenderMultipleLines.renderMultipleLines(null, null, false);
        for (int i = 0; i < foundRoute.size(); i++) {
          if (i != foundRoute.size() - 1) {
            RenderMultipleLines.renderMultipleLines(foundRoute.get(i), foundRoute.get(i + 1), true);
          }
        }

        foundRoute.remove(0);
        ArmadilloStates.offlineState = KillSwitch.ONLINE;
        finderInitiated = true;
        WalkOnPath.pathEnd = false;
        alrCheckedWalk.add(walkBlock);
        WalkOnPath.walkOnPath(foundRoute);
      }
    } else {
      SendChat.chat(prefix.prefix + "Could not find a viable block to walk to!");
    }
  }

  public static void tpToBlock() {
    //TeleportToNextBlock.teleportToNextBlock();

    Vec3 block = getAdj(TeleportToNextBlock.nextBlockInList);

    if (block != null) {
      placeBlockFromVec3(block, 200, ">??", "!!!", true);
    } else {
      SendChat.chat("NO ADJ!");
    }
  }

  public static Vec3 getAdj(BlockPos pos) {
    Block blockType = ids.mc.theWorld.getBlockState(pos).getBlock();

    if (blockType == Blocks.air) {
      BlockPos nextBlock = pos;
      Vec3 blockCent = new Vec3(nextBlock.getX() + 0.5, nextBlock.getY(), nextBlock.getZ() + 0.5);
      Vec3[] blockVecs = new Vec3[] {
        new Vec3(-1, 0, 0),
        new Vec3(1, 0, 0),
        new Vec3(0, -1, 0),
        new Vec3(0, 1, 0),
        new Vec3(0, 0, -1),
        new Vec3(0, 0, 1),
      };

      for (Vec3 vec : blockVecs) {
        Vec3 newVec = blockCent.add(vec);
        BlockPos newBlockPos = new BlockPos(newVec.xCoord, newVec.yCoord, newVec.zCoord);
        Block newBlockType = ids.mc.theWorld.getBlockState(newBlockPos).getBlock();

        if (newBlockType != Blocks.air) {
          Vec3 addedVec = newVec.add(blockCent);
          Vec3 grain = new Vec3(addedVec.xCoord / 2, addedVec.yCoord / 2 + 0.5, addedVec.zCoord / 2);

          Vec3 player = ids.mc.thePlayer.getPositionVector();

          double distToBlockCenter = getDistance(new Vec3(player.xCoord, player.yCoord + 1.54, player.zCoord), grain);

          RayTracingUtils.CollisionResult result = getCollisionVecs(
            player.xCoord,
            player.yCoord + 1.54,
            player.zCoord,
            grain.xCoord,
            grain.yCoord,
            grain.zCoord,
            distToBlockCenter,
            new Block[] { Blocks.air }
          );

          if (result != null) {
            return grain;
          }
        }
      }
    }

    return null;
  }

  public static void placeBlockFromVec3(
    Vec3 center,
    long looktime,
    String failState,
    String successState,
    boolean start
  ) {
    LookAt.smoothLook(LookAt.getRotation(center), looktime);

    startPlacing = start;
    time = System.currentTimeMillis();
    lookTim = looktime;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startPlacing && time > 0) {
        if (System.currentTimeMillis() >= time + lookTim + 5) {
          for (int i = 0; i < 8; i++) {
            ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.getDisplayName().toLowerCase().contains("cobblestone")) {
              cobbleSlot = i;
              ids.mc.thePlayer.inventory.currentItem = i;
              break;
            }
          }

          if (cobbleSlot != -1) {
            rightClick();
            startPlacing = false;
            startCheckIfFailPlace = true;
          } else {
            startPlacing = false;
            startCheckIfFailPlace = false;
            SendChat.chat(prefix.prefix + "Could not find cobble slot!");
          }
        }
      }

      if (startCheckIfFailPlace) {
        if (System.currentTimeMillis() >= time + lookTim + 1000) {
          if (ids.mc.theWorld.getBlockState(TeleportToNextBlock.nextBlockInList).getBlock() == Blocks.air) {
            rightClick();
            placeBlockWithPacket(
              TeleportToNextBlock.nextBlockInList,
              getBlockEnum.getEnum(TeleportToNextBlock.nextBlockInList)
            );
          }

          startCheckIfFailPlace = false;
        }
      }

      if (finderInitiated && ArmadilloStates.isOnline()) {
        if (WalkOnPath.pathEnd) {
          tpToBlock();
          finderInitiated = false;
          SendChat.chat(prefix.prefix + "In Range!");
        }
      }
    }
  }

  private static BlockPos getBlockToWalkTo(BlockPos original) {
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 0; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos newBlock = new BlockPos(original.getX() + x, original.getY() + y, original.getZ() + z);
          if (ids.mc.theWorld.getBlockState(newBlock).getBlock() != Blocks.air) {
            BlockPos blockAbove1 = new BlockPos(newBlock.getX(), newBlock.getY() + 1, newBlock.getZ());
            BlockPos blockAbove2 = new BlockPos(newBlock.getX(), newBlock.getY() + 2, newBlock.getZ());

            if (ids.mc.theWorld.getBlockState(blockAbove1).getBlock() == Blocks.air) {
              if (ids.mc.theWorld.getBlockState(blockAbove2).getBlock() == Blocks.air) {
                if (!alrCheckedWalk.contains(newBlock)) {
                  BlockPos playerPos = new BlockPos(
                    ids.mc.thePlayer.posX,
                    ids.mc.thePlayer.posY,
                    ids.mc.thePlayer.posZ
                  );
                  List<BlockPos> foundRoute = PathFinderV2.pathFinder(
                    new BlockNode(newBlock, 0.0, DistanceFromTo.distanceFromTo(newBlock, playerPos), null),
                    new BlockNode(playerPos, DistanceFromTo.distanceFromTo(newBlock, playerPos), 0.0, null)
                  );
                  RenderOneBlockMod.renderOneBlock(new Vec3(newBlock.getX(), newBlock.getY(), newBlock.getZ()), true);

                  if (foundRoute != null) {
                    return newBlock;
                  } else {
                    alrCheckedWalk.add(newBlock);
                    continue;
                  }
                }
              }
            }
          }
        }
      }
    }

    return null;
  }

  public static void placeBlockWithPacket(BlockPos pos, EnumFacing facing) {
    // Get the block state of the block to be placed
    IBlockState blockState = Blocks.cobblestone.getDefaultState();

    // Calculate the position offset based on the facing direction
    BlockPos offsetPos = pos.offset(facing);

    // Create a new packet with the necessary data
    ItemStack itemStack = ids.mc.thePlayer.inventory.getStackInSlot(cobbleSlot);
    C08PacketPlayerBlockPlacement placementPacket = new C08PacketPlayerBlockPlacement(
      offsetPos,
      facing.getIndex(),
      itemStack,
      0,
      0,
      0
    );
    ids.mc.thePlayer.sendQueue.addToSendQueue(placementPacket);
    // Send the packet to the server
    /* Get the player's NetHandlerPlayClient object and call sendPacket() method to send the packet */
  }

  @SubscribeEvent
  public void onDone(DonePathEvent event) {}
}
