package com.dillo.adapter;

import com.dillo.pathfinding.stevebot.core.minecraft.InputBinding;
import net.minecraft.client.settings.KeyBinding;

class McInputBinding implements InputBinding {

    private final KeyBinding binding;

    public McInputBinding(final KeyBinding binding) {
        this.binding = binding;
    }

    @Override
    public int getKeyCode() {
        return binding.getKeyCode();
    }

    @Override
    public boolean isDown() {
        return binding.isKeyDown();
    }
}
