package com.dillo.main.sacks.sackclicker;

public enum GemType {
    JADE("jade"),
    AMBER("amber"),
    TOPAZ("topaz"),
    SAPPHIRE("sapphire"),
    AMETHYST("amethyst"),
    JASPER("jasper"),
    RUBY("ruby"),
    ALL("all");

    public final String name;

    GemType(String str) {
        this.name = str;
    }
}
