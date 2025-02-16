package ex12a;

import java.util.*;

public class State {
    State parent;
    World world;
    Action action;
    public float cost;

    public State(State parent, Action action, World child) {
        this.parent = parent;
        this.action = action;
        this.world = child;

        if (parent != null && action != null) {
            this.cost = parent.cost + action.cost();
        } else {
            this.cost = 0;
        }
    }

    public State parent() {
        return this.parent;
    }

    public World world() {
        return this.world;
    }

    public Action action() {
        return this.action;
    }

    public float cost() {
        return this.cost;
    }

    public boolean isGoal() {
        return this.world.isGoal();
    }

    public List<State> children() {
        return this.world.actions().stream()
                .map(a -> new State(this, a, this.world.successor(a)))
                .filter(s -> s.world.isValid())
                .toList();
    }

    @Override
    public String toString() {
        var str = this.world.toString() + "@" + this.cost;
        if (this.action != null) {
            str = String.format("%s (%s)", str, this.action);
        }
        return str;
    }
}
