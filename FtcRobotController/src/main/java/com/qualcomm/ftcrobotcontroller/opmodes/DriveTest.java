package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.Range;
//import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class DriveTest extends LinearOpMode {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    double wheelCircumference = 6 * Math.PI;
    double ticksPerRotation = 280;
    double leftSpeed;
    double rightSpeed;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.dcMotor.get("leftMotor1");
        backLeft = hardwareMap.dcMotor.get("leftMotor2");
        frontRight = hardwareMap.dcMotor.get("rightMotor1");
        backRight = hardwareMap.dcMotor.get("rightMotor2");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);


        waitForStart();

        while(opModeIsActive()){


            while(frontLeft.getCurrentPosition() < 210)
            {
                frontLeft.setPower(.5);
                frontRight.setPower(.5);
                backLeft.setPower(.5);
                backRight.setPower(.5);
                telemetry.addData("Text", "*** Robot Data***");
                telemetry.addData(" Left Position", frontLeft.getCurrentPosition());
                telemetry.addData("Right Position", frontRight.getCurrentPosition());
                waitForNextHardwareCycle();
            }

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            sleep(10000000);

            /*
            if(Math.abs(gamepad1.left_stick_y) > 0.2)
                leftSpeed = gamepad1.left_stick_y;
            else
                leftSpeed = 0;

            if(Math.abs(gamepad1.right_stick_y) > 0.2)
                rightSpeed = gamepad1.right_stick_y;
            else
                rightSpeed = 0;


            frontLeft.setPower(leftSpeed);
            backLeft.setPower(leftSpeed);

            frontRight.setPower(rightSpeed);
            backRight.setPower(rightSpeed);

            telemetry.addData("Text", "Drive Test");
            telemetry.addData("    Left Speed", leftSpeed);
            telemetry.addData("   Right Speed", rightSpeed);
            telemetry.addData(" Left Position", frontLeft.getCurrentPosition());
            telemetry.addData("Right Position", frontLeft.getCurrentPosition());
            */
        }

    }
}
