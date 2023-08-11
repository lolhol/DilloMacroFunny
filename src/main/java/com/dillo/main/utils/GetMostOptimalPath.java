package com.dillo.main.utils;

import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.random.ids;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.dillo.main.macro.main.NewSpinDrive.isLeft;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.isClearing;
import static com.dillo.main.utils.looks.LookAt.getNeededChange;
import static com.dillo.main.utils.looks.LookAt.getRotation;

public class GetMostOptimalPath {

    public static boolean isClear = false;

    public static OptimalPath getBestPath(List<BlockPos> originBlocks, float currentLook) {
        OptimalPathRotation bestPath = new OptimalPathRotation(new ArrayList<>(), 0, 0);
        float bestPoints = 0;
        OptimalPath optimalPath = new OptimalPath(new ArrayList<>(), 0);

        float maxRot = 25;

        if (isClearing) {
            maxRot = 20;
        }

        for (int displacement = 0; displacement < 400; displacement += 5) {
            List<BlockPos> blocks = new ArrayList<>();
            float points = 0;

            for (BlockPos block : originBlocks) {
                Vec3 centered = centerBlock(block);
                float neededYaw = getYawNeededVec(centered, displacement);

                if (!isLeft) {
                    if (neededYaw > 0 && neededYaw < maxRot) {
                        if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass) {
                            if (neededYaw + 10 > displacement) {
                                points += 4;
                            } else {
                                points += 1.5F;
                            }
                        } else {
                            points += 1;
                        }
                        blocks.add(block);
                    }
                } else if (neededYaw < 0 && neededYaw > -maxRot) {
                    if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass) {
                        points += 1.5;
                    } else {
                        points += 1;
                    }

                    blocks.add(block);
                }
            }

            if (!blocks.isEmpty() && !optimalPath.path.isEmpty()) {
                if (getClosestToCamYaw(optimalPath.path, displacement) > getClosestToCamYaw(blocks, displacement)) {
                    points += 10;
                }
            }

            if (bestPoints < points) {
                optimalPath.path = blocks;
                optimalPath.displacement = displacement;
                bestPoints = points;
            }
        }

        isClear = false;

        return optimalPath;
    }

    public static Vec3 centerBlock(BlockPos block) {
        return new Vec3(block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
    }

    public static float getClosestToCamYaw(List<BlockPos> blocks, float displacement) {
        float bestYaw = 10000000;

        for (BlockPos block : blocks) {
            float newYaw = getYawNeededVec(BlockUtils.fromBlockPosToVec3(block), displacement);

            if (newYaw < bestYaw) {
                bestYaw = newYaw;
            }
        }

        return bestYaw;
    }

    public static float getYawNeededVec(Vec3 block, float addCurYaw) {
        LookAt.Rotation rotation = getRotation(block);
        LookAt.Rotation startRot = new LookAt.Rotation(
                ids.mc.thePlayer.rotationPitch,
                ids.mc.thePlayer.rotationYaw + addCurYaw
        );
        LookAt.Rotation neededChange = getNeededChange(startRot, rotation);

        return neededChange.yaw;
    }

    @Getter
    @AllArgsConstructor
    public static class OptimalPath {

        public List<BlockPos> path = null;
        public float displacement = 0;
    }

    @Getter
    @AllArgsConstructor
    public static class OptimalPathRotation {

        public List<BlockPos> path = null;
        public float displacement = 0;
        public float rotation = 0;
    }
}
