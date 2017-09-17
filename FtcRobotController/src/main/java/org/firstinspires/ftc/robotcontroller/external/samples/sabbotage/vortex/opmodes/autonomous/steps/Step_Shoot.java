package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Shoot implements StepInterface {

    private Robot robot;
    private boolean startedToShoot_Flag = false;
    private boolean startedServoDelay_Flag = false;
    private Long targetMotorShootTimeMillSec;
    private Long targetServoDelayTimeMillSec;

    private static final double CLOSE = 0.0;
    private static final double OPEN = 1.0;

    // Constructor, called to create an instance of this class.
    public Step_Shoot() {
    }


    @Override
    public String getLogKey() {
        return "Step_Shoot";
    }


    @Override
    public void runStep() {

        robot.motorShoot.setPower(.75);
        setMotorShootTime_OnlyOnce();
        setServoDelayTime_OnlyOnce();

        positionServo();
    }

    private void positionServo() {

        if (isStillTimeWaitingForServo()) {

            robot.servoShoot.setPosition(CLOSE);
        } else {
            robot.servoShoot.setPosition(OPEN);
        }

    }

    @Override
    public boolean isStepDone() {

        if (isStillTimeWaitingForMotor()) {
            return false;
        }

        robot.motorShoot.setPower(0.0);
        return true;
    }

    private void setMotorShootTime_OnlyOnce() {

        if (startedToShoot_Flag == false) {
            targetMotorShootTimeMillSec = System.currentTimeMillis() + 3000;
            startedToShoot_Flag = true;
        }

    }


    private void setServoDelayTime_OnlyOnce() {

        if (startedServoDelay_Flag == false) {
            targetServoDelayTimeMillSec = System.currentTimeMillis() + 1000;
            startedServoDelay_Flag = true;
        }

    }

    private boolean isStillTimeWaitingForMotor() {

        if (targetMotorShootTimeMillSec > System.currentTimeMillis()) {
            return true;
        }
        return false;

    }

    private boolean isStillTimeWaitingForServo() {

        if (targetServoDelayTimeMillSec > System.currentTimeMillis()) {
            return true;
        }
        return false;

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
