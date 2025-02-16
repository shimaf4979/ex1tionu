package ex12c;

import ex12a.Heuristic;
import ex12a.State;

public interface Evaluator {
    public static Evaluator minCost() {
        return new MinCostEvaluator();
    }

    public static Evaluator bestFirst(Heuristic h) {
        return new BestFirstEvaluator(h);
    }

    public static Evaluator aStar(Heuristic h) {
        return new AStarEvaluator(h);
    }

    float f(State s);

    default float g(State s) {
        return s.cost;
    }
}

class MinCostEvaluator implements Evaluator {
    @Override
    public float f(State s) {
        return g(s);
    }
}

class BestFirstEvaluator implements Evaluator {
    Heuristic h;

    BestFirstEvaluator(Heuristic h) {
        this.h = h;
    }

    @Override
    public float f(State s) {
        return h.eval(s);
    }
}

class AStarEvaluator implements Evaluator {
    Heuristic h;

    AStarEvaluator(Heuristic h) {
        this.h = h;
    }

    @Override
    public float f(State s) {
        return g(s) + h.eval(s);
    }
}
