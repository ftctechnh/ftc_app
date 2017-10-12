/*
    Basic rat bot program for testing
 */

package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "RatBot", group = "Misc.")
//@Disabled
public class RatBot extends OpMode {

    DcMotorEx frontLeftMotor;
    DcMotorEx backLeftMotor;
    DcMotorEx frontRightMotor;
    DcMotorEx backRightMotor;

    ColorSensor mahColor;
    DistanceSensor mahDistance;
    DigitalChannel touch;

    UltrasonicSensor side;
    UltrasonicSensor front;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        frontLeftMotor = (DcMotorEx) hardwareMap.dcMotor.get("front_left");
        frontRightMotor = (DcMotorEx) hardwareMap.dcMotor.get("front_right");
        backLeftMotor = (DcMotorEx) hardwareMap.dcMotor.get("back_left");
        backRightMotor = (DcMotorEx) hardwareMap.dcMotor.get("back_right");

        mahColor = hardwareMap.get(ColorSensor.class, "color");
        mahDistance = hardwareMap.get(DistanceSensor.class, "color");
        touch = hardwareMap.get(DigitalChannel.class, "touch");

        side = hardwareMap.get(UltrasonicSensor.class, "ul_side");
        front = hardwareMap.get(UltrasonicSensor.class, "ul_front");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {
        // run the drive train motors
        frontLeftMotor.setPower(gamepad1.left_stick_y);
        frontRightMotor.setPower(gamepad1.right_stick_y);
        backLeftMotor.setPower(gamepad1.left_stick_y);
        backRightMotor.setPower(gamepad1.right_stick_y);

        telemetry.addData("color", mahColor.red());
        telemetry.addData("color distance", mahDistance.getDistance(DistanceUnit.MM));

        telemetry.addData("touch", touch.getState());

        telemetry.addData("side", side.getUltrasonicLevel());
        telemetry.addData("front", front.getUltrasonicLevel());
    }
}
