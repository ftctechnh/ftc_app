package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(name = "NEW Jewel Color", group = "Sensor")
public class NewJewel extends LinearOpMode {

    ColorSensor colorSensorL;
    Servo loweringJewelServo;
    Servo turningJewelServo;

    public double loweredPosition = 0.2;
    public double upPosition = 0.7;
    public double leftPosition = .5;
    public double rightPosition = 1.0;
    public double middlePos = .75;

    public boolean redLeft;

    @Override
    public void runOpMode() {
        colorSensorL = hardwareMap.get(ColorSensor.class, "color sensor left");
        loweringJewelServo = hardwareMap.get(Servo.class, "lowering servo" );
        turningJewelServo = hardwareMap.get(Servo.class, "turning servo");

        waitForStart();

        lower();

        sleep(1000);

        if (loweringJewelServo.getPosition() == loweredPosition) {
            redLeft = isLeft();

            telemetry.addLine("working");
            telemetry.update();
        }

        while (opModeIsActive()) {
            if (redLeft) {
                turn(rightPosition);
            } else {
                turn(leftPosition);
            }
            telemetry.update();
        }
    }

    public boolean isLeft() {
        telemetry.addLine("Random");

        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

        if (colorSensorL.red() > colorSensorL.blue()) {
            telemetry.addLine("See Red");

            return true;
        } else {
            telemetry.addLine("See Blue");

            return false;
        }
    }

    public void lower() {
        loweringJewelServo.setPosition(loweredPosition);
    }

    public void turn(double position) {
        turningJewelServo.setPosition(position);
    }

}

/**
 * LOGIC:
 *      Color Function - returns whether it's blue or red
 *      Servo move function - moves servo based on what color is where
 */



