
package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.manualcontrol;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;


public class ManualControlOp extends OpMode {
    private static final String KEY = "Manual";

    private Robot robot;

    private int loopCounter = 0;


    public ManualControlOp() {
    }


    @Override
    public void init() {

        this.robot = new Robot(hardwareMap, telemetry);
    }


    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        loopCounter = loopCounter + 1;

        driver_controlDriveMotors();
//        operator_controlBeaconButton();

    }

    private void operator_controlBeaconButton() {

        if (gamepad1.y) {

            robot.servoLeftButtonA.setPosition(.7);
            robot.servoRightButtonA.setPosition(.7);
        } else {
            robot.servoLeftButtonA.setPosition(0);
            robot.servoRightButtonA.setPosition(0);
        }
    }


    private void driver_controlDriveMotors() {

        float weightFactor_Forward = 0.33f;
        float weightFactor_TankTurn = 0.33f;
        float weightFactor_Strafing = 0.33f;

        float forward = -gamepad1.left_stick_y * weightFactor_Forward;
        float tankTurn = gamepad1.right_stick_x * weightFactor_TankTurn;
        float strafing = gamepad1.left_stick_x * weightFactor_Strafing;

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        float frontLeft = (float) forward + tankTurn + strafing;
        float rearLeft = (float) forward + tankTurn - strafing;
        float frontRight = (float) forward - tankTurn - strafing;
        float rearRight = (float) forward - tankTurn + strafing;


        float maxSpeedfactor = calculateMaxSpeedFactor(frontLeft, rearLeft, frontRight, rearRight);


        // write the values to the motors
        robot.motorRightFront.setPower(limitValue(frontRight * maxSpeedfactor));
        robot.motorRightRear.setPower(limitValue(rearRight * maxSpeedfactor));
        robot.motorLeftFront.setPower(limitValue(frontLeft * maxSpeedfactor));
        robot.motorLeftRear.setPower(limitValue(rearLeft * maxSpeedfactor));

        Log.i(KEY, "--Robot");
        Log.i(KEY, "WHEELS: [" + String.format("%.0f", robot.motorRightFront.getPower() * 100) + "]---[" + String.format("%.0f", robot.motorLeftFront.getPower() * 100) + "]");
        Log.i(KEY, "WHEELS: [" + String.format("%.0f", robot.motorRightRear.getPower() * 100) + "]---[" + String.format("%.0f", robot.motorLeftRear.getPower() * 100) + "]");
        Log.i(KEY, "------------------------------------------");

    }

    private float calculateMaxSpeedFactor(float value1, float value2, float value3, float value4) {


        float maxValue = abs(value1);

        if (maxValue < abs(value2)) {
            maxValue = abs(value2);
        }

        if (maxValue < abs(value3)) {
            maxValue = abs(value3);
        }

        if (maxValue < abs(value4)) {
            maxValue = abs(value4);
        }


        if (maxValue < 20) {
            return 1.0f;
        }

        float maxSpeedFactor = 100 / maxValue;


        return maxSpeedFactor;
    }


    private static float abs(float a) {
        return (a <= 0.0F) ? 0.0F - a : a;
    }


    private float limitValue(float input) {

        if (input > 1.0) {

            return 1.0f;
        } else if (input < -1.0) {
            return -1.0f;
        }
        return input;
    }


    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

        telemetry.addData("TextStop", "***Stop happened**" + loopCounter);

    }

    /*
     * This method scales the joystick inputValue so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleOutput(double inputValue, double[] scaleArray) {


        // get the corresponding index for the scaleOutput array.
        int index = (int) (inputValue * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (inputValue < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
