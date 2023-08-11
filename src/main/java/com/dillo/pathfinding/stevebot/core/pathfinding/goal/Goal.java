package com.dillo.pathfinding.stevebot.core.pathfinding.goal;

import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.rendering.Renderable;

public abstract class Goal {

    /**
     * @param pos the position
     * @return true, if the given position completes this goal
     */
    public abstract boolean reached(BaseBlockPos pos);

    /***
     * @param pos the position
     * @return the estimated cost from the given position to this goal
     */
    public abstract double calcHCost(BaseBlockPos pos);

    /**
     * @return a readable string of this goal.
     */
    public abstract String goalString();

    /**
     * @return a {@link Renderable} representing this goal
     */
    public abstract Renderable createRenderable();
}
