package org.firstinspires.ftc.teamcode.framework.util;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;

public abstract class AbstractRobot {

    public AbstractRobot() {

    }

    public abstract void stop();

    public void delay(int time) {
        AbstractOpMode.delay(time);
    }
}
