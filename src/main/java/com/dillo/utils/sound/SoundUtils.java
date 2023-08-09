package com.dillo.utils.sound;

import com.dillo.utils.previous.random.ids;

import java.util.function.Predicate;

public class SoundUtils {

    public static void playSound(float volume, float pitch, String sound) {
        ids.mc.theWorld.playSound(
                ids.mc.thePlayer.posX,
                ids.mc.thePlayer.posY,
                ids.mc.thePlayer.posZ,
                sound,
                volume,
                pitch,
                false
        );
    }

    public static void playSoundForWithIntervalsOf(
            int time,
            int intervals,
            String sound,
            float volume,
            float pitch,
            Predicate<Integer> stopOn
    ) {
        new Thread(() -> {
            long next = intervals;

            for (int i = 0; i <= time; i++) {
                if (i >= next) {
                    next = i + intervals;
                    playSound(volume, pitch, sound);
                }

                if (!stopOn.test(i)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return;
                }
            }
        })
                .start();
    }
}
