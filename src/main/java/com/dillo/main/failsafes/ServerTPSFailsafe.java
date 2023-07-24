package com.dillo.main.failsafes;

import static com.dillo.calls.KillSwitch.ONLINE;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.config.config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerTPSFailsafe {

  private long lastPacketTime = 0;
  private double previousTPS = 0;
  private static double totalTPSCurrent = 0;
  public static CurrentState previousState = null;

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

        if (config.tickFailsafe && ArmadilloStates.offlineState == ONLINE) {
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
