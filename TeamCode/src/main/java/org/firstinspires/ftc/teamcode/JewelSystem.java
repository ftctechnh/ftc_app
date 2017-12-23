package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class JewelSystem {
    //public OpMode members
    public Servo    jewelArm    = null;
    public Servo    jewelWrist  = null;
    ColorSensor colorSensor;

    public static final double TOP_SERVO = 1;
    public static final double BOTTOM_SERVO = .5;
    public static final double MIDDLE_ARM = .75;
    public static final double CENTER_SERVO = .5;
    public static final double FLICK_RIGHT = .1;
    public static final double FLICK_LEFT = 1;
    public static final double INIT_WRIST = .77;

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
        jewelWrist  = hwMap.get(Servo.class,"jewel_flick");
        jewelArm  = hwMap.get(Servo.class,"jewel_arm");
        colorSensor = hwMap.get(ColorSensor.class, "color_sensor");
        jewelArm.setPosition(TOP_SERVO);
        jewelWrist.setPosition(INIT_WRIST);
        //colorSensor.enableLed(false);
    }
    public void armPos(ArmServoPosition position){
        if (position == ArmServoPosition.TOP) {
            jewelArm.setPosition(TOP_SERVO);
        }else if (position == ArmServoPosition.BOTTOM) {
            jewelArm.setPosition(BOTTOM_SERVO);
        }
    }
    public void wristPos(WristServoPosition direction){
        if (direction == WristServoPosition.RIGHT) {
            jewelWrist.setPosition(FLICK_RIGHT);
        }else if (direction == WristServoPosition.LEFT) {
            jewelWrist.setPosition(FLICK_LEFT);
        }else if (direction == WristServoPosition.CENTER) {
            jewelWrist.setPosition(CENTER_SERVO);
        }else if (direction == WristServoPosition.MID) {
            jewelWrist.setPosition(MIDDLE_ARM);
        }
    }
    public void colorLED(boolean onOff){
        if (onOff == true){colorSensor.enableLed(true);
        }else if (onOff == false){colorSensor.enableLed(false);}
    }
    public JewelColor colorSens() {
        colorSensor.enableLed(true);
        red   = colorSensor.red();
        green = colorSensor.green();
        blue  = colorSensor.blue();
        if (colorSensor.red() > colorSensor.blue()) {
            colorSensor.enableLed(false);
            return JewelColor.RED;
        }else if (colorSensor.blue() > colorSensor.red()) {
            colorSensor.enableLed(false);
            return JewelColor.BLUE;
        }else {
            colorSensor.enableLed(false);
            return JewelColor.UNKNOWN;
        }
    }
    public double getWristPosition() {return jewelWrist.getPosition();}
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
