package com.dillo.pathfinding.stevebot.core.data.player;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockWrapper;
import com.dillo.pathfinding.stevebot.core.data.items.ItemLibrary;
import com.dillo.pathfinding.stevebot.core.data.items.ItemUtils;
import com.dillo.pathfinding.stevebot.core.data.items.wrapper.*;
import com.dillo.pathfinding.stevebot.core.data.modification.Modification;
import com.dillo.pathfinding.stevebot.core.minecraft.MinecraftAdapter;


public class PlayerSnapshot {

    private static final float COST_INFINITE = Float.MAX_VALUE;

    private final ItemWrapper[] hotbarItems = new ItemWrapper[9];
    private final int[] hotbarStackSizes = new int[9];
    private int health;

    public PlayerSnapshot() {
    }

    public PlayerSnapshot(PlayerSnapshot snapshot) {
        this.health = snapshot.health;
        for (int i = 0; i < 9; i++) {
            setHotbarItemStack(i, snapshot.hotbarItems[i], snapshot.hotbarStackSizes[i]);
        }
    }

    public void applyModification(MinecraftAdapter minecraftAdapter, Modification modification) {
        // ... rest of the method ...
    }

    public int getPlayerHealth() {
        return health;
    }

    public void setPlayerHealth(int health) {
        this.health = health;
    }

    public void setHotbarItemStack(ItemStackWrapper stack) {
        setHotbarItemStack(stack.getSlot(), stack.getItem(), stack.getSize());
    }

    public void setHotbarItemStack(int slot, ItemWrapper item, int stackSize) {
        hotbarItems[slot] = item;
        hotbarStackSizes[slot] = stackSize;
    }

    // ... rest of the methods ...

    public int findBestToolSlot(BlockWrapper block) {
        int slotBest = -1;
        float bestSpeed = COST_INFINITE;
        for (int i = 0; i < 9; i++) {
            final ItemToolWrapper tool = getAsTool(i);
            final float breakTime = ItemUtils.getBreakDuration(tool, block);
            if (bestSpeed > breakTime) {
                bestSpeed = breakTime;
                slotBest = i;
            }
        }
        return slotBest;
    }

    public ItemToolWrapper getAsTool(int slot) {
        final ItemWrapper stack = hotbarItems[slot];
        if (stack != null && hotbarStackSizes[slot] != 0 && stack.isTool()) {
            return (ItemToolWrapper) stack;
        } else {
            return ItemLibrary.ITEM_HAND;
        }
    }
}
