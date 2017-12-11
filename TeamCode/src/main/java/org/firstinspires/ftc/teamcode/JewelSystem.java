package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Joseph Liang on 11/25/2017.
 */

public class JewelSystem {
    //public OpMode members
    public Servo    jewelArm    = null;
    ColorSensor colorSensor;

    public static final double TOP_SERVO = 1;
    public static final double BOTTOM_SERVO = .444;

    int red;
    int green;
    int blue;

    //local OpMode members
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    //Constructor
    public JewelSystem(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and initialize ALL installed servos.
        jewelArm  = hwMap.get(Servo.class,"jewel_arm");
        colorSensor = hwMap.get(ColorSensor.class, "color_sensor");
        jewelArm.setPosition(TOP_SERVO);

        //colorSensor.enableLed(false);
    }
    public void armPos(double pos){
        if (pos < BOTTOM_SERVO)
            pos = (BOTTOM_SERVO);
        jewelArm.setPosition(pos);
    }
    public void colorLED(boolean onOff){
        if (onOff == true){colorSensor.enableLed(true);
        }else if (onOff == false){colorSensor.enableLed(false);}
    }
    public String colorSens() {
        colorSensor.enableLed(true);
        red   = colorSensor.red();
        green = colorSensor.green();
        blue  = colorSensor.blue();
        if (colorSensor.red() > colorSensor.blue()) {
            colorSensor.enableLed(false);
            return "red";
        }else if (colorSensor.blue() > colorSensor.red()) {
            colorSensor.enableLed(false);
            return "blue";
        }else {
            colorSensor.enableLed(false);
            return "unknown";
        }
    }
    public double getArmPosition(){
        return jewelArm.getPosition();
    }
    public int getRedVal(){
        return red;
    }
    public int getGreenVal(){
        return green;
    }
    public int getBlueVal(){
        return blue;
    }
}
