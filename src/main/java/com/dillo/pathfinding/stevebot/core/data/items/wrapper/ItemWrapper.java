package com.dillo.pathfinding.stevebot.core.data.items.wrapper;

import lombok.Getter;

@Getter
public class ItemWrapper {

    private final int id;
    private final String name;

    /**
     * @param id   the id of the item
     * @param name the name of the item
     */
    public ItemWrapper(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Check if the item is an instance of ItemBlock.
     *
     * @return true if the item is an instance of ItemBlock, false otherwise.
     */
    public boolean isBlock() {
        return isItemBlock(this);
    }

    /**
     * Check if the item is an instance of ItemTool.
     *
     * @return true if the item is an instance of ItemTool, false otherwise.
     */
    public boolean isTool() {
        return isItemTool(this);
    }

    /**
     * Check if the provided instance is an instance of ItemBlock.
     *
     * @param item the instance to check
     * @return true if the item is an instance of ItemBlock, false otherwise.
     */
    public static boolean isItemBlock(ItemWrapper item) {
        return item instanceof ItemBlockWrapper;
    }

    /**
     * Check if the provided instance is an instance of ItemTool.
     *
     * @param item the instance to check
     * @return true if the item is an instance of ItemTool, false otherwise.
     */
    public static boolean isItemTool(ItemWrapper item) {
        return item instanceof ItemToolWrapper;
    }
}
