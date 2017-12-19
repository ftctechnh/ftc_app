package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.RelicRecoveryRobot;

/**
 *
 */

public class JewelKnocker implements IMechanism {
    private Servo arm;

    private ColorSensor jewelColorSensor;


    public JewelKnocker(Robot robot) {
        HardwareMap hwMap = robot.getCurrentOpMode().hardwareMap;

        this.arm = hwMap.servo.get("jks");

        this.jewelColorSensor = hwMap.colorSensor.get("jcs");
        jewelColorSensor.enableLed(true);
    }

    public void retractArm() {
        arm.setPosition(0.35);
    }

    public void extendArm() {
        arm.setPosition(1.0);
    }

    public int getRed(){
        return jewelColorSensor.red();
    }

    public int getBlue(){
        return jewelColorSensor.blue();
    }

    public boolean isJewelRed(RelicRecoveryRobot robot){
        int blueLevel = getBlue();
        int redLevel = getRed();
        boolean isRed = false;

        if(redLevel > blueLevel){
            isRed= true;
        } else {
            isRed= false;
        }

        return isRed;
    }

    public boolean isJewelBlue(RelicRecoveryRobot robot){
        int blueLevel = getBlue();
        int redLevel = getRed();
        boolean isBlue = false;

        if(blueLevel > redLevel){
            isBlue= true;
        } else {
            isBlue= false;
        }

        return isBlue;
    }
}
