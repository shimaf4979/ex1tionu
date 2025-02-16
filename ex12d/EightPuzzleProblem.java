package ex12d;

import ex12a.Heuristic;
import ex12a.Evaluator;
import ex12a.State;
import ex12a.World;
import java.util.*;

public class EightPuzzleProblem {
    public static void main(String[] args) {
        // 難しい8パズルの初期状態
        int[] initialState = { 8, 6, 7, 5, 0, 4, 2, 3, 1 };
        var puzzle = new EightPuzzleWorld(initialState);
        var metrics = new SearchMetrics();

        System.out.println("難しい8パズル問題 - 3種類のヒューリスティック関数の比較");
        System.out.println("初期状態:");
        System.out.println(puzzle);
        System.out.println("目標状態:");
        System.out.println(EightPuzzleWorld.goal);

        // 3つのヒューリスティック関数を準備
        var h1 = new H1Heuristic();
        var h2 = new H2Heuristic();
        var h3 = new H3Heuristic();

        // h'1を使用したA*探索
        System.out.println("\nA*探索 with h'1 (マンハッタン距離/4)");
        var aStarH1 = new InformedSolver(Evaluator.aStar(h1), metrics);
        aStarH1.solve(puzzle.clone());

        // h'2を使用したA*探索
        System.out.println("\nA*探索 with h'2 (不一致タイル数)");
        metrics.reset();
        var aStarH2 = new InformedSolver(Evaluator.aStar(h2), metrics);
        aStarH2.solve(puzzle.clone());

        // h'3を使用したA*探索
        System.out.println("\nA*探索 with h'3 (マンハッタン距離+2*リニアコンフリクト)");
        metrics.reset();
        var aStarH3 = new InformedSolver(Evaluator.aStar(h3), metrics);
        aStarH3.solve(puzzle.clone());

        // ヒューリスティック関数の比較
        System.out.println("\nヒューリスティック関数の比較例:");
        compareHeuristics(puzzle, h1, h2, h3);
    }

    private static void compareHeuristics(EightPuzzleWorld world,
            Heuristic h1, Heuristic h2, Heuristic h3) {
        var state = new State(null, null, world);
        float h1Value = h1.eval(state);
        float h2Value = h2.eval(state);
        float h3Value = h3.eval(state);

        System.out.printf("初期状態での評価値:\n");
        System.out.printf("h'1(n) = %.1f (マンハッタン距離/4)\n", h1Value);
        System.out.printf("h'2(n) = %.1f (不一致タイル数)\n", h2Value);
        System.out.printf("h'3(n) = %.1f (マンハッタン距離+2*リニアコンフリクト)\n", h3Value);
        System.out.printf("\n大小関係: %.1f ≤ %.1f ≤ h(n) ≤ %.1f\n",
                h1Value, h2Value, h3Value);
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

        metrics.printMetrics(eval.getClass().getSimpleName() + "の性能評価");
    }

    private State search(State root) {
        var openList = new PriorityQueue<State>(
                (s1, s2) -> Float.compare(eval.f(s1), eval.f(s2)));
        var closedSet = new HashSet<String>();

        openList.add(root);

        while (!openList.isEmpty()) {
            metrics.updateMaxOpenListSize(openList.size());
            metrics.updateMaxClosedListSize(closedSet.size());

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
                var childStr = child.world().toString();
                if (!closedSet.contains(childStr)) {
                    openList.add(child);
                }
            }
        }
        return null;
    }

    private void printSolution(State goal) {
        var list = new ArrayList<State>();
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
        System.out.println("\n総ステップ数: " + (list.size() - 1));
    }
}