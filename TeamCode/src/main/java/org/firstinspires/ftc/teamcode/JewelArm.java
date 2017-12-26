package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Kaden on 11/28/2017.
 */

public class JewelArm {
    public Servo servo;
    private ColorSensor cs;
    private double servoDownPos = 0;
    private double servoUpPos = 1;
    private Telemetry telemetry;

    public JewelArm(Servo servo, ColorSensor cs, Telemetry telemetry) {
        this.servo = servo;
        this.cs = cs;
        this.telemetry = telemetry;
    }

    public void down() {
        setPostion(servoDownPos);
    }

    public void up() {
        telemetrize("going up");
        setPostion(servoUpPos);
        telemetrize("went up");
    }

    public String findJewel() {
        down();
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
        up();
        cs.enableLed(true);
    }

    public void loop() {
    }
    public void setPostion(double postion) {
        servo.setPosition(postion);
        servo.setPosition(postion);
    }
    public void telemetrize(String message) {
        telemetry.addData(message, null);
        telemetry.update();
    }
}
