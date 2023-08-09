package com.dillo.main.files.readwrite;

import net.minecraft.util.BlockPos;

import java.io.File;

public class AddBlockToRoute {

    public static void addBlockToRoute(BlockPos block, File file) {
        WriteFile.writeFile(file, block);
    }
}
