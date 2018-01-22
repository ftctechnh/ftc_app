package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team11248.Robot11248;

@TeleOp(name = "SensorValTest")
public class SensorValTest extends OpMode {

    public Robot11248 robot;



    public void init() {

        robot = new Robot11248(hardwareMap, telemetry);
        robot.init(); //Sets servos to right position.
        robot.vuforia.init(true,true);
        robot.activateColorSensors();

    }

    public void start(){
        robot.calibrateGyro();
    }

    // Respond to gamepad input.
    public void loop() {
        telemetry.addData("01: ", "isJewelBlue: " + robot.jewelColor.isBlue());
        telemetry.addData("02: ", "isJewelRed: " + robot.jewelColor.isRed());
        telemetry.addData("03: ", "isFrontFloorBlue: " + robot.frontFloorColor.isBlue());
        telemetry.addData("04: ", "isFrontFloorRed: " + robot.frontFloorColor.isRed());
        telemetry.addData("05: ", "isBackFloorBlue: " + robot.backFloorColor.isBlue());
        telemetry.addData("06: ", "isBackFloorRed: " + robot.backFloorColor.isRed());
        telemetry.addData("07: ", "Heading: " + robot.getGyroAngle());
        telemetry.addData("08: ", "Ultrasonic CM: " + robot.rangeSensor.ultrasonicValue());
        telemetry.addData("09: ", "VuMark Column " + robot.vuforia.getLastImage().toString());



    }

}