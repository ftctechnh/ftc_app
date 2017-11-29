package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kaden on 11/28/2017.
 */

public class JewelArm {
    private Servo servo;
    private ColorSensor cs;
    private double servoDownPos = 0.39;
    private double servoUpPos = 0.9;

    public JewelArm(Servo servo, ColorSensor cs) {
        this.servo = servo;
        this.cs = cs;
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
        for (int i = 0; i<=5; i++) {
            if (cs.blue() > cs.red()) {
                blue += 1;
            } else {
                red += 1;
            }
        }
        if (blue > red) {
            return "Red";
        } else {
            return "Blue";
        }
    }
}
