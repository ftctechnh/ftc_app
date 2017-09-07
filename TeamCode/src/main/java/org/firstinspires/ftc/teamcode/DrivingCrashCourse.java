package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jason Idris on 8/31/2017.
 */

// (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧  Welcome to Jason's TeleOp!  ✧ﾟ･: *ヽ(◕ヮ◕ヽ)
@TeleOp(name = "Jason's TeleOp")

public class DrivingCrashCourse extends LinearOpMode{

    //Create motor variables
    private DcMotor motorLeft;
    private DcMotor motorRight;

    private DcMotor motorLeftBack;
    private DcMotor motorRightBack;

    private Servo servoMotor;
    private ColorSensor sensor;

    private DcMotor shooter;
    private DcMotor ballPickUpLeft;
    private DcMotor ballPickUpRight;

    private Double servoMaxPos = 0.8;
    private Double servoMinPos = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        //Do stuff

        motorLeft = hardwareMap.dcMotor.get("motorLeftFront");
        motorRight = hardwareMap.dcMotor.get("motorRightFront");

        motorLeftBack = hardwareMap.dcMotor.get("motorLeftBack");
        motorRightBack = hardwareMap.dcMotor.get("motorRightBack");

        ballPickUpLeft = hardwareMap.dcMotor.get("bpum1");
        ballPickUpRight = hardwareMap.dcMotor.get("bpum2");

        shooter = hardwareMap.dcMotor.get("shooter");

        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        //Do stuff when started

        while(opModeIsActive()) {

            motorLeft.setPower(-gamepad1.left_stick_y);
            motorLeftBack.setPower(-gamepad1.left_stick_y);
            motorRight.setPower(-gamepad1.right_stick_y);
            motorRight.setPower(-gamepad1.right_stick_y);

            telemetry.addData("Left Motor Power", motorLeft.getPower());
            telemetry.addData("Right Motor Power", motorRight.getPower());


            if (gamepad2.a) {

                ballPickUpLeft.setPower(1);
                ballPickUpRight.setPower(1);

                //wait(3500);
            }

            if (gamepad2.right_bumper) {

                shooter.setPower(1);
                //wait(2000);

            }

            idle(); //Allows computer to rest for a moment

        }

    }

}
