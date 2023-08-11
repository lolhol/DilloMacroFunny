package com.dillo.main.sacks.sackclicker;

import com.dillo.utils.getSBAtr;
import com.dillo.utils.previous.random.ids;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.main.macro.refuel.ReFuelDrill.getInventoryName;
import static com.dillo.main.sacks.sackclicker.GemType.*;

public class SackClickerMain {

    boolean state = false;
    boolean refreshGemEachTime = false;
    GemType type;
    int ticks = 0;
    int clickSlot = -1;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (refreshGemEachTime) {
            }

            if (!state) return;

            if (getInventoryName(ids.mc.currentScreen).toLowerCase().contains("sack")) {
                if (ticks >= 2) {
                    ticks = 0;
                    if (clickSlot == -1) return;
                    clickSlot(clickSlot, 1);
                } else {
                    ticks++;
                }
            }
        }
    }

    public void sackClicker(boolean state, GemType type) {
        switch (type) {
            case JADE:
                getSlot(JADE.name);
                break;
            case AMBER:
                getSlot(AMBER.name);
                break;
            case TOPAZ:
                getSlot(TOPAZ.name);
                break;
            case SAPPHIRE:
                getSlot(SAPPHIRE.name);
                break;
            case AMETHYST:
                getSlot(AMETHYST.name);
                break;
            case JASPER:
                getSlot(JASPER.name);
                break;
            case RUBY:
                getSlot(RUBY.name);
                break;
            case ALL:
                refreshGemEachTime = true;
        }

        this.state = state;
    }

    void clickSlot(int slot, int windowAdd) {
        ids.mc.playerController.windowClick(
                ids.mc.thePlayer.openContainer.windowId + windowAdd,
                slot,
                0,
                0,
                ids.mc.thePlayer
        );
    }

    int getSlot(String name) {
        for (int i = 0; i < 27; i++) {
            ItemStack stack = ids.mc.thePlayer.inventory.getStackInSlot(i);
            String skyblockID = getSBAtr.getSkyBlockID(stack);
            if (skyblockID == null) continue;

            skyblockID = skyblockID.toLowerCase();

            if (skyblockID.contains(name.toLowerCase())) return i;
        }

        return -1;
    }
}
