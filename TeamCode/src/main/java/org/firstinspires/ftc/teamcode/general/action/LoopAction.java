package org.firstinspires.ftc.teamcode.general.action;


import java.util.concurrent.Callable;

/**
 * Created by Derek on 2/13/2018.
 */

public class LoopAction implements RobotAction {

    protected Callable<Boolean> action;

    public LoopAction(Callable<Boolean> action) {
        this.action = action;
    }

    public boolean run() {
        assert action != null;

        try {
            return action.call();
        } catch (Exception e) {
            //uh oh

            e.printStackTrace();
        }

        return false;
    }

    public static LoopAction blockAction() {
        return new LoopAction(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return false;
            }
        });
    }

    public static LoopAction waitAction() {
        return new LoopAction(new Callable<Boolean>() {
            Double startTime;
            @Override
            public Boolean call() throws Exception {
                if (startTime == null) startTime = (double) System.currentTimeMillis();

                return System.currentTimeMillis() - startTime >= 350;
            }
        });
    }

}