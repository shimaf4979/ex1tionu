package ex11e;

import java.util.*;

public class MisCanProblem {
    public static void main(String[] args) {
        System.out.println("宣教師と人食い人の問題 - 探索アルゴリズムの比較\n");
        System.out.println("【凡例】");
        System.out.println("M: 宣教師  C: 人食い人  B: ボート");
        System.out.println("→: 右岸へ移動  ←: 左岸へ移動\n");

        // 深さ制限探索
        var dlsSolver = new DLSSolver(20);
        dlsSolver.solve(new MisCanWorld(3, 3, 1));

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 反復深化探索
        var idsSolver = new IDSSolver(20);
        idsSolver.solve(new MisCanWorld(3, 3, 1));

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 繰り返し回避型探索
        var clsSolver = new CLSSolver();
        clsSolver.solve(new MisCanWorld(3, 3, 1));
    }
}

class MisCanAction implements Action {
    int missionary;
    int cannibal;
    int boat;
    static List<Action> all = List.of(
            new MisCanAction(-2, 0, -1),
            new MisCanAction(-1, -1, -1),
            new MisCanAction(0, -2, -1),
            new MisCanAction(-1, 0, -1),
            new MisCanAction(0, -1, -1),
            new MisCanAction(+2, 0, +1),
            new MisCanAction(+1, +1, +1),
            new MisCanAction(0, +2, +1),
            new MisCanAction(+1, 0, +1),
            new MisCanAction(0, +1, +1));

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

    @Override
    public boolean isValid() {
        if (missionary < 0 || missionary > 3)
            return false;
        if (cannibal < 0 || cannibal > 3)
            return false;
        if (boat < 0 || boat > 1)
            return false;
        if (missionary > 0 && missionary < cannibal)
            return false;
        if ((3 - missionary) > 0 && (3 - missionary) < (3 - cannibal))
            return false;
        return true;
    }

    @Override
    public boolean isGoal() {
        return missionary == 0 && cannibal == 0;
    }

    @Override
    public List<Action> actions() {
        return MisCanAction.all;
    }

    @Override
    public World successor(Action action) {
        var a = (MisCanAction) action;
        return new MisCanWorld(
                missionary + a.missionary,
                cannibal + a.cannibal,
                boat + a.boat);
    }

    @Override
    public String toString() {
        StringBuilder leftBank = new StringBuilder("左岸: ");
        if (missionary > 0)
            leftBank.append("M".repeat(missionary));
        if (cannibal > 0)
            leftBank.append("C".repeat(cannibal));
        while (leftBank.length() < 10)
            leftBank.append(" ");

        StringBuilder rightBank = new StringBuilder("右岸: ");
        if (3 - missionary > 0)
            rightBank.append("M".repeat(3 - missionary));
        if (3 - cannibal > 0)
            rightBank.append("C".repeat(3 - cannibal));
        while (rightBank.length() < 10)
            rightBank.append(" ");

        String river = boat == 1 ? " [B~~~~] " : " [~~~~B] ";

        return leftBank.toString() + river + rightBank.toString();
    }
}
