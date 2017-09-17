package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public interface StepInterface {


    void setRobot(Robot robot);

    void runStep();

    boolean isStepDone();

    boolean isAborted();

    String getLogKey();

}
