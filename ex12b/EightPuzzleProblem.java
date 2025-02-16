package ex12b;

import ex12a.State;
import ex12a.World;

public class EightPuzzleProblem {
    public static void main(String[] args) {
        // 初期状態の定義
        int[] initialState = { 2, 3, 5, 7, 1, 6, 4, 8, 0 };
        var puzzle = new EightPuzzleWorld(initialState);
        var heuristic = new EightPuzzleHeuristic();
        var metrics = new SearchMetrics();

        System.out.println("8パズル問題 - 探索アルゴリズムの性能比較");
        System.out.println("初期状態:");
        System.out.println(puzzle);
        System.out.println("目標状態:");
        System.out.println(EightPuzzleWorld.goal);

        // 1. 最小コスト探索
        System.out.println("\n1. 最小コスト探索");
        var minCostSearch = new InformedSolver(Evaluator.minCost(), metrics);
        minCostSearch.solve(puzzle.clone());

        // 2. 最良優先探索
        System.out.println("\n2. 最良優先探索");
        metrics.reset();
        var bestFirstSearch = new InformedSolver(Evaluator.bestFirst(heuristic), metrics);
        bestFirstSearch.solve(puzzle.clone());

        // 3. A*探索
        System.out.println("\n3. A*探索");
        metrics.reset();
        var aStarSearch = new InformedSolver(Evaluator.aStar(heuristic), metrics);
        aStarSearch.solve(puzzle.clone());
    }
}

class InformedSolver {
    private final Evaluator eval;
    private final SearchMetrics metrics;

    public InformedSolver(Evaluator eval, SearchMetrics metrics) {
        this.eval = eval;
        this.metrics = metrics;
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

        metrics.printMetrics(getSolverName());
    }

    private String getSolverName() {
        if (eval instanceof MinCostEvaluator)
            return "最小コスト探索";
        if (eval instanceof BestFirstEvaluator)
            return "最良優先探索";
        if (eval instanceof AStarEvaluator)
            return "A*探索";
        return "Unknown";
    }

    private State search(State root) {
        var openList = new java.util.PriorityQueue<State>(
                (s1, s2) -> Float.compare(eval.f(s1), eval.f(s2)));
        var closedSet = new java.util.HashSet<String>();

        openList.add(root);

        while (!openList.isEmpty()) {
            metrics.updateMaxOpenListSize(openList.size());
            var state = openList.poll();
            metrics.incrementVisitedNodes();

            if (state.isGoal()) {
                return state;
            }

            var stateStr = state.world().toString();
            if (closedSet.contains(stateStr)) {
                continue;
            }
            closedSet.add(stateStr);

            for (var child : state.children()) {
                if (child.world() != null) { // nullチェックを追加
                    openList.add(child);
                }
            }
        }
        return null;
    }

    private void printSolution(State goal) {
        var list = new java.util.ArrayList<State>();
        var currentState = goal;
        while (currentState != null) {
            list.add(0, currentState);
            currentState = currentState.parent();
        }

        System.out.println("\n解法手順:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("\nステップ %d:\n", i);
            System.out.println(list.get(i).world());
            if (i < list.size() - 1) {
                System.out.println("   " + list.get(i + 1).action());
            }
        }
    }
}