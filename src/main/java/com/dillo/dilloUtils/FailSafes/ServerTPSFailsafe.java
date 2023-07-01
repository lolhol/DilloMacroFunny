package com.dillo.dilloUtils.FailSafes;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.utils.previous.SendChat;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerTPSFailsafe {

  private long lastPacketTime = 0;
  private double previousTPS = 0;
  private static double totalTPSCurrent = 0;
  public static String previousState = null;

  @SubscribeEvent
  public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    lastPacketTime = System.currentTimeMillis();
  }

  @SubscribeEvent
  public void onClientDisconnection(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
    lastPacketTime = 0;
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END && lastPacketTime != 0) {
      long currentTime = System.currentTimeMillis();
      long elapsed = currentTime - lastPacketTime;
      double tps = 1000.0 / elapsed;
      lastPacketTime = currentTime;

      if (previousTPS != 0 && tps != 0) {
        totalTPSCurrent = (tps + previousTPS) / 2;

        if (config.tickFailsafe && Objects.equals(ArmadilloStates.offlineState, "online")) {
          if (totalTPSCurrent <= config.ticksFail) {
            previousState = ArmadilloStates.currentState;
            ArmadilloStates.currentState = null;
          }
        }
      }

      previousTPS = tps;
    }
  }

  public static double getCurrentTPS() {
    return Math.floor(totalTPSCurrent);
  }
}
