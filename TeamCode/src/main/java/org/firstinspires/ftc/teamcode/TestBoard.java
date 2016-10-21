package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
/**
 * Created by FTC Team 4799-4800 on 9/27/2016.
 */
@TeleOp(name = "Test Board", group="")
public class TestBoard extends LinearOpMode {
    ModernRoboticsI2cRangeSensor rangeSensor;
    ModernRoboticsI2cColorSensor colorSensor;
    DcMotor M1;
    Servo S1;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor motorBackLeft;



    @Override
    public void runOpMode() throws InterruptedException {
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "RF");
        colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "CS");

        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        //M1 = hardwareMap.dcMotor.get("M1");
        S1 = hardwareMap.servo.get("S1");
        telemetry.log().setCapacity(6);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a)
            //M1.setPower(1);
            if (gamepad1.b)
            S1.setPosition(.5);
            else
            S1.setPosition(-.5);


            float leftthrottle = -gamepad1.left_stick_y;
            float rightthrottle = -gamepad1.right_stick_y;

            motorBackLeft.setPower(leftthrottle);
            motorFrontLeft.setPower(leftthrottle);
            motorBackRight.setPower(rightthrottle);
            motorFrontRight.setPower(rightthrottle);

            telemetry.update();
            telemetry.addData("Optical Range ", rangeSensor.cmOptical());
            telemetry.addData("Ultrasonic Range ", rangeSensor.cmUltrasonic());
            telemetry.addData("Red Value ", colorSensor.red());
            telemetry.addData("Green Value ", colorSensor.green());
            telemetry.addData("Blue Value ", colorSensor.blue());
            telemetry.addData("Alpha Value ", colorSensor.alpha());
        }

    }
}
