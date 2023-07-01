package com.dillo.utils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class LoopUtils {
    public static void brLoop(int startX, int startY, int startZ, int radius, BlockOperation lambdaThree) {
        for (int x = startX - radius; x <= startX + radius; x++) {
            for (int y = startY - radius; y <= startY + radius; y++) {
                for (int z = startZ - radius; z <= startZ + radius; z++) {
                    lambdaThree.activate(x, y, z);
                }
            }
        }
    }

    public static void brLoopBoundChunk(int startX, int startY, int startZ, int radius, BlockOperation lambdaThree, int downBoundary, int upperBoundary) {
        int chunkRadius = (int) Math.ceil(radius / 16);
        Chunk startCh = ids.mc.theWorld.getChunkFromBlockCoords(new BlockPos(startX, startY, startZ));
        int startChX = startCh.xPosition;
        int startChZ = startCh.zPosition;
        for (int chX = startChX - chunkRadius; chX <= startChX + chunkRadius; chX++) {
            for (int chZ = startChZ - chunkRadius; chZ <= startChZ + chunkRadius; chZ++) {
                Chunk checkChunk = ids.mc.theWorld.getChunkFromChunkCoords(chX, chZ);
                if (!checkChunk.isLoaded()) continue;
                for (int x = 0; x < 17; x++) {
                    for (int y = downBoundary; y <= upperBoundary; y++) {
                        for (int z = 0; z < 17; z++) {
                            lambdaThree.activate(x + (checkChunk.xPosition * 16), y, z + (checkChunk.zPosition * 16));
                        }
                    }
                }
            }
        }
    }

    public static void brLoopBound(int startX, int startY, int startZ, int radius, BlockOperation lambdaThree, int downBoundary, int upperBoundary) {
        for (int x = startX - radius; x <= startX + radius; x++) {
            for (int y = Math.max(startY - radius, downBoundary); y <= Math.min(startY + radius, upperBoundary); y++) {
                for (int z = startZ - radius; z <= startZ + radius; z++) {
                    lambdaThree.activate(x, y, z);
                }
            }
        }
    }

    public interface BlockOperation {
        void activate(int x, int y, int z);
    }
}
