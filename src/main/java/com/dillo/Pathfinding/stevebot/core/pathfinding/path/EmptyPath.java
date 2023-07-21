package com.dillo.Pathfinding.stevebot.core.pathfinding.path;

import com.dillo.Pathfinding.stevebot.core.pathfinding.actions.ActionCosts;
import com.dillo.Pathfinding.stevebot.core.pathfinding.nodes.Node;
import java.util.Collections;
import java.util.List;

/**
 * A failed path (a path that does not contain any nodes).
 */
public class EmptyPath implements Path {

  @Override
  public double getCost() {
    return ActionCosts.get().COST_INFINITE;
  }

  @Override
  public List<Node> getNodes() {
    return Collections.emptyList();
  }

  @Override
  public boolean reachedGoal() {
    return false;
  }

  @Override
  public Node getFirstNode() {
    return null;
  }

  @Override
  public Node getLastNode() {
    return null;
  }
}
