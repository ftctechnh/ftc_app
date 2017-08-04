package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by connorespenshade on 7/12/17.
 */

//Line 11 allows Op Mode to appear on Android device
@TeleOp(name = "Two Motor Drive Train")
public class TwoMotorDrivetrainTeleOp extends LinearOpMode {


    //Create motor variables
    private DcMotor motorLeft;
    private DcMotor motorRight;

    private Servo servoMotor;
    private ColorSensor sensor;

    private Double servoMaxPos = 0.8;
    private Double servoMinPos = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        //Do stuff

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        //Do stuff when started

        while(opModeIsActive()) {

            motorLeft.setPower(-gamepad1.left_stick_y);

            motorRight.setPower(-gamepad1.right_stick_y);

            telemetry.addData("Left Motor Power", motorLeft.getPower());
            telemetry.addData("Right Motor Power", motorRight.getPower());




            idle(); //Allows computer to rest for a moment

        }

    }



}