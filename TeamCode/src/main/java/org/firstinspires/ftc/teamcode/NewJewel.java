package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "NEW Jewel Color", group = "Sensor")
public class NewJewel extends LinearOpMode {

    ColorSensor colorSensorL;
    Servo loweringJewelServo;
    Servo turningJewelServo;

    public double downPos = 0.2;
    public final double UP_POS = 0.7;

    public final double LEFT_POS = .5;
    public final double RIGHT_POS = 1.0;

    public final double MIDDLE_POS = .75;

    public double increment = .07;

    public placement myPlacement;

    public alliance team;

    NewJewel(alliance myTeam) {
        team = myTeam;
    }

    @Override
    public void runOpMode() {

        colorSensorL = hardwareMap.get(ColorSensor.class, "color sensor left");
        loweringJewelServo = hardwareMap.get(Servo.class, "lowering servo" );
        turningJewelServo = hardwareMap.get(Servo.class, "turning servo");

        waitForStart();

        lower();

        sleep(1000);

        if (loweringJewelServo.getPosition() == downPos) {
            myPlacement = isLeft();
        }

        while (opModeIsActive()) {
            if (team == alliance.RED) {
                red();
            } else {
                blue();
            }
        }
    }

    public placement isLeft() {
        telemetry.addLine("Random");

        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

        while (colorSensorL.red() == 0 && colorSensorL.blue() == 0) {
            downPos += increment;
            lower();

            sleep(100);
        }

        if (colorSensorL.red() > colorSensorL.blue()) {
            telemetry.addLine("See Red");

            return placement.LEFT;
        } else {
            telemetry.addLine("See Blue");

            return placement.RIGHT;
        }
    }

    public void lower() {
        loweringJewelServo.setPosition(downPos);
    }

    public void turn(double position) {
        turningJewelServo.setPosition(position);
    }

    public void red() {
        if (isLeft() == placement.LEFT) {
            turn(RIGHT_POS);
        } else {
            turn(LEFT_POS);
        }
    }

    public void blue() {
        if (isLeft() == placement.LEFT) {
            turn(LEFT_POS);
        } else {
            turn(RIGHT_POS);
        }
    }

    public enum alliance {
        RED, BLUE;
    }

    public enum placement {
        LEFT, RIGHT, NONE;
    }
}

/**
 * LOGIC:
 *      Color Function - returns whether it's blue or red
 *      Servo move function - moves servo based on what color is where
 */



