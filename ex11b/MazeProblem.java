package ex11b;

import java.util.*;

public class MazeProblem {
    public static void main(String[] args) {
        var solver = new Solver();
        solver.solve(new MazeWorld("A"));
    }
}

class MazeAction implements Action {
    String next;

    MazeAction(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "move to " + this.next;
    }
}

class MazeWorld implements World {
    Map<String, List<String>> map = Map.of(
            "A", List.of("B", "C", "D"),
            "B", List.of("E", "F"),
            "C", List.of("G", "H"),
            "D", List.of("I", "J"));

    String current;

    MazeWorld(String current) {
        this.current = current;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return "E".equals(this.current);
    }

    @Override
    public List<Action> actions() {
        return this.map.getOrDefault(current, Collections.emptyList())
                .stream()
                .map(p -> (Action) new MazeAction(p))
                .toList();
    }

    @Override
    public World successor(Action action) {
        var a = (MazeAction) action;
        return new MazeWorld(a.next);
    }

    @Override
    public String toString() {
        return this.current;
    }
}
