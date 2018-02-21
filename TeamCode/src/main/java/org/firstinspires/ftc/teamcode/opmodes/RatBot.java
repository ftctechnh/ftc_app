/*
    Basic rat bot program for testing
 */

package org.firstinspires.ftc.teamcode.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

import java.util.LinkedList;

@TeleOp(name = "RatBot", group = "Misc.")
@Disabled
public class RatBot extends OpMode {

    private static final int DATA_MAX = 100;

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
        touch.setMode(DigitalChannel.Mode.INPUT);

        side = hardwareMap.get(UltrasonicSensor.class, "ul_side");
        front = hardwareMap.get(UltrasonicSensor.class, "ul_front");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

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

        telemetry.addData("side status", side.status());
        telemetry.addData("front status", front.status());
        telemetry.addData("side", side.getUltrasonicLevel());
        telemetry.addData("front", front.getUltrasonicLevel());
    }
}
