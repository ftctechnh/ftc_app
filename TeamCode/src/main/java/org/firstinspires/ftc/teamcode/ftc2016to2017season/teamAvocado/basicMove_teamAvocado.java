package org.firstinspires.ftc.teamcode.ftc2016to2017season.teamAvocado;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Rohan Bosworth on 7/16/17.
 */
@Autonomous(name = "basicMove_teamAvocado")
@Disabled
public class basicMove_teamAvocado extends LinearOpMode{


    double encoder_ticks_per_rotation;
    double gear_ratio;
    double wheel_circumference;
    double encoder_ticks_per_cm;

    DcMotor rightMotor;
    DcMotor leftMotor;

    public void runOpMode() {




        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
        encoder_ticks_per_rotation = 1440;
        gear_ratio = 1.5;
        wheel_circumference = 9.1 * Math.PI;
        encoder_ticks_per_cm = (encoder_ticks_per_rotation) / (wheel_circumference * gear_ratio);

    /* Move FWD 100cm*/
        encoderDrive(0.5, 200, 200);


//    /*Code to turn Right*/
//    turn_right(0.2);
//
//    /* Move FWD 100cm*/
//    encoderDrive(0.5,100,100);
//
//    /*Code to turn Right*/
//    turn_right(0.2);
//
//    /* Move FWD 100cm*/
//    encoderDrive(0.5,100,100);
//
//    /*Code to turn Right*/
//    turn_right(0.2);
//
//    /* Move FWD 100cm*/
//    encoderDrive(0.5,100,100);
//
//    /*Just turn Right*/
//    turn_right(0.2);
    }

    public void encoderDrive(double speed, double leftCM, double rightCM) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        //  if (opModeIsActive())

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Determine new target position, and pass to motor controller
        newLeftTarget = leftMotor.getCurrentPosition() + (int) (leftCM * encoder_ticks_per_cm);
        newRightTarget = rightMotor.getCurrentPosition() + (int) (rightCM * encoder_ticks_per_cm);

        leftMotor.setTargetPosition(newLeftTarget);
        rightMotor.setTargetPosition(newRightTarget);

        // reset the timeout time and start motion.
        if (Math.abs(leftCM) > Math.abs(rightCM)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightCM) / leftCM;
        } else if(Math.abs(rightCM) > Math.abs(leftCM)) {
            rightSpeed = speed;
            leftSpeed = (speed * leftCM) / rightCM;
        }
        else{
            leftSpeed = speed;
            rightSpeed = speed;
        }
        leftMotor.setPower(Math.abs(leftSpeed));
        rightMotor.setPower(Math.abs(rightSpeed));
        while (opModeIsActive() && ((leftMotor.isBusy() && rightMotor.isBusy()))) {
        }
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }
}