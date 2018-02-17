package org.firstinspires.ftc.teamcode.general.action;

/**
 * Created by Derek on 2/13/2018.
 */

public class LinearAction implements RobotAction {

    protected Runnable action;

    public LinearAction(Runnable action) {
        this.action = action;
    }

    @Override
    public boolean run() {
        action.run();
        return true;
    }

    //for debugging
    public static LinearAction nullAction() {
        return new LinearAction(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
