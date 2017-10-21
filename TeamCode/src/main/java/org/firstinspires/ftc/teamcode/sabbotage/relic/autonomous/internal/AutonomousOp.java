
package org.firstinspires.ftc.teamcode.sabbotage.relic.autonomous.internal;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import org.firstinspires.ftc.teamcode.sabbotage.relic.robot.Robot;

import java.util.ArrayList;
import java.util.List;


public abstract class AutonomousOp extends OpMode {


    private static final String KEY = "AutonomousOp";

    abstract protected ArrayList<StepInterface> definedStepList();

    private List<StepInterface> stepList = null;
    private Robot robot = null;
    private int delayUntilLoopCount = 0;
    private int activeStepNumber = 0;
    private boolean init_HardwarePositions;
    private long MAX_RUN_TIME_MILLI_SECONDS = 31 * 1000;
    private Long startTimeMilliSeconds = null;
    private boolean rescueIsAborted = false;


    @Override
    public void init() {
        this.robot = new Robot(hardwareMap, telemetry);
        this.stepList = definedStepList();
        init_calibrateGyroSensors();

    }

    private void init_calibrateGyroSensors() {


//        robot.gyroSensor.calibrate();

    }


    @Override
    public void loop() {

        robot.loopCounter = robot.loopCounter + 1;

        initStartTime();

        if (isWaiting() || isCalibratingGyroSensor()) {
            Log.i(KEY, "isWaiting:" + isWaiting() + " isCalibratingGyroSensor:" + isCalibratingGyroSensor());

            return;
        }


        sendMessageToDriverPhone();

        performRescue();

    }


    private boolean isWaiting() {
        return this.delayUntilLoopCount > robot.loopCounter;
    }

    private boolean isCalibratingGyroSensor() {

//        return (robot.gyroSensor.isCalibrating());
        return false;
    }

    private void initStartTime() {

        if (this.startTimeMilliSeconds == null) {
            this.startTimeMilliSeconds = System.currentTimeMillis();
        }
    }


    @Override
    public void stop() {


        sendMessageToDriverPhone();

        robot.resetEncodersAndStopMotors();

        robot.motorDriveLeft.setPower(0);
        robot.motorDriveRight.setPower(0);
    }

    private void sendMessageToDriverPhone() {
//        robot.telemetry.addData("Status1", "Rescue Step:" + this.activeStepNumber + " Loop:" + robot.loopCounter);

    }


    private void performRescue() {

        if (isRescueDone() || this.rescueIsAborted) {

            celebrate();

        } else {

            StepInterface activeStep = stepList.get(activeStepNumber);

            activeStep.setRobot(this.robot);
            activeStep.runStep();

            if (activeStep.isAborted()) {

                rescueIsAborted = true;
                return;
            }


            if (activeStep.isStepDone()) {
                robot.resetEncodersAndStopMotors();
                logDebugInfo(activeStep.getLogKey() + activeStepNumber);
                activeStepNumber = activeStepNumber + 1;
                setLoopDelay();
            }

        }


    }


    private void celebrate() {

//        robot.telemetry.addData("CELEBRATE with Happy Dance", this.activeStepNumber + " Loop:" + robot.loopCounter);
//        robot.telemetry.addData("DURATION", (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));
        Log.i(KEY + "_END", "DURATION: " + (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));
        stop();

    }

    private void logDebugInfo(String keySuffix) {


        String key = "A_" + keySuffix;

        Log.i(key, "--------------------------------------------------------------------------------------");
        Log.i(key, " Motor Mode:" + robot.motorDriveLeft.getMode() + " --- " + robot.motorDriveRight.getMode());
        Log.i(key, "-------------------------------");
        Log.i(key, " speed:" + robot.motorDriveLeft.getPower() + " --- " + robot.motorDriveRight.getPower());
        Log.i(key, "-------------------------------");
        Log.i(key, " zero:" + robot.motorDriveLeft.getZeroPowerBehavior() + " --- " + robot.motorDriveRight.getZeroPowerBehavior());
        Log.i(key, "--------------------------------------------------------------------------------------");
    }


    private boolean isRescueAborted() {

        return false;
    }

    private boolean isRescueDone() {


        return isAutoTimeOver() || activeStepNumber >= stepList.size();
    }

    private boolean isAutoTimeOver() {

        if (System.currentTimeMillis() >= this.startTimeMilliSeconds + MAX_RUN_TIME_MILLI_SECONDS) {

            Log.i(KEY + "_TOUT", "DURATION: " + (System.currentTimeMillis() / 1000 - this.startTimeMilliSeconds / 1000));

            return true;
        }
        return false;
    }


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + 10;
    }

    public static interface StepInterface {


        void setRobot(Robot robot);

        void runStep();

        boolean isStepDone();

        boolean isAborted();

        String getLogKey();

    }
}
