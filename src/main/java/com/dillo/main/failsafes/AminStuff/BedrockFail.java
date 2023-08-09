package com.dillo.main.failsafes.AminStuff;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static com.dillo.utils.RandomisationUtils.percentOf;

public class BedrockFail {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!ArmadilloStates.isOnline() || config.bedrockPercentAmount == 0) return;
        List<BlockPos> blocks = BlockUtils.getSpecificBlocksInRadius(
                new Block[]{Blocks.acacia_door},
                5,
                5,
                5,
                ids.mc.thePlayer.getPosition()
        );

        double percent = percentOf(blocks.size(), 125);

        if (percent >= config.bedrockPercentAmount) {
            ArmadilloStates.currentState = null;
            ArmadilloStates.offlineState = KillSwitch.OFFLINE;
            SendChat.chat(prefix.prefix + "BEDROCK THRESHOLD EXCEEDED. STOPPING!");
            // Add a ping sound
        }
    }
}
