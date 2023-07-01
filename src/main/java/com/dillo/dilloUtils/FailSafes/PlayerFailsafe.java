package com.dillo.dilloUtils.FailSafes;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.utils.DistFromXPlayer;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerFailsafe {

  private static String prevState = "";

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (
      event.phase == TickEvent.Phase.END &&
      config.playerFailsafe &&
      !MinecraftServer.getServer().isSinglePlayer() &&
      isPlayerOnServer() &&
      ArmadilloStates.offlineState == "online"
    ) {
      if (ids.mc.theWorld != null) {
        List<BlockPos> playersClose = new ArrayList<>();
        HashSet<String> players = getAllPlayers();

        for (Object playerObj : ids.mc.theWorld.playerEntities) {
          EntityPlayer player = (EntityPlayer) playerObj;

          if (players.contains(player.getName())) {
            if (
              DistanceFromTo.distanceFromTo(player.getPosition(), ids.mc.thePlayer.getPosition()) <
              config.playerFailsafeRange
            ) {
              playersClose.add(player.getPosition());
            }
            //RenderMultipleBlocksMod.renderMultipleBlocks(new Vec3(player.posX, player.posY, player.posZ), true);
          }
        }

        if (ArmadilloStates.currentState != "PLAYER_DETECTION" && playersClose.size() > 0) {
          prevState = ArmadilloStates.currentState;
        } else if (ArmadilloStates.currentState == "PLAYER_DETECTION" && playersClose.size() == 0) {
          ArmadilloStates.currentState = prevState;
        }
      }
    }
  }

  public static boolean isPlayerOnServer() {
    return ids.mc.getCurrentServerData() != null;
  }

  public static HashSet<String> getAllPlayers() {
    HashSet<String> players = new HashSet<>();
    Scoreboard scoreboard = ids.mc.theWorld.getScoreboard();
    ScorePlayerTeam[] teams = scoreboard.getTeams().toArray(new ScorePlayerTeam[0]);

    for (ScorePlayerTeam t : teams) {
      if (t == null || t.getMembershipCollection().isEmpty()) {
        continue;
      }

      for (String playerName : t.getMembershipCollection()) {
        Team.EnumVisible visible = t.getNameTagVisibility();
        boolean isV = visible == Team.EnumVisible.ALWAYS || visible == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS;

        if (isV) {
          players.add(EnumChatFormatting.getTextWithoutFormattingCodes(playerName));
        }
      }
    }

    return players;
  }
}
