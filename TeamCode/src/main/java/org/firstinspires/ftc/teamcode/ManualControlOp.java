
package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Robot;

@Autonomous(name="SimpleRobot", group="Concept")

public class ManualControlOp extends OpMode {
    private static final String KEY = "Manual";
    public static final double SERVO_OPEN = 0.7;
    public static final int SERVO_CLOSE = 0;

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
        operator_controlPaddles();

    }

    private void operator_controlPaddles() {

        if (gamepad1.x) {
            robot.servoLeftPaddle.setPosition(SERVO_OPEN);
            robot.servoRightPaddle.setPosition(SERVO_OPEN);
        }
        if (gamepad1.b) {
            robot.servoLeftPaddle.setPosition(SERVO_CLOSE);
            robot.servoRightPaddle.setPosition(SERVO_CLOSE);
        }

    }


    private void driver_controlDriveMotors() {

        // write the values to the motors
        robot.motorRight.setPower(limitValue(gamepad1.right_stick_y));
        robot.motorLeft.setPower(limitValue(gamepad1.left_stick_y));

        Log.i(KEY, "--Robot");
        Log.i(KEY, "WHEELS: [" + String.format("%.0f", robot.motorRight.getPower() * 100) + "]---[" + String.format("%.0f", robot.motorLeft.getPower() * 100) + "]");
        Log.i(KEY, "------------------------------------------");

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
