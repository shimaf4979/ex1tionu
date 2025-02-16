package ex12b;

import ex12a.Action;
import java.util.List;

public class EightPuzzleAction implements Action {
    int rowDelta; // 行方向の移動量
    int colDelta; // 列方向の移動量

    // 可能な全アクション
    static List<Action> all = List.of(
            new EightPuzzleAction(-1, 0), // 空白を上に移動
            new EightPuzzleAction(1, 0), // 空白を下に移動
            new EightPuzzleAction(0, -1), // 空白を左に移動
            new EightPuzzleAction(0, 1) // 空白を右に移動
    );

    EightPuzzleAction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    @Override
    public float cost() {
        return 1.0f; // すべての移動のコストを1とする
    }

    @Override
    public String toString() {
        String direction;
        if (rowDelta == -1)
            direction = "上";
        else if (rowDelta == 1)
            direction = "下";
        else if (colDelta == -1)
            direction = "左";
        else
            direction = "右";

        return String.format("空白を%sに移動", direction);
    }
}