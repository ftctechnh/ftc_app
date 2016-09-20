package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */

//Add the teleop to the op mode register.
@TeleOp(name="Teleop", group="Teleop Group")
//@Disabled

public class Teleop extends RobotBase {

    @Override
    protected void customInitialization() {
    }

    protected void runInstructions() throws InterruptedException
    {

        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop()).
        while (opModeIsActive())
        {
            //Insert code here.
            idle();
        }
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

    double scaleInput(double dVal) {
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