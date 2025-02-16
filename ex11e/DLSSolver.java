package ex11e;

import java.util.*;

public class DLSSolver {
    private SearchMetrics metrics;
    private final int depthLimit;

    public DLSSolver(int depthLimit) {
        this.depthLimit = depthLimit;
        this.metrics = new SearchMetrics();
    }

    public void solve(World world) {
        metrics.startTimer();
        var root = new State(null, null, world);
        var goal = depthLimitedSearch(root);
        metrics.stopTimer();

        if (goal != null) {
            printSolution(goal);
        } else {
            System.out.println("解が見つかりませんでした（深さ制限: " + depthLimit + "）");
        }

        metrics.printMetrics("深さ制限探索(DLS)");
    }

    private State depthLimitedSearch(State root) {
        var stack = new Stack<SearchNode>();
        stack.push(new SearchNode(root, 0));
        var visited = new HashSet<String>();

        while (!stack.isEmpty()) {
            metrics.updateMaxOpenListSize(stack.size());
            var node = stack.pop();
            var state = node.state;
            var depth = node.depth;
            metrics.updateMaxDepth(depth);
            metrics.incrementVisitedNodes();

            if (state.isGoal()) {
                return state;
            }

            var stateStr = state.world.toString();
            if (visited.contains(stateStr)) {
                continue;
            }
            visited.add(stateStr);

            if (depth < depthLimit) {
                var children = state.children();
                for (var child : children) {
                    stack.push(new SearchNode(child, depth + 1));
                }
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

        System.out.println("\n【深さ制限探索による解法手順】（制限: " + depthLimit + "）");
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
        int depth;

        SearchNode(State state, int depth) {
            this.state = state;
            this.depth = depth;
        }
    }
}