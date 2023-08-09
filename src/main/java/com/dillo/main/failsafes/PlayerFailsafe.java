package com.dillo.main.failsafes;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.config.config;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.dillo.calls.CurrentState.PLAYER_DETECTION;

public class PlayerFailsafe {

    private static CurrentState prevState = null;

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

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (
                event.phase == TickEvent.Phase.END &&
                        config.playerFailsafe &&
                        !MinecraftServer.getServer().isSinglePlayer() &&
                        isPlayerOnServer() &&
                        ArmadilloStates.isOnline()
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

                if (ArmadilloStates.currentState != PLAYER_DETECTION && playersClose.size() > 0) {
                    prevState = ArmadilloStates.currentState;
                } else if (ArmadilloStates.currentState == PLAYER_DETECTION && playersClose.size() == 0) {
                    ArmadilloStates.currentState = prevState;
                }
            }
        }
    }
}
