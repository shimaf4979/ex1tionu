package ex12c;

import ex12a.Heuristic;
import ex12a.State;

public class EightPuzzleHeuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (EightPuzzleWorld) s.world();
        return manhattanDistance(w.board);
    }

    private float manhattanDistance(int[] board) {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            int value = board[i];
            if (value == 0)
                continue; // 空白はスキップ

            // 現在位置
            int currentRow = i / 3;
            int currentCol = i % 3;

            // 目標位置
            int targetRow = (value - 1) / 3;
            int targetCol = (value - 1) % 3;

            // マンハッタン距離を加算
            distance += Math.abs(targetRow - currentRow) +
                    Math.abs(targetCol - currentCol);
        }
        return distance;
    }
}