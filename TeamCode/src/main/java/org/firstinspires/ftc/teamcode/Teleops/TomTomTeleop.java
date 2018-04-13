package org.firstinspires.ftc.teamcode.Teleops;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * ☺ Hi! Esto es el codigo para el 24 Febrero! ☺
 */
@TeleOp(name = "Tom Tom Teleop 1 Player", group = "bacon")
//@Disabled
public class TomTomTeleop extends LinearOpMode {
    /* All hardware & variables */

    /* Declare all objects */
    DcMotor frontLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor backRightMotor = null;
    Servo octoServo;

    /* Give place holder values for the motors*/
    double FrontLeftPower = 0;
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;

    /*These values are used for collection and tray control*/
    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    /* Define values for teleop bumper control */
    static double nobumper = 1.5;
    static double bumperSlowest = 3.2;
    static double bumperFastest = 1.0;

    /* Loop counter */
    double loopCount;

    /* Servo position */
    double servoPos = .5;

    @Override
    public void runOpMode() {
    /* Initialization */
        /* Define and Initialize Hardware */
        frontLeftMotor = hardwareMap.dcMotor.get("FL");
        frontRightMotor = hardwareMap.dcMotor.get("FR");
        backLeftMotor = hardwareMap.dcMotor.get("BL");
        backRightMotor = hardwareMap.dcMotor.get("BR");
        octoServo = hardwareMap.servo.get("octoServo");

        /* Wait for the start button */
        telemetry.addLine("!☺ Ready to Run ☺!");
        telemetry.update();
        waitForStart();

    /* While OpMode is Active Loop */

        while (opModeIsActive()) {

            /* Talk to the drivers & coach */
            telemetry.addLine("Hi! I'm OctoBot!");
            telemetry.update();

        /* Use bumpers to rotate octopus head */
            if (gamepad1.right_bumper) {
                if (0 == loopCount % 700) {
                    servoPos = servoPos + .1;
                    if (servoPos > 1) {
                        servoPos = 1;
                    }
                    
                    octoServo.setPosition(servoPos);
                }
                loopCount++;
            }
            if (gamepad1.left_bumper) {
                if (0 == loopCount % 700) {
                    servoPos = servoPos - .1;
                    if (servoPos < 0) {
                        servoPos = 0;
                    }
                    octoServo.setPosition(servoPos);
                }
                loopCount++;
            }

            double GLY = -gamepad1.left_stick_y / bumperFastest;
            double GRX = gamepad1.right_stick_x / bumperFastest;
            double GLX = gamepad1.left_stick_x / bumperFastest;

            final double v1 = GLY + GRX + GLX;
            final double v2 = GLY - GRX - GLX;
            final double v3 = GLY + GRX - GLX;
            final double v4 = GLY - GRX + GLX;

            frontLeft = -v1;
            frontRight = v2;
            backLeft = -v3;
            backRight = v4;

            setWheelPower(frontLeft, frontRight, backLeft, backRight);

        }

    }

    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

    /* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft = fl;
        frontRight = fr;
        backLeft = bl;
        backRight = br;

        /* set each wheel to the power indicated whenever this method is called */
        if (FrontLeftPower != frontLeft) {
            frontLeftMotor.setPower(fl);
            FrontLeftPower = frontLeft;
        }
        if (FrontRightPower != frontRight) {
            frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if (BackLeftPower != backLeft) {
            backLeftMotor.setPower(bl);
            BackLeftPower = backLeft;
        }
        if (BackRightPower != backRight)
            backRightMotor.setPower(br);
        BackRightPower = backRight;
    }
}

