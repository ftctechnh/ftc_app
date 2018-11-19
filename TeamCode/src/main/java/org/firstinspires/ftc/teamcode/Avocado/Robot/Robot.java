package org.firstinspires.ftc.teamcode.Avocado.Robot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This file contains all the methods and hardware for Avocado's robot as of 11/18/18
 */

public class Robot {

/* Hardware ------------------------------------------------------------------------------------ */

    // Hardware controlled by Gamepad 1
    public DcMotor topLeftMotor;
    public DcMotor bottomLeftMotor;
    public DcMotor topRightMotor;
    public DcMotor bottomRightMotor;

    // Hardware controlled by Gamepad 2
    public DcMotor hanger;
    public DcMotor claw;
    public DcMotor tiltMotor;

    HardwareMap hwmap = null;

    public void init(HardwareMap ahwmap) {
        // Save reference to Hardware map
        hwmap = ahwmap;

        // Hardware map

        topLeftMotor = hwmap.dcMotor.get("topLeftMotor");
        bottomLeftMotor = hwmap.dcMotor.get("bottomLeftMotor");
        topRightMotor = hwmap.dcMotor.get("topRightMotor");
        bottomRightMotor = hwmap.dcMotor.get("bottomRightMotor");
        hanger = hwmap.dcMotor.get("hanger");


    }

/* Methods ------------------------------------------------------------------------------------ */

    // posleft is used to correct the direction of the motors on the left side of the robot
    byte posleft = -1;

    public void TankDrive(float leftdrive, float rightdrive) {

        // Passes controller values to the motors
        topLeftMotor.setPower(-leftdrive);
        bottomLeftMotor.setPower(-leftdrive);
        topRightMotor.setPower(rightdrive);
        bottomRightMotor.setPower(rightdrive);

    }

    public void lift_a(float lift) {

        // Pass controller value into the lift motor
        hanger.setPower(-lift);

    }

    public void lift_b(boolean up, boolean down) {

        // If up is pressed move motor in a positive direction. If down is pressed, move it in a negative direction.
        if(up) {

            hanger.setPower(1);

        } else if(down) {

            hanger.setPower(-1);

        }

    }

    public void strafe(boolean left, boolean right, boolean up, boolean down) {
    // Move robot in the direction corresponding to the DPad
        if (left) {

            topLeftMotor.setPower(1 * posleft);
            bottomLeftMotor.setPower(-1 * posleft);
            topRightMotor.setPower(-1);
            bottomRightMotor.setPower(1);

        }
        else if (right){

            topLeftMotor.setPower(-1 * posleft);
            bottomLeftMotor.setPower(1 * posleft);
            topRightMotor.setPower(1);
            bottomRightMotor.setPower(-1);

        }
        else if (up) {

            topLeftMotor.setPower(0.5 * posleft);
            bottomLeftMotor.setPower(0.5 * posleft);
            topRightMotor.setPower(0.5);
            bottomRightMotor.setPower(0.5);


        }
        else if (down) {

            topLeftMotor.setPower(-0.5 * posleft);
            bottomLeftMotor.setPower(-0.5 * posleft);
            topRightMotor.setPower(-0.5);
            bottomRightMotor.setPower(-0.5);

        }

    }



/* Constants ---------------------------------------------------------------------------------- */


}