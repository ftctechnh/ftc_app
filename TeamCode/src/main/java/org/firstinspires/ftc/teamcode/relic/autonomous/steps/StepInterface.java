package org.firstinspires.ftc.teamcode.relic.autonomous.steps;


import org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.robot.Robot;

public interface StepInterface {


    void setRobot(Robot robot);

    void runStep();

    boolean isStepDone();

    boolean isAborted();

    String getLogKey();

}
