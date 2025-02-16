package ex11b;

import java.util.*;
import java.util.stream.*;

interface World extends Cloneable {
    boolean isValid();

    boolean isGoal();

    List<Action> actions();

    World successor(Action action);
}

interface Action {
}

class State {
    State parent;
    Action action;
    World world;

    State(State parent, Action action, World child) {
        this.action = action;
        this.parent = parent;
        this.world = child;
    }

    public boolean isGoal() {
        return this.world.isGoal();
    }

    List<State> children() {
        return this.world.actions().stream()
                .map(a -> new State(this, a, this.world.successor(a)))
                .filter(s -> s.world.isValid())
                .toList();
    }

    public String toString() {
        if (this.action != null) {
            return String.format("%s (%s)", this.world, this.action);
        } else {
            return this.world.toString();
        }
    }
}

public class Solver {
    public void solve(World world) {
        var root = new State(null, null, world);
        var goal = search(root);

        if (goal != null) {
            printSolution(goal);
        }
    }

    State search(State root) {
        var openList = toMutable(List.of(root));

        while (!openList.isEmpty()) {
            var state = get(openList);

            if (state.isGoal()) {
                return state;
            }

            var children = state.children();
            openList = concat(openList, children);
        }

        return null;
    }

    State get(List<State> list) {
        return list.remove(0);
    }

    List<State> concat(List<State> xs, List<State> ys) {
        return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
    }

    List<State> toMutable(List<State> list) {
        return new ArrayList<>(list);
    }

    void printSolution(State goal) {
        var list = new ArrayList<State>();

        while (goal != null) {
            list.add(0, goal);
            goal = goal.parent;
        }

        list.forEach(System.out::println);
    }
}
