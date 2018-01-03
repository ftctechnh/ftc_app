package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Kaden on 11/28/2017.
 */

public class JewelArm {
    public Servo servo;
    ColorSensor cs;
    private final double DOWN_POSITION = 0;
    private final double UP_POSITION = 1;
    private Telemetry telemetry;

    public JewelArm(Servo servo, ColorSensor cs, Telemetry telemetry) {
        this.servo = servo;
        this.cs = cs;
        this.telemetry = telemetry;
    }

    public void down() {
        setPostion(DOWN_POSITION);
    }

    public void up() {
        setPostion(UP_POSITION);
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
            } else if (cs.blue() < cs.red()) {
                red += 1;
            }
        }
        if (red > blue) {
            addLine("RED");
            return "Red";
        } else if (blue > red) {
            addLine("BLUE");
            return "Blue";
        } else {
            addLine("NOTHING");
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
    private void addLine(String message) {
        telemetry.addLine(message);
        telemetry.update();
    }
}
