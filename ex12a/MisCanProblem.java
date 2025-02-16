package ex12a;

import java.util.*;

public class MisCanProblem {
    public static void main(String[] args) {
        System.out.println("1. 最小コスト探索");
        System.out.println("================");
        var minCostSearch = new InformedSolver(Evaluator.minCost());
        minCostSearch.solve(new MisCanWorld(3, 3, 1));

        System.out.println("\n2. 最良優先探索");
        System.out.println("================");
        var bestFirstSearch = new InformedSolver(Evaluator.bestFirst(new MisCanHeuristic()));
        bestFirstSearch.solve(new MisCanWorld(3, 3, 1));

        System.out.println("\n3. A*探索");
        System.out.println("================");
        var aStarSearch = new InformedSolver(Evaluator.aStar(new BetterMisCanHeuristic()));
        aStarSearch.solve(new MisCanWorld(3, 3, 1));
    }
}

class MisCanAction implements Action {
    int missionary;
    int cannibal;
    int boat;

    static List<Action> all = List.of(
            new MisCanAction(-2, 0, -1), // 宣教師2人を右岸へ
            new MisCanAction(-1, -1, -1), // 宣教師1人と人食い人1人を右岸へ
            new MisCanAction(0, -2, -1), // 人食い人2人を右岸へ
            new MisCanAction(-1, 0, -1), // 宣教師1人を右岸へ
            new MisCanAction(0, -1, -1), // 人食い人1人を右岸へ
            new MisCanAction(+2, 0, +1), // 宣教師2人を左岸へ
            new MisCanAction(+1, +1, +1), // 宣教師1人と人食い人1人を左岸へ
            new MisCanAction(0, +2, +1), // 人食い人2人を左岸へ
            new MisCanAction(+1, 0, +1), // 宣教師1人を左岸へ
            new MisCanAction(0, +1, +1) // 人食い人1人を左岸へ
    );

    MisCanAction(int missionary, int cannibal, int boat) {
        this.missionary = missionary;
        this.cannibal = cannibal;
        this.boat = boat;
    }

    @Override
    public float cost() {
        // すべての移動アクションのコストを1とする
        return 1.0f;
    }

    @Override
    public String toString() {
        var dir = this.boat < 0 ? "→" : "←";
        var m = Math.abs(this.missionary);
        var c = Math.abs(this.cannibal);
        var actors = new ArrayList<String>();
        if (m > 0)
            actors.add(m + "人の宣教師");
        if (c > 0)
            actors.add(c + "人の人食い人");
        return String.format("%sが%s移動", String.join("と", actors), dir);
    }
}

class MisCanWorld implements World {
    int missionary; // 左岸の宣教師の数
    int cannibal; // 左岸の人食い人の数
    int boat; // ボートの位置（1:左岸, 0:右岸）

    public MisCanWorld(int missionary, int cannibal, int boat) {
        this.missionary = missionary;
        this.cannibal = cannibal;
        this.boat = boat;
    }

    public MisCanWorld clone() {
        return new MisCanWorld(this.missionary, this.cannibal, this.boat);
    }

    @Override
    public boolean isValid() {
        // 数の制約
        if (this.missionary < 0 || this.missionary > 3)
            return false;
        if (this.cannibal < 0 || this.cannibal > 3)
            return false;
        if (this.boat < 0 || this.boat > 1)
            return false;

        // 左岸での宣教師の安全性
        if (this.missionary > 0 && this.missionary < this.cannibal)
            return false;

        // 右岸での宣教師の安全性
        if ((3 - this.missionary) > 0 && (3 - this.missionary) < (3 - this.cannibal))
            return false;

        return true;
    }

    @Override
    public boolean isGoal() {
        return this.missionary == 0 && this.cannibal == 0;
    }

    @Override
    public List<Action> actions() {
        return MisCanAction.all;
    }

    @Override
    public World successor(Action action) {
        var a = (MisCanAction) action;
        var next = clone();
        next.missionary += a.missionary;
        next.cannibal += a.cannibal;
        next.boat += a.boat;
        return next;
    }

    @Override
    public String toString() {
        // 左岸の状態
        StringBuilder leftBank = new StringBuilder("左岸: ");
        if (this.missionary > 0)
            leftBank.append("M".repeat(this.missionary));
        if (this.cannibal > 0)
            leftBank.append("C".repeat(this.cannibal));
        while (leftBank.length() < 10)
            leftBank.append(" ");

        // 右岸の状態
        StringBuilder rightBank = new StringBuilder("右岸: ");
        if (3 - this.missionary > 0)
            rightBank.append("M".repeat(3 - this.missionary));
        if (3 - this.cannibal > 0)
            rightBank.append("C".repeat(3 - this.cannibal));
        while (rightBank.length() < 10)
            rightBank.append(" ");

        // ボートの位置
        String river = this.boat == 1 ? " [B~~~~] " : " [~~~~B] ";

        return leftBank.toString() + river + rightBank.toString();
    }
}

class MisCanHeuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (MisCanWorld) s.world();
        // 左岸の宣教師の数をヒューリスティック値として使用
        return w.missionary;
    }
}

class BetterMisCanHeuristic implements Heuristic {
    @Override
    public float eval(State s) {
        var w = (MisCanWorld) s.world();
        // 左岸の宣教師と人食い人の合計をヒューリスティック値として使用
        return w.missionary + w.cannibal;
    }
}