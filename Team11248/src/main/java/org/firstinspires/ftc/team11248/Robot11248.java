package org.firstinspires.ftc.team11248;

/**
 * Created by tonytesoriero on 9/11/17.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team11248.Hardware.Claw;
import org.firstinspires.ftc.team11248.Hardware.HolonomicDriver_11248;
import org.firstinspires.ftc.team11248.Hardware.MRColorSensorV3;

public class Robot11248 extends HolonomicDriver_11248 {

    public static final String vuforiaKey = "AeTwV0H/////AAAAGfe7ayWmjE9+nI9k65aoO+NQIIujZBIX8AxeoVDf9bwLLNvQ6QwvM+Clc3CE/8Pumv5guDuXMxkERpyJTzSb50PcrH9y/lJC9Zfh0FlPVkkvDnZVNsPEIEsg0Ta5oDlz1jIZmSB/Oxu2qRAyo4jXIsWSmDMdQdpNrwkyKbLfl/CT7PWe23RAdF8oQf5XqnSbKoapQali8MH4+HPOR8r13/k+cZv9eKqUvknmxZPiyJbp4oFzqrWDJSUqwTGQLEdbp76Hjrkuxu3Pa/I4jQSt3RRRbAUrZeV1Z79cLKg+22SvrhUKKzwxeEMcgp4rQzrMXhTL+wE+6sBczuguHmPtWA5w/NsUlevRaLbEionbyXYN";

    //Color sensor color thresholds
    private final int BLUE_LOW_THRESHOLD = 1;
    private final int BLUE_HIGH_THRESHOLD = 4;
    private final int RED_LOW_THRESHOLD = 10;
    private final int RED_HIGH_THRESHOLD = 11;

    private final byte COLOR_SENSOR_BEACON_ADDR = 0x3E;

     /*
     * CLAW DECLARATIONS
     */

    public Claw frontClaw, backClaw;
    private final String[] frontServoNames = {"servo1", "servo2", "servo3", "servo4"};
    private final String[] backServoNames = {"servo7", "servo8", "servo9", "servo10"};

    private final double[] frontOpen = {0, 0, 0, 0};
    private final double[] frontRelease = {0, 0, 0, 0};
    private final double[] frontClose = {0, 0, 0, 0};

    private final double[] backOpen = {0, 0, 0, 0};
    private final double[] backRelease = {0, 0, 0, 0};
    private final double[] backClose = {0, 0, 0, 0};

    /*
     * SERVO DECLARATIONS
     */

    private ServoController servoController1, servoController2;
    private Servo jewelArm;
    private final double jewelDown = 0.0;
    private final double jewelUp = 0.0;


    /*
     * MOTOR DECLARATIONS
     */

    private DcMotor frontLift, backLift;

    /*
     * SENSOR DECLARATIONS
     */
    private MRColorSensorV3 jewelColor;
    private DeviceInterfaceModule dim;
    private Telemetry telemetry;


    public Robot11248(HardwareMap hardwareMap, Telemetry telemetry){

        super(hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                telemetry);

        /*
         * MOTOR INITS
         */
        this.frontLift = hardwareMap.dcMotor.get("frontLift");
        this.backLift = hardwareMap.dcMotor.get("backLift");

        /*
         * SERVO INITS
         */
        this.jewelArm = hardwareMap.servo.get("servo5");
        this.frontClaw = new Claw(hardwareMap, frontServoNames, frontOpen, frontRelease, frontClose);
        this.backClaw = new Claw(hardwareMap, backServoNames, backOpen, backRelease, backClose);
        this.servoController1 = hardwareMap.servoController.get("Servo Controller 0");
        this.servoController2 = hardwareMap.servoController.get("Servo Controller 1");

         /*
         * SENSOR INITS
         */

        I2cDevice colorBeacon = hardwareMap.i2cDevice.get("color");
        this.jewelColor = new MRColorSensorV3(colorBeacon, COLOR_SENSOR_BEACON_ADDR);

        this.telemetry = telemetry;
        this.dim = hardwareMap.get(DeviceInterfaceModule.class, "Device Interface Module 1");
    }

    public void init(){
        frontClaw.open();
        backClaw.open();
        lowerJewelArm();
        setDimLed(true, true);
    }


    /*
     * COLOR SENSOR METHODS
     */
    public void activateColorSensors(){
        jewelColor.setPassiveMode();
    }

    public void deactivateColorSensors(){
        jewelColor.setPassiveMode();
    }

    public int getColorBeacon(){
        return jewelColor.getColorNumber();
    }

    public boolean isBeaconBlue(){
        int color = jewelColor.getColorNumber();
        return (BLUE_LOW_THRESHOLD <= color && color <= BLUE_HIGH_THRESHOLD);
    }

    public boolean isBeaconRed(){
        int color = jewelColor.getColorNumber();
        return (RED_LOW_THRESHOLD <= color && color <= RED_HIGH_THRESHOLD);
    }



    /*
     * JEWEL ARM/SERVO METHODS
     */
    public void lowerJewelArm(){
        jewelArm.setPosition(jewelDown);
    }

    public void raiseJewelArm(){
        jewelArm.setPosition(jewelUp);
    }

    public void activateServos(){
        servoController1.pwmEnable();
        servoController2.pwmEnable();
    }

    public void deactivateServos(){
        servoController1.pwmDisable();
        servoController2.pwmDisable();
    }


    /*
     * LIFT METHODS
     */

    public void setFrontLiftPower(double power){
        frontLift.setPower(power);
    }

    public void setBackLiftPower(double power){
        backLift.setPower(power);
    }

    /*
     * LED METHODS
     */
    public void setDimLed(boolean red, boolean blue){
        dim.setLED(0, red);
        dim.setLED(1, blue);
    }

    public void setDimLed(){
        dim.setLED(0, false);
        dim.setLED(1, false);
    }

    public void setDimRedLed(boolean on){
        dim.setLED(0, on);
    }

    public void setDimBlueLed(boolean on){
        dim.setLED(1, on);
    }


}
