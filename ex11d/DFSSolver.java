package ex11d;

import java.util.*;

public class DFSSolver {
    private SearchMetrics metrics;

    public DFSSolver() {
        this.metrics = new SearchMetrics();
    }

    public void solve(World world) {
        metrics.startTimer();
        var root = new State(null, null, world);
        var goal = search(root);
        metrics.stopTimer();

        if (goal != null) {
            printSolution(goal);
        } else {
            System.out.println("解が見つかりませんでした。");
        }

        metrics.printMetrics("縦型探索(DFS)");
    }

    private State search(State root) {
        var openList = new Stack<State>();
        openList.push(root);
        var closedSet = new HashSet<String>();

        while (!openList.isEmpty()) {
            metrics.updateMaxOpenListSize(openList.size());
            var state = openList.pop();
            metrics.incrementVisitedNodes();

            if (state.isGoal()) {
                return state;
            }

            var stateStr = state.world.toString();
            if (closedSet.contains(stateStr)) {
                continue;
            }
            closedSet.add(stateStr);

            var children = state.children();
            for (var child : children) {
                openList.push(child);
            }
        }
        return null;
    }

    private void printSolution(State goal) {
        var list = new ArrayList<State>();
        while (goal != null) {
            list.add(0, goal);
            goal = goal.parent;
        }

        System.out.println("\n【縦型探索による解法手順】");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("\nステップ %d:\n", i);
            System.out.println(list.get(i).world);
            if (i < list.size() - 1) {
                System.out.println("   " + list.get(i + 1).action);
            }
        }
    }
}