package org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.opmodes.autonomous.steps;


import org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.robot.Robot;

public class Step_Wait implements StepInterface {

    private Robot robot;
    private Long waitTimeMillSec;
    private Long endTimeMillSeconds;

    //constructor
    public Step_Wait(Long waitMillSec) {

        this.waitTimeMillSec = waitMillSec;


    }

    @Override
    public String getLogKey() {
        return "Step_Wait";
    }


    @Override
    public void runStep() {

        if (this.endTimeMillSeconds == null) {
            this.endTimeMillSeconds = System.currentTimeMillis() + this.waitTimeMillSec;
        }

    }


    @Override
    public boolean isStepDone() {

        if (this.endTimeMillSeconds == null) {
            return false;
        }

        return (System.currentTimeMillis() > this.endTimeMillSeconds);
    }


    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


}
