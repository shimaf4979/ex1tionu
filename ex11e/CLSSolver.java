package ex11e;

import java.util.*;

public class CLSSolver {
    private SearchMetrics metrics;

    public CLSSolver() {
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
            System.out.println("解が見つかりませんでした");
        }

        metrics.printMetrics("繰り返し回避型探索(CLS)");
    }

    private State search(State root) {
        var openList = new Stack<SearchNode>();
        openList.push(new SearchNode(root, new HashSet<>()));

        while (!openList.isEmpty()) {
            metrics.updateMaxOpenListSize(openList.size());
            var node = openList.pop();
            var state = node.state;
            var path = node.pathStates;
            metrics.incrementVisitedNodes();
            metrics.updateMaxDepth(path.size());

            if (state.isGoal()) {
                return state;
            }

            var stateStr = state.world.toString();
            if (path.contains(stateStr)) {
                continue;
            }

            var newPath = new HashSet<>(path);
            newPath.add(stateStr);

            var children = state.children();
            for (var child : children) {
                openList.push(new SearchNode(child, newPath));
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

        System.out.println("\n【繰り返し回避型探索による解法手順】");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("\nステップ %d:\n", i);
            System.out.println(list.get(i).world);
            if (i < list.size() - 1) {
                System.out.println("   " + list.get(i + 1).action);
            }
        }
    }

    private static class SearchNode {
        State state;
        Set<String> pathStates;

        SearchNode(State state, Set<String> pathStates) {
            this.state = state;
            this.pathStates = pathStates;
        }
    }
}