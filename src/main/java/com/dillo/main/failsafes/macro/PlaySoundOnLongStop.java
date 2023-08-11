package com.dillo.main.failsafes.macro;

import com.dillo.calls.ArmadilloStates;
import com.dillo.utils.StartMacro;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.utils.sound.SoundUtils.playSoundForWithIntervalsOf;

public class PlaySoundOnLongStop {

    public static boolean override = false;
    int curTicks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (override) return;

        if (StartMacro.isPlayerTurnedOn && !ArmadilloStates.isOnline()) {
            curTicks++;
        }

        if (curTicks >= 600) {
            override = true;
            curTicks = 0;
            StartMacro.isPlayerTurnedOn = false;
            playSoundForWithIntervalsOf(
                    2000,
                    20,
                    "random.orb",
                    0.5f,
                    0.5f,
                    (Integer i) -> {
                        return false;
                    }
            );
        }
    }
}
