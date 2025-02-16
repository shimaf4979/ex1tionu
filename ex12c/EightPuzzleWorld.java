package ex12c;

import ex12a.World;
import ex12a.Action;
import java.util.*;

public class EightPuzzleWorld implements World {
    static int[] GOAL = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
    static EightPuzzleWorld goal = new EightPuzzleWorld(GOAL);

    int[] board; // パズルの状態を1次元配列で表現

    public EightPuzzleWorld(int[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    @Override
    public EightPuzzleWorld clone() {
        return new EightPuzzleWorld(this.board);
    }

    @Override
    public boolean isValid() {
        // パズルの状態が有効かチェック
        if (board.length != 9)
            return false;
        boolean[] used = new boolean[9];
        for (int value : board) {
            if (value < 0 || value > 8)
                return false;
            if (used[value])
                return false;
            used[value] = true;
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        return Arrays.equals(this.board, GOAL);
    }

    @Override
    public List<Action> actions() {
        // 現在の状態で可能なアクションのみを返す
        var validActions = new ArrayList<Action>();
        int blankIndex = findBlank();
        int row = blankIndex / 3;
        int col = blankIndex % 3;

        for (Action action : EightPuzzleAction.all) {
            var a = (EightPuzzleAction) action;
            int newRow = row + a.rowDelta;
            int newCol = col + a.colDelta;

            // 移動が盤面の範囲内の場合のみアクションを追加
            if (newRow >= 0 && newRow <= 2 && newCol >= 0 && newCol <= 2) {
                validActions.add(action);
            }
        }

        return validActions;
    }

    @Override
    public World successor(Action action) {
        var a = (EightPuzzleAction) action;
        var next = clone();

        int blankIndex = findBlank();
        int row = blankIndex / 3;
        int col = blankIndex % 3;

        int newRow = row + a.rowDelta;
        int newCol = col + a.colDelta;

        // 移動が盤面の範囲内かチェック
        if (newRow >= 0 && newRow <= 2 && newCol >= 0 && newCol <= 2) {
            int newBlankIndex = newRow * 3 + newCol;
            swap(next.board, blankIndex, newBlankIndex);
            return next;
        }

        // 無効な移動の場合は元の状態を返す（nullは返さない）
        return this.clone();
    }

    private int findBlank() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0)
                return i;
        }
        throw new IllegalStateException("空白(0)が見つかりません");
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EightPuzzleWorld))
            return false;
        EightPuzzleWorld other = (EightPuzzleWorld) obj;
        return Arrays.equals(this.board, other.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(board[i] == 0 ? "_" : board[i]);
            sb.append(" ");
            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}