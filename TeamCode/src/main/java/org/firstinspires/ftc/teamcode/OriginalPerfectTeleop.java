package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@TeleOp(name = "♪ ♥ Original Teleop ♥  ♪", group = "Our Teleop")
//@Disabled
public class OriginalPerfectTeleop extends LinearOpMode {

    /* This says use MasterHardwareClass */
    MasterHardwareClass robot = new MasterHardwareClass();

    /*These values are used for the drive*/
    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        // Wait for the start button
        telemetry.addLine("!☻ Ready to Run ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Display the current value
            telemetry.addLine("Hi~♪");
            telemetry.addData("Claw Opening Controls", "X is Close, B is Open");
            telemetry.addData("Claw Moving Controls", "Use the D-Pad ↑ & ↓ buttons!");

            telemetry.addData("Front Left Power", robot.frontLeftMotor.getPower());
            telemetry.addData("Front Right Power", robot.frontRightMotor.getPower());
            telemetry.addData("Back Left Power", robot.backLeftMotor.getPower());
            telemetry.addData("Back Right Power", robot.backRightMotor.getPower());
            telemetry.update();

        /* Set the arm up */
//            if (robot.gemServo.getPosition() != robot.xPosUp) {
//                robot.gemServo.setPosition(robot.xPosUp);
//           \ }
            robot.gemServo.setPosition(robot.xPosUp);

        /* Servo Control */
            if (gamepad2.x) {
                if (robot.ClawPower != robot.clawClose) {
                    robot.clawMotor.setPower(robot.clawClose);
                    robot.ClawPower = robot.clawClose;
                }
            }

            if (gamepad2.b) {
                if (robot.ClawPower != robot.clawOpen) {
                    robot.clawMotor.setPower(robot.clawOpen);
                    robot.ClawPower = robot.clawOpen;
                }
            }

            if (!gamepad2.b && !gamepad2.x) {
                if(robot.ClawPower != robot.clawStill){
                    robot.clawMotor.setPower(robot.clawStill);
                    robot.ClawPower = robot.clawStill;
                }
            }

        /* Vertical Arm Motor */
            if (gamepad2.dpad_up) {
                if (robot.VerticalArmPower != 1) {
                    robot.verticalArmMotor.setPower(1);
                    robot.VerticalArmPower = 1;
                }
            }
            else {
                if (robot.VerticalArmPower != 0) {
                    robot.verticalArmMotor.setPower(0);
                    robot.VerticalArmPower = 0;
                }

                if (gamepad2.dpad_down) {
                    if (robot.VerticalArmPower != -1) {
                        robot.verticalArmMotor.setPower(-1);
                        robot.VerticalArmPower = -1;
                    }
                } else {
                    if (robot.VerticalArmPower != 0) {
                        robot.verticalArmMotor.setPower(0);
                        robot.VerticalArmPower = 0;
                    }
                }
            }
        /* Rotational Drive Control */
            if (gamepad1.left_bumper && gamepad1.right_stick_x < 0 || gamepad1.left_bumper && gamepad1.right_stick_x > 0) {

                double GRX = gamepad1.right_stick_x / robot.bumperSlowest;

                final double v1 = +GRX;
                final double v2 = -GRX;
                final double v3 = +GRX;
                final double v4 = -GRX;

                frontLeft = -v1;
                frontRight = v2;
                backLeft = -v3;
                backRight = v4;

                setWheelPower(frontLeft, frontRight, backLeft, backRight);
            } else {

                double GRX = gamepad1.right_stick_x / robot.nobumper;

                final double v1 = +GRX;
                final double v2 = -GRX;
                final double v3 = +GRX;
                final double v4 = -GRX;

                frontLeft = -v1;
                frontRight = v2;
                backLeft = -v3;
                backRight = v4;

                setWheelPower(frontLeft, frontRight, backLeft, backRight);
            }

        /* Drive Control */
            if (gamepad1.left_bumper) {
                double GLY = -gamepad1.left_stick_y / robot.bumperSlowest;
                double GRX = gamepad1.right_stick_x / robot.bumperSlowest;
                double GLX = gamepad1.left_stick_x  / robot.bumperSlowest;

                final double v1 = GLY + GRX + GLX;
                final double v2 = GLY - GRX - GLX;
                final double v3 = GLY + GRX - GLX;
                final double v4 = GLY - GRX + GLX;

                frontLeft = -v1;
                frontRight = v2;
                backLeft = -v3;
                backRight = v4;

                setWheelPower(frontLeft, frontRight, backLeft, backRight);
            } else {
                double GLY = -gamepad1.left_stick_y / robot.nobumper;
                double GRX = gamepad1.right_stick_x / robot.nobumper;
                double GLX =  gamepad1.left_stick_x / robot.nobumper;

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
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(fl);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(fr);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(bl);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight)
            robot.backRightMotor.setPower(br);
        robot.BackRightPower = backRight;
    }
}