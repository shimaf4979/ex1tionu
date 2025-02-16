package ex11e;

import java.util.*;

public class IDSSolver {
    private SearchMetrics metrics;
    private final int maxDepth;

    public IDSSolver(int maxDepth) {
        this.maxDepth = maxDepth;
        this.metrics = new SearchMetrics();
    }

    public void solve(World world) {
        metrics.startTimer();
        var root = new State(null, null, world);
        State goal = null;

        System.out.println("反復深化探索を開始...");
        for (int depth = 0; depth <= maxDepth && goal == null; depth++) {
            System.out.printf("深さ %d で探索中...\n", depth);
            goal = depthLimitedSearch(root, depth);
            if (goal == null) {
                System.out.printf("深さ %d では解が見つかりませんでした\n", depth);
            }
        }
        metrics.stopTimer();

        if (goal != null) {
            printSolution(goal);
        } else {
            System.out.println("解が見つかりませんでした（最大深さ: " + maxDepth + "）");
        }

        metrics.printMetrics("反復深化探索(IDS)");
    }

    private State depthLimitedSearch(State root, int depthLimit) {
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

        System.out.println("\n【反復深化探索による解法手順】");
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