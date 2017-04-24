/**
 * This code is used during the robot's teleop period.
 */

package org.firstinspires.ftc.teamcode.driverchoices;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

//Add the teleop to the op mode register.
@TeleOp(name="Teleop", group="Teleop Group")

public class Teleop extends MainRobotBase
{
    //All teleop controls are here.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Normal mode variables
        double leftPower, rightPower,
                rightPusherPowerLeft, rightPusherPowerRight;
        boolean backwards = false;
        double speedCoefficient = 1.0;
        double flywheelCoefficient = 0.4;
        boolean pressingFlywheelC = false;
        boolean fbpUp = true;
        boolean capBallMode = false, capBallMode2 = false;
        final double flywheelMaxRPS = 35, harvesterMaxRPS = 5;

        NiFTConsole.ProcessConsole teleopConsole = new NiFTConsole.ProcessConsole ("Teleop");

        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop())
        while (true)
        {
            /**************************** CONTROLLER #1 ********************************/
            /************** Direction Toggle **************/
            if (!backwards)
            { // Driving forward
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;
            } else { // Driving backward
                leftPower = gamepad1.right_stick_y;
                rightPower = gamepad1.left_stick_y;
            }

            /************** Motor Speed Control **************/
            rightPower = Range.clip(rightPower, -1, 1);
            leftPower = Range.clip(leftPower, -1, 1);

            // Write the values to the motors.  Scale the robot in order to startEasyTask the robot more effectively at slower speeds.
            leftDrive.setDirectMotorPower (scaleInput(leftPower) * speedCoefficient);
            rightDrive.setDirectMotorPower (scaleInput(rightPower) * speedCoefficient);

            /************** Cap Ball Drive Mode **************/
            if (gamepad1.x) {
                speedCoefficient = 0.7;
                capBallMode = true;
            }
            else if (gamepad1.y) {
                speedCoefficient = 1.0;
                capBallMode = false;
            }

            if (gamepad1.right_bumper) {
                speedCoefficient = 0.7;
                frontButtonPusher.setToLowerLim ();
            }
            else if (!capBallMode) {
                speedCoefficient = 1.0;
                frontButtonPusher.setToUpperLim ();
            }
            else {
                frontButtonPusher.setToUpperLim ();
            }

            /************** Clamp **************/
            if (gamepad1.left_bumper || (gamepad2.left_bumper && capBallMode2))
                capBallHolder.setServoPosition (capBallHolder.getServoPosition () + 0.05);
            else if (gamepad1.left_trigger > 0.5 || (gamepad2.left_trigger > 0.5 && capBallMode2))
                capBallHolder.setServoPosition (capBallHolder.getServoPosition () - 0.01);

            /************** Open Clamp **************/
            if (gamepad1.back || gamepad2.back) {
                capBallHolder.setToUpperLim ();
            }

            /**************************** CONTROLLER #2 ********************************/
            /************** Cap Ball Lift **************/
            if (gamepad2.x) {
                capBallMode2 = true;
            }
            else if (gamepad2.y) {
                capBallMode2 = false;
            }

            if (gamepad2.right_bumper)
                lift.setPower (1.0);
            else if (gamepad2.right_trigger > 0.5)
                lift.setPower (-0.5);
            else
                lift.setPower (0.0);

            /************** Harvester **************/
            if (gamepad2.b) {
                harvester.setRPS (harvesterMaxRPS); // Collect
            }
            else if (gamepad2.a) {
                harvester.setRPS (-harvesterMaxRPS); // Reverse harvester
            }
            else {
                harvester.setRPS (0);
            }

            /************** Flywheels **************/
            if (gamepad2.dpad_up) {
                flywheels.setRPS (flywheelCoefficient * flywheelMaxRPS); // Shoot
            }
            else if (gamepad2.dpad_down) {
                flywheels.setRPS (-flywheelMaxRPS * 2.0/3.0); // Reverse flywheels
            }
            else {
                flywheels.setRPS (0.0);
            }

            //Used to adjust the power of the flywheels.
            if (gamepad2.left_trigger > 0.5 && !pressingFlywheelC && !capBallMode2) {
                flywheelCoefficient = Range.clip(flywheelCoefficient - 0.04, 0.2, 1.0);
                pressingFlywheelC = true;
            }
            else if (gamepad2.left_bumper && !pressingFlywheelC && !capBallMode2) {
                flywheelCoefficient = Range.clip(flywheelCoefficient + 0.04, 0.2, 1.0);
                pressingFlywheelC = true;
            }

            if (!(gamepad2.left_trigger > 0.5) && !gamepad2.left_bumper) {
                pressingFlywheelC = false;
            }

            /************** Side Beacon Pushers **************/
            rightPusherPowerLeft = Range.clip(gamepad2.left_stick_x, -1, 1);
            rightPusherPowerRight = Range.clip(gamepad2.right_stick_x, -1, 1);

            if (rightPusherPowerLeft > 0.5 || rightPusherPowerRight > 0.5)
                rightButtonPusher.setToUpperLim ();
            else if (rightPusherPowerLeft < -0.5 || rightPusherPowerRight < -0.5)
                rightButtonPusher.setToLowerLim ();
            else
                rightButtonPusher.setServoPosition (0.5);

            /************** Data Output **************/
            teleopConsole.updateWith (
                    "Fly wheel power = " + flywheelCoefficient,
                    "Conversion " + flywheels.getRPSConversionFactor (),
                    "Drive power = " + speedCoefficient,
                    "Cap ball mode = " + capBallMode,
                    "FBP_up = " + fbpUp
            );

            NiFTFlow.pauseForSingleFrame ();

            /******************** END OF LOOP ********************/
        }
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

    private double scaleInput(double dVal)
    {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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