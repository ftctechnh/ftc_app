package org.firstinspires.ftc.teamcode.tankbotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//Add the teleop to the op mode register.
@TeleOp(name="TankBot Teleop", group="Teleop Group")

public class Teleop extends _RobotBase
{
    //All teleop controls are here.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Normal mode variables
        double leftPower, rightPower;
        boolean backwards = false;
        double lastTimeBackTogglePressed = System.currentTimeMillis();

        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop())
        while (opModeIsActive())
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

            // Write the values to the motors.  Scale the robot in order to run the robot more effectively at slower speeds.
            leftMotor.setPower(scaleInput(leftPower));
            rightMotor.setPower(scaleInput(rightPower));

            /************** Data Output **************/
            outputConstantDataToDrivers(new String[] {
                    "Right power = " + rightPower,
                    "Left power = " + leftPower
            });

            //Toggle direction
            if (gamepad1.a && (System.currentTimeMillis() - lastTimeBackTogglePressed) > 1000)
            {
                backwards = !backwards;
                lastTimeBackTogglePressed = System.currentTimeMillis();
            }

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