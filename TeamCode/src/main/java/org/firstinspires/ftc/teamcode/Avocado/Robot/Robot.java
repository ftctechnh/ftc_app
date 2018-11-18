package org.firstinspires.ftc.teamcode.Avocado.Robot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This file contains all the code that is being executed when a method is called in the main TeleOp file.
 * Additionally, this file contains the robot's hardware.
 */

public class Robot {

    // Gamepad 1
    public DcMotor topLeftMotor;
    public DcMotor bottomLeftMotor;
    public DcMotor topRightMotor;
    public DcMotor bottomRightMotor;

    //Gamepad 2
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

    byte posleft = -1;

    public void TankDrive(float leftdrive, float rightdrive) {

        topLeftMotor.setPower(-leftdrive);
        bottomLeftMotor.setPower(-leftdrive);
        topRightMotor.setPower(rightdrive);
        bottomRightMotor.setPower(rightdrive);

    }

    public void lift(float lift) {

        float leftY_gp2 = (-lift);
        float rightY_gp2 = (-lift);

        hanger.setPower(leftY_gp2);

    }

    public void strafe(boolean left, boolean right, boolean up, boolean down) {

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
    public interface Constants {

        int x = 1;
    }

}