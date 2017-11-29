package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Kaden on 11/28/2017.
 */

public class JewelArm {
    private Servo servo;
    private ColorSensor cs;
    private double servoDownPos = 0.39;
    private double servoUpPos = 0.9;
    private Telemetry telemetry;

    public JewelArm(Servo servo, ColorSensor cs, Telemetry telemetry) {
        this.servo = servo;
        this.cs = cs;
        this.telemetry = telemetry;
    }

    public void down() {
        servo.setPosition(servoDownPos);
        servo.setPosition(servoDownPos);
    }

    public void up() {
        servo.setPosition(servoUpPos);
        servo.setPosition(servoUpPos);
    }

    public String findJewel() {
        while (cs.red() < 2 && cs.blue() < 2) {

        }
        int blue = 0;
        int red = 0;
        for (int i = 0; i <= 5; i++) {
            if (cs.blue() > cs.red()) {
                blue += 1;
                telemetry.addData("saw blue", 1);
                telemetry.update();
            } else if (cs.blue() < cs.red()) {
                red += 1;
                telemetry.addData("saw red", 1);
                telemetry.update();
            }
        }
        if (red > blue) {
            return "Red";
        } else if (blue > red) {
            return "Blue";
        } else {
            return "nothing";
        }
    }

    public void init() {
    }

    public void loop() {
    }
}
