package com.dillo.pathfinding.stevebot.core.pathfinding.nodes;

import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.math.vectors.vec3.Vector3d;
import com.dillo.pathfinding.stevebot.core.rendering.Color;
import com.dillo.pathfinding.stevebot.core.rendering.Renderable;
import com.dillo.pathfinding.stevebot.core.rendering.Renderer;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class NodeRenderable implements Renderable {

    private final Map<BaseBlockPos, Node> nodes;

    public NodeRenderable(Map<BaseBlockPos, Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void render(Renderer renderer) {
        renderAABB(renderer);
    }

    private void renderAABB(Renderer renderer) {
        renderer.beginBoxes(2);
        final Vector3d pos = new Vector3d();
        try {
            for (Node node : nodes.values()) {
                BaseBlockPos nodePos = node.getPos();
                pos.set(nodePos.getX(), nodePos.getY(), nodePos.getZ());
                renderer.drawBoxOpen(pos, (node.isOpen() ? Color.WHITE : Color.GRAY));
            }
        } catch (ConcurrentModificationException e) {
            // ignore
        }
        renderer.end();
    }
}
