package com.dillo.pathfinding.stevebot.core.data.items.wrapper;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;

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
     * @return the id of the item
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the item ("minecraft:item_name")
     */
    public String getName() {
        return name;
    }

    public boolean isBlock() {
        return this instanceof ItemBlock;
    }

    public boolean isTool() {
        return this instanceof ItemTool;
    }
}
