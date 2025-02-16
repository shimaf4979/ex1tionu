package ex12d;

import ex12a.Heuristic;
import ex12a.State;

// h'1: マンハッタン距離/4
class H1Heuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (EightPuzzleWorld) s.world();
        return manhattanDistance(w.board) / 4.0f;
    }

    private int manhattanDistance(int[] board) {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            int value = board[i];
            if (value == 0)
                continue;

            int targetRow = (value - 1) / 3;
            int targetCol = (value - 1) % 3;
            int currentRow = i / 3;
            int currentCol = i % 3;

            distance += Math.abs(targetRow - currentRow) +
                    Math.abs(targetCol - currentCol);
        }
        return distance;
    }
}

// h'2: 不一致タイル数
class H2Heuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (EightPuzzleWorld) s.world();
        return misplacedTiles(w.board);
    }

    private int misplacedTiles(int[] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0 && board[i] != i + 1) {
                count++;
            }
        }
        return count;
    }
}

// h'3: マンハッタン距離 + 2 * リニアコンフリクト
class H3Heuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (EightPuzzleWorld) s.world();
        return manhattanDistance(w.board) + 2 * linearConflicts(w.board);
    }

    private int manhattanDistance(int[] board) {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            int value = board[i];
            if (value == 0)
                continue;

            int targetRow = (value - 1) / 3;
            int targetCol = (value - 1) % 3;
            int currentRow = i / 3;
            int currentCol = i % 3;

            distance += Math.abs(targetRow - currentRow) +
                    Math.abs(targetCol - currentCol);
        }
        return distance;
    }

    private int linearConflicts(int[] board) {
        int conflicts = 0;

        // 行のコンフリクトをチェック
        for (int row = 0; row < 3; row++) {
            conflicts += countRowConflicts(board, row);
        }

        // 列のコンフリクトをチェック
        for (int col = 0; col < 3; col++) {
            conflicts += countColConflicts(board, col);
        }

        return conflicts;
    }

    private int countRowConflicts(int[] board, int row) {
        int conflicts = 0;
        for (int j = 0; j < 3; j++) {
            int value1 = board[row * 3 + j];
            if (value1 == 0)
                continue;
            if ((value1 - 1) / 3 != row)
                continue;

            for (int k = j + 1; k < 3; k++) {
                int value2 = board[row * 3 + k];
                if (value2 == 0)
                    continue;
                if ((value2 - 1) / 3 != row)
                    continue;

                if (value1 > value2)
                    conflicts++;
            }
        }
        return conflicts;
    }

    private int countColConflicts(int[] board, int col) {
        int conflicts = 0;
        for (int i = 0; i < 3; i++) {
            int value1 = board[i * 3 + col];
            if (value1 == 0)
                continue;
            if ((value1 - 1) % 3 != col)
                continue;

            for (int k = i + 1; k < 3; k++) {
                int value2 = board[k * 3 + col];
                if (value2 == 0)
                    continue;
                if ((value2 - 1) % 3 != col)
                    continue;

                if (value1 > value2)
                    conflicts++;
            }
        }
        return conflicts;
    }
}