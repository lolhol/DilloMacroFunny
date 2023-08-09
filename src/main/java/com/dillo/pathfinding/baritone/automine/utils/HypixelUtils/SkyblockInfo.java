package com.dillo.pathfinding.baritone.automine.utils.HypixelUtils;

import com.google.gson.JsonObject;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

// Credits to skytils

public class SkyblockInfo {

    public static String map;
    private static JsonObject data;

    public static boolean onCrystalHollows() {
        return Objects.equals(SkyblockInfo.map, MAPS.CrystalHollows.map);
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        map = null;
    }

    public enum MAPS {
        PrivateIsland("dynamic"),
        SpiderDen("combat_1"),
        CrimsonIsle("crimson_isle"),
        TheEnd("combat_3"),
        GoldMine("mining_1"),
        DeepCaverns("mining_2"),
        DwarvenMines("mining_3"),
        CrystalHollows("crystal_hollows"),
        FarmingIsland("farming_1"),
        ThePark("foraging_1"),
        Dungeon("dungeon"),
        DungeonHub("dungeon_hub"),
        Hub("hub"),
        DarkAuction("dark_auction"),
        JerryWorkshop("winter");

        public final String map;

        MAPS(String map) {
            this.map = map;
        }
    }
}
