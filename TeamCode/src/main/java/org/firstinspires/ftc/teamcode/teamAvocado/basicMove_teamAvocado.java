package org.firstinspires.ftc.teamcode.teamAvocado;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rohan Bosworth and Pahel Srivastava on 7/16/17.
 */
@Autonomous(name = "basicMove_teamAvocado")
public class basicMove_teamAvocado extends LinearOpMode{

    public double COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    public double DRIVE_GEAR_REDUCTION;     // 56/24
    public double WHEEL_PERIMETER_CM;     // For figuring circumference
    public double COUNTS_PER_CM;

    DcMotor rightMotor;
    DcMotor leftMotor;

    public void runOpMode(){

        COUNTS_PER_MOTOR_REV = 1440;
        DRIVE_GEAR_REDUCTION = 1.5;
        WHEEL_PERIMETER_CM = 9.1* Math.PI;
        COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV) /
                (WHEEL_PERIMETER_CM * DRIVE_GEAR_REDUCTION);


        leftMotor= hardwareMap.dcMotor.get("leftMotor");
        rightMotor=  hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        /* Move FWD 100cm*/
        encoderDrive(0.5, 100, 100);

        /*Code to turn Right*/
        turn_right(0.2);

        /* Move FWD 100cm*/
        encoderDrive(0.5, 100, 100);

        /*Code to turn Right*/
        turn_right(0.2);

        /* Move FWD 100cm*/
        encoderDrive(0.5, 100, 100);

        /*Code to turn Right*/
        turn_right(0.2);

        /* Move FWD 100cm*/
        encoderDrive(0.5, 100, 100);

        /*Just turn Right*/
        turn_right(0.2);

    }

    public void encoderDrive(double speed,
                             double leftCM, double rightCM) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        //  if (opModeIsActive())

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        newLeftTarget = leftMotor.getCurrentPosition() + (int) (leftCM * COUNTS_PER_CM);
        newRightTarget = rightMotor.getCurrentPosition() + (int) (rightCM * COUNTS_PER_CM);

        leftMotor.setTargetPosition(newLeftTarget);
        rightMotor.setTargetPosition(newRightTarget);

        // reset the timeout time and start motion.
        if (Math.abs(leftCM) > Math.abs(rightCM)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightCM) / leftCM;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftCM) / rightCM;
        }
        //  runtime.reset();
        //if(leftInches != -rightInches)
        leftMotor.setPower(Math.abs(leftSpeed));
        rightMotor.setPower(Math.abs(rightSpeed));



        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                ((leftMotor.isBusy() && rightMotor.isBusy()))) {
        }

        // Stop all motion;
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }
    /* Need to test if it really turns the motor right */
    public void turn_right(double motor_power)
    {
        leftMotor.setPower(motor_power);
        rightMotor.setPower(motor_power * (-1));
    }
}



