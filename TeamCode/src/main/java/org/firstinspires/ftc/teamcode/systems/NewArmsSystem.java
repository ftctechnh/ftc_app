package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by nycfirst on 1/9/18.
 */

public class NewArmsSystem {
    private Servo jewelArmServo;
    private ColorSensor colorSensor;
    private double initPosition = 0.0; // TODO: find correct values
    private double downPosition = 0.0; // TODO: find correct values

    public NewArmsSystem(HardwareMap hardwareMap) {
        this.jewelArmServo = hardwareMap.get(Servo.class, "jewel arm servo");
        this.colorSensor = hardwareMap.colorSensor.get("color sensor");
    }

    public void setPosition(double position) {
        this.jewelArmServo.setPosition(position);
    }

    public void setInitPosition() {
        this.jewelArmServo.setPosition(initPosition);
    }

    public void setJewelArmServoDownPosition() {
        this.jewelArmServo.setPosition(downPosition);
    }

    public double getJewelServoPosition() {
        return this.jewelArmServo.getPosition();
    }

    public int getRed() {
        return colorSensor.red();
    }

    public int getBlue() {
        return colorSensor.blue();
    }

    public int getGreen() {
        return colorSensor.green();
    }

    public boolean isBlue() {
        return (getBlue() > getRed()) && (getBlue() > getGreen());
    }

    public boolean isRed() {
        return (getRed() > getBlue()) && (getRed() > getGreen());
    }
}
