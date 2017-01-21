package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */

//Add the teleop to the op mode register.
@TeleOp(name="Teleop", group="Teleop Group")
//@Disabled

public class Teleop extends _RobotBase
{
    //Not really required, just initialize everything that needs to be implemented in teleop.
    protected Servo leftSensorServo, rightSensorServo;
    protected final double RIGHT_SERVO_CLOSED = 1.0, LEFT_SERVO_CLOSED = 1.0;

    //All teleop controls are here.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Audio Control Variables

        //Normal mode variables
        double leftPower, rightPower, leftPusherPower, rightPusherPower;
        boolean backwards = true;
        double lastTimeToggleDirectionPressed = 0;
        double speedCoefficient = 1.0;
        double flywheelCoefficient = 0.5;
        boolean capBallMode = false;
        boolean pressingFlywheelC = false;

        //Other motor variables

        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop()).
        while (opModeIsActive())
        {
            /******************** DRIVING CONTROL ********************/
            //Driving Toggle
            if (!backwards)
            { // Driving forward
                leftPower = -gamepad1.right_stick_y;
                rightPower = -gamepad1.left_stick_y;
            } else { // Driving backward
                leftPower = gamepad1.left_stick_y;
                rightPower = gamepad1.right_stick_y;
            }

            if (gamepad1.back) {
                backwards = !backwards;
            }

            // clip the right/left values so that the values never exceed +/- 1
            rightPower = speedCoefficient*Range.clip(rightPower, -1, 1);
            leftPower = speedCoefficient*Range.clip(leftPower, -1, 1);


            // Write the values to the motors.  Scale the robot in order to run the robot more effectively at slower speeds.
            for (DcMotor lMotor : leftDriveMotors)
                lMotor.setPower(scaleInput(leftPower));
            for (DcMotor rMotor : rightDriveMotors)
                rMotor.setPower(scaleInput(rightPower));

            //Wait a second before switching to backwards again (can only toggle once every second).
            if (gamepad1.back && (System.currentTimeMillis() - lastTimeToggleDirectionPressed) > 1000)
            {
                backwards = !backwards; // Switch driving direction
                lastTimeToggleDirectionPressed = System.currentTimeMillis();
                outputNewLineToDriverStation("Toggled drive mode to " + (backwards ? "backwards" : "forwards"));
            }

            if (gamepad1.left_bumper) {
                speedCoefficient = 0.7;
                backwards = false;
            }
            else if (gamepad1.left_trigger > 0.5) {
                speedCoefficient = 1.0;
                backwards = true;
            }

            /******************** OTHER MOTORS ********************/

            //Harvester (hopefully just this simple)

            if (gamepad2.b)
                harvester.setPower(-1.0); // Reverse harvester
            else if (gamepad2.a)
                harvester.setPower(1.0); // Collect
            else
                harvester.setPower(0);

            if (gamepad2.dpad_up)
                flywheels.setPower(flywheelCoefficient*1.0); // Shoot
            else if (gamepad2.dpad_down)
                flywheels.setPower(-0.6); // Reverse flywheels
            else
                flywheels.setPower(0.0);

            if (gamepad2.left_trigger > 0.5 && !pressingFlywheelC) {
                flywheelCoefficient = Range.clip(flywheelCoefficient - 0.1, 0.2, 1.0);
                pressingFlywheelC = true;
            }
            else if (gamepad2.left_bumper && !pressingFlywheelC) {
                flywheelCoefficient = Range.clip(flywheelCoefficient + 0.1, 0.2, 1.0);
                pressingFlywheelC = true;
            }

            if (!(gamepad2.left_trigger > 0.5) && !gamepad2.left_bumper) {
                pressingFlywheelC = false;
            }

            if (gamepad2.right_bumper && capBallMode) {
                lift.setPower(1.0);
            }
            else if (gamepad2.right_trigger > 0.5 && capBallMode) {
                lift.setPower(-0.5);
            }
            else {
                lift.setPower(0.0);
            }

            if (gamepad2.x) {
                capBallMode = true;
            }

            if (gamepad2.y && capBallMode) {
                rightLifterServo.setPosition(RIGHT_SERVO_UNLOCKED);
                leftLifterServo.setPosition(LEFT_SERVO_UNLOCKED);
            }

            leftPusherPower = gamepad2.right_stick_x;

            // clip the right/left values so that the values never exceed +/- 1
            leftPusherPower = speedCoefficient*Range.clip(leftPusherPower, -1, 1);

            if (leftPusherPower > 0.5) {
                leftButtonPusher.setPosition(1.0);
            }
            else if (leftPusherPower < -0.5) {
                leftButtonPusher.setPosition(0.0);
            }
            else {
                leftButtonPusher.setPosition(0.5);
            }

            rightPusherPower = gamepad2.left_stick_x;

            // clip the right/left values so that the values never exceed +/- 1
            rightPusherPower = speedCoefficient*Range.clip(rightPusherPower, -1, 1);

            if (rightPusherPower > 0.5) {
                rightButtonPusher.setPosition(1.0);
            }
            else if (rightPusherPower < -0.5) {
                rightButtonPusher.setPosition(0.0);
            }
            else {
                rightButtonPusher.setPosition(0.5);
            }

            outputConstantLinesToDriverStation(new String[] {
                    "Fly wheel power = " + flywheelCoefficient,
                    "Drive power = " + speedCoefficient,
                    "Cap ball mode = " + capBallMode
            });

            idle();

            /******************** END OF LOOP ********************/
        }
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

    double scaleInput(double dVal)
    {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}