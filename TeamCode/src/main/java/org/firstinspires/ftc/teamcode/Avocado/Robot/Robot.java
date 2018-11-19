package org.firstinspires.ftc.teamcode.Avocado.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Avocado.Autonomous.Autonomous_Avocado;
import org.firstinspires.ftc.teamcode.Avocado.Robot.Constants;


/**
 * This file contains all the methods and hardware for Avocado's robot as of 11/19/18
 */

public class Robot extends Autonomous_Avocado {

    Constants constants;
    private ElapsedTime runtime = new ElapsedTime();
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

    /* TeleOp Methods ----------------------------------------------------------------------------- */

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
        if (up) {

            hanger.setPower(1);

        } else if (down) {

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

        } else if (right) {

            topLeftMotor.setPower(-1 * posleft);
            bottomLeftMotor.setPower(1 * posleft);
            topRightMotor.setPower(1);
            bottomRightMotor.setPower(-1);

        } else if (up) {

            topLeftMotor.setPower(0.5 * posleft);
            bottomLeftMotor.setPower(0.5 * posleft);
            topRightMotor.setPower(0.5);
            bottomRightMotor.setPower(0.5);


        } else if (down) {

            topLeftMotor.setPower(-0.5 * posleft);
            bottomLeftMotor.setPower(-0.5 * posleft);
            topRightMotor.setPower(-0.5);
            bottomRightMotor.setPower(-0.5);

        }

    }

    /* Autonomous Methods ----------------------------------------------------------------------------- */
    public void encoderDrive(double speed,
                             double leftCM, double rightCM,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = topLeftMotor.getCurrentPosition() + (int) (leftCM * constants.TICKS_PER_CM);
            newLeftTarget = bottomLeftMotor.getCurrentPosition() + (int) (leftCM * constants.TICKS_PER_CM);
            newRightTarget = topRightMotor.getCurrentPosition() + (int) (rightCM * constants.TICKS_PER_CM);
            newRightTarget = bottomRightMotor.getCurrentPosition() + (int) (rightCM * constants.TICKS_PER_CM);

            topLeftMotor.setTargetPosition(newLeftTarget);
            bottomLeftMotor.setTargetPosition(newLeftTarget);
            topRightMotor.setTargetPosition(newRightTarget);
            bottomRightMotor.setTargetPosition(newRightTarget);


            // Turn On RUN_TO_POSITION
            topLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bottomLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            topRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bottomRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            topLeftMotor.setPower(Math.abs(speed));
            topRightMotor.setPower(Math.abs(speed));
            bottomLeftMotor.setPower(Math.abs(speed));
            topRightMotor.setPower(Math.abs(speed));

            //If you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while ((runtime.seconds() < timeoutS) && (topLeftMotor.isBusy() || bottomLeftMotor.isBusy() || topRightMotor.isBusy() || bottomRightMotor.isBusy())) {

                // Telemetry
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        topLeftMotor.getCurrentPosition(),
                        topRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop the motors
            topLeftMotor.setPower(0);
            bottomLeftMotor.setPower(0);
            topRightMotor.setPower(0);
            bottomRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            topLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bottomLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            topRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bottomRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
}



