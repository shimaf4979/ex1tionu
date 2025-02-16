package ex11c;

import java.util.*;

public class MisCanProblem {
    public static void main(String[] args) {
        var solver = new Solver();
        System.out.println("宣教師と人食い人の問題 - 解法\n");
        System.out.println("【凡例】");
        System.out.println("M: 宣教師  C: 人食い人  B: ボート");
        System.out.println("→: 右岸へ移動  ←: 左岸へ移動\n");
        solver.solve(new MisCanWorld(3, 3, 1));
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
    int missionary;
    int cannibal;
    int boat;

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
        if (this.missionary < 0 || this.missionary > 3)
            return false;
        if (this.cannibal < 0 || this.cannibal > 3)
            return false;
        if (this.boat < 0 || this.boat > 1)
            return false;
        if (this.missionary > 0 && this.missionary < this.cannibal)
            return false;
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