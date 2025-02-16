package ex12a;

import java.util.*;

public interface World extends Cloneable {
    boolean isValid();

    boolean isGoal();

    List<Action> actions();

    World successor(Action action);

}
