package com.dillo.main.failsafes.RestartMacroUtils;

import com.dillo.main.blockutils.colisions.GetUnobstructedPos;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CheckIfCanSeeAny {

    public static List<BlockPos> checkIfCanSeeAny(List<BlockPos> initialBlocks) {
        List<BlockPos> returnList = new ArrayList<BlockPos>();

        for (BlockPos block : initialBlocks) {
            Vec3 questionBlock = GetUnobstructedPos.getUnobstructedPos(block);

            if (questionBlock != null) {
                returnList.add(block);
            }
        }

        return returnList;
    }
}
