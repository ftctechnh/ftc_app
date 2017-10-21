package org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.opmodes.autonomous.steps;


import android.util.Log;

import org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.robot.Robot;

public class Step_Beacon implements StepInterface {


    private static final double SERVO_PRESS_BUTTON_A__PRESS_POSITION = 0.7;
    private static final double SERVO_PRESS_BUTTON_B__PRESS_POSITION = 0.7;

    private static final double SERVO_PRESS_BUTTON_A__RESET_POSITION = 0.0;
    private static final double SERVO_PRESS_BUTTON_B__RESET_POSITION = 0.0;


    private static final int RED_LIGHT_DETECTED_VALUE = 5;
    private static final int BLUE_LIGHT_DETECTED_VALUE = 5;

    private Robot.TeamEnum teamEnum;

    private Robot.TeamEnum detectedBeaconColor;
    private Robot robot;
    private Long targetWaitTimeMillSec;
    private boolean buttonPressed_Done_Flag = false;
    private boolean resetButtonPressing_Done_Flag = false;

    //constructor
    public Step_Beacon(Robot.TeamEnum teamEnum) {

        this.teamEnum = teamEnum;
    }

    @Override
    public String getLogKey() {
        return "Step_Beacon";
    }


    @Override
    public void runStep() {

        if (this.buttonPressed_Done_Flag == true &&
                this.resetButtonPressing_Done_Flag == false &&
                isStillTimeWaiting() == false) {
            resetButtonPressing();;
            return;
        }

        if (this.resetButtonPressing_Done_Flag) {
            return;
        }

        if (detectedBeaconColor()) {

            if (this.teamEnum.equals(this.detectedBeaconColor)) {

                pressButtonA();

            } else {

                pressButtonB();

            }
        }

    }


    private boolean detectedBeaconColor() {

        int red = robot.colorSensorBeacon.red();
        int blue = robot.colorSensorBeacon.blue();

        if (isOnlyRedLightDetected()) {
            logIt("FOUND COLOR RED! " + red);
            this.detectedBeaconColor = Robot.TeamEnum.RED;
            return true;
        }
        if (isOnlyBlueLightDetected()) {
            logIt("FOUND COLOR BLUE! " + blue);
            this.detectedBeaconColor = Robot.TeamEnum.BLUE;
            return true;
        }
        logIt("NO SINGLE COLOR FOUND!");

        return false;

    }

    private boolean isOnlyRedLightDetected() {

        if (robot.colorSensorBeacon.red() >= RED_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeacon.blue() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE BLUE:" + robot.colorSensorBeacon.red() + "/" + robot.colorSensorBeacon.blue());

            return true;
        }


        return false;
    }

    private boolean isOnlyBlueLightDetected() {


        if (robot.colorSensorBeacon.blue() >= BLUE_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeacon.red() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE BLUE:" + robot.colorSensorBeacon.red() + "/" + robot.colorSensorBeacon.blue());

            return true;
        }


        return false;
    }

    private void pressButtonA() {

        if (buttonPressed_Done_Flag == false) {
            robot.servoRightButtonA.setPosition(SERVO_PRESS_BUTTON_A__PRESS_POSITION);
            robot.servoLeftButtonA.setPosition(SERVO_PRESS_BUTTON_A__PRESS_POSITION);
            setLoopTimeDelay();
            buttonPressed_Done_Flag = true;

            logIt("pressButtonA");
        }
    }

    private void pressButtonB() {

        if (buttonPressed_Done_Flag == false) {
            robot.servoRightButtonB.setPosition(SERVO_PRESS_BUTTON_B__PRESS_POSITION);
            robot.servoLeftButtonB.setPosition(SERVO_PRESS_BUTTON_B__PRESS_POSITION);
            setLoopTimeDelay();
            buttonPressed_Done_Flag = true;
            logIt("pressButtonB");
        }
    }


    private void resetButtonPressing() {


        robot.servoRightButtonA.setPosition(SERVO_PRESS_BUTTON_A__RESET_POSITION);
        robot.servoRightButtonB.setPosition(SERVO_PRESS_BUTTON_B__RESET_POSITION);
        robot.servoLeftButtonA.setPosition(SERVO_PRESS_BUTTON_A__RESET_POSITION);
        robot.servoLeftButtonB.setPosition(SERVO_PRESS_BUTTON_B__RESET_POSITION);
        setLoopTimeDelay();

        resetButtonPressing_Done_Flag = true;

        logIt("ResetButtonPressing");
    }

    private void setLoopTimeDelay() {

        targetWaitTimeMillSec = System.currentTimeMillis() + 1000;
    }


    private boolean isStillTimeWaiting() {

        if (targetWaitTimeMillSec > System.currentTimeMillis()) {
            logIt("STILL WAITING: " + (targetWaitTimeMillSec - System.currentTimeMillis()));
            return true;
        }
        return false;

    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + " " + robot.loopCounter);
        sb.append(" Red:" + robot.colorSensorBeacon.red());
        sb.append(" Blue:" + robot.colorSensorBeacon.blue());
        Log.i(getLogKey(), sb.toString());
    }

    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


    @Override
    public boolean isStepDone() {

        if (this.resetButtonPressing_Done_Flag && isStillTimeWaiting() == false) {

            logIt("DONE");
            return true;
        }

        return false;
    }

}
