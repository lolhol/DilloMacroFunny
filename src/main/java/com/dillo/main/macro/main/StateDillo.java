package com.dillo.main.macro.main;

import static com.dillo.armadillomacro.regJump;
import static com.dillo.calls.CurrentState.ROUTEOBSTRUCTEDCLEAR;
import static com.dillo.calls.CurrentState.SPINDRIVE;
import static com.dillo.config.config.fasterDillo;
import static com.dillo.config.config.ping;
import static com.dillo.main.macro.main.NewSpinDrive.*;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.isThrowRod;
import static com.dillo.main.utils.keybinds.AllKeybinds.JUMP;
import static com.dillo.main.utils.looks.DriveLook.reset;
import static com.dillo.utils.BlockUtils.getBlocksLayer;
import static com.dillo.utils.BlockUtils.getNextBlock;
import static com.dillo.utils.RayTracingUtils.adjustLook;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class StateDillo {

  public static float playerYBe4 = 0;
  public static boolean canCheckIfOnDillo = false;
  public static int tickDilloCheckCount = 0;
  public static boolean isNoTp = false;
  public static int checkedNumber = 0;
  private static boolean look = true;
  public static boolean isSmartTP = false;
  private static float isDilloSummonedTickCount = 0;
  boolean looking = false;

  public static void stateDilloNoGettingOn() {
    if (canDillo() && ArmadilloStates.isOnline()) {
      ArmadilloStates.currentState = null;
      NewSpinDrive.putAllTogether();
      swapToSlot.swapToSlot(GetSBItems.getDrillSlot());
      ArmadilloStates.currentState = SPINDRIVE;
    } else {
      if (ArmadilloStates.isOnline()) {
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public static void stateDillo() {
    //KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
    if (canDillo() && ArmadilloStates.isOnline() && !isSmartTP) {
      ArmadilloStates.currentState = null;

      new Thread(() -> {
        int slot = getItemInSlot.getItemSlot(Items.fishing_rod);
        if (slot != -1) {
          ids.mc.thePlayer.inventory.currentItem = slot;
        } else {
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        ids.mc.thePlayer.sendQueue.addToSendQueue(
          new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1),
            255,
            ids.mc.thePlayer.inventory.getStackInSlot(slot),
            0,
            0,
            0
          )
        );

        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        swapToSlot.swapToSlot(GetSBItems.getDrillSlot());

        originalBlocks = getBlocks();

        NewSpinDrive.putAllTogether();

        try {
          Thread.sleep(config.rod_drill_switch_time);

          playerYBe4 = (float) ids.mc.thePlayer.posY;

          if (ArmadilloStates.isOnline()) {
            //KeyBinding.setKeyBindState(jump.getKeyCode(), true);
            canCheckIfOnDillo = true;
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      })
        .start();
    } else {
      if (ArmadilloStates.isOnline()) {
        isThrowRod = false;
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public static boolean isDilloSummoned() {
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayer player = mc.thePlayer;

    AxisAlignedBB boundingBox = new AxisAlignedBB(
      player.posX - 3,
      player.posY - 3,
      player.posZ - 3,
      player.posX + 3,
      player.posY + 3,
      player.posZ + 3
    );

    List<Entity> entityList = mc.theWorld.getEntitiesWithinAABB(Entity.class, boundingBox);
    for (Entity entity : entityList) {
      if (!(entity instanceof EntityPlayer)) {
        if (entity.getName().contains(player.getName())) {
          isDilloSummonedTickCount++;
          return true;
        }
      }
    }

    return false;
  }

  public static boolean canDillo() {
    return (
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ))
        .isEmpty() ||
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ))
        .isEmpty() ||
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ)).isEmpty()
    );
  }

  public static boolean canDilloOn() {
    return (
      (
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 4,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty() ||
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 3,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty() ||
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 2,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty()
      ) &&
      adjustLook(
        new Vec3(
          currentRoute.currentBlock.getX() + 0.5,
          currentRoute.currentBlock.getY() + 2,
          currentRoute.currentBlock.getZ() + 0.5
        ),
        getNextBlock(),
        new net.minecraft.block.Block[] { Blocks.air },
        false
      ) ==
      null
    );
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (canCheckIfOnDillo && ArmadilloStates.isOnline()) {
        if (playerYBe4 - ids.mc.thePlayer.posY + 0.01 < 0) {
          reset();
          checkedNumber = 0;
          tickDilloCheckCount = 0;
          canCheckIfOnDillo = false;
          look = true;

          regJump.reset();

          if (ArmadilloStates.isOnline()) {
            if (isNoTp) {
              ArmadilloStates.currentState = ROUTEOBSTRUCTEDCLEAR;
              isNoTp = false;
            } else {
              new Thread(() -> {
                if (ArmadilloStates.isOnline()) {
                  try {
                    Thread.sleep(ping * 2L);
                  } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                  }

                  newSpinDrive();
                } else {
                  KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
                }
              })
                .start();
            }
          }
        } else {
          /*if (!looking && ids.mc.thePlayer.posY - playerYBe4 > 0.2) {
            looking = true;

            if (isLeft) {
              addYaw(200, -20);
            } else {
              addYaw(200, 20);
            }
          }*/

          if (tickDilloCheckCount >= 4) {
            if (!fasterDillo) {
              if (isDilloSummoned() && isDilloSummonedTickCount > 2) {
                rightClick();
              }
            } else {
              rightClick();
            }

            looking = false;
            tickDilloCheckCount = 0;
          } else {
            tickDilloCheckCount++;
          }
        }

        if (checkedNumber > 300) {
          checkedNumber = 0;
          tickDilloCheckCount = 0;
          canCheckIfOnDillo = false;
          look = true;
        } else {
          tickDilloCheckCount++;
          checkedNumber++;
        }

        look = false;
      } else {
        checkedNumber = 0;
        tickDilloCheckCount = 0;
        canCheckIfOnDillo = false;
      }
    }
  }
}
