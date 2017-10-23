package edu.usrobotics.opmode.task;

/**
 * Created by dsiegler19 on 11/3/16.
 */
public class Goal<U> {
    private U goal;

    public Goal (U goal) {
        setGoal (goal);
    }

    public U getGoal () {
        return goal;
    }

    public void setGoal(U goal) {
        this.goal = goal;
    }
}
