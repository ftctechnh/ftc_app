package org.firstinspires.ftc.team11248;

/**
 * Created by tonytesoriero on 9/11/17.
 */

import com.qualcomm.robotcore.hardware.ColorSensor;
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
import org.firstinspires.ftc.team11248.Hardware.MRRangeSensor_V2;
import org.firstinspires.ftc.team11248.Hardware.Vuforia_V2;

public class Robot11248 extends HolonomicDriver_11248 {

    public Vuforia_V2 vuforia;
    public static final String vuforiaKey = "AeTwV0H/////AAAAGfe7ayWmjE9+nI9k65aoO+NQIIujZBIX8AxeoVDf9bwLLNvQ6QwvM+Clc3CE/8Pumv5guDuXMxkERpyJTzSb50PcrH9y/lJC9Zfh0FlPVkkvDnZVNsPEIEsg0Ta5oDlz1jIZmSB/Oxu2qRAyo4jXIsWSmDMdQdpNrwkyKbLfl/CT7PWe23RAdF8oQf5XqnSbKoapQali8MH4+HPOR8r13/k+cZv9eKqUvknmxZPiyJbp4oFzqrWDJSUqwTGQLEdbp76Hjrkuxu3Pa/I4jQSt3RRRbAUrZeV1Z79cLKg+22SvrhUKKzwxeEMcgp4rQzrMXhTL+wE+6sBczuguHmPtWA5w/NsUlevRaLbEionbyXYN";

    private boolean silent = false;

    //Gyro Thresholds
    private static final int GYRO_THRESHOLD = 1;

    //Sensor Addresses

    private final byte JEWEL_COLOR_SENSOR_ADDR = 0x36;
    private final byte FRONT_FLOOR_COLOR_SENSOR_ADDR = 0x3C; //Front
    private final byte BACK_FLOOR_COLOR_SENSOR_ADDR = 0x60; //Back
    private final byte RANGE_SENSOR_ADDR = 0x28;
    private final byte GYRO_SENSOR_ADDR = 0x20; //Not used

     /*
     * CLAW DECLARATIONS
     */

    public Claw frontClaw, backClaw;
    private final String[] frontServoNames = {"servo1", "servo9", "servo2", "servo8"};
    private final String[] backServoNames = {"servo10", "servo3", "servo11", "servo4"};

    private final double[] close = {0, 0, 0, 0};
    private final double[] release = {.6, .3, .6, .3};
    private final double[] grab = {.45, .55, .45, .55};
    private final double[] open = {1, 0, 1, 0};

    /*
     * SERVO DECLARATIONS
     */

    private ServoController servoController1, servoController2;
    private Servo jewelArm;
    private final double jewelDown = 0.09; // servo7
    private final double jewelUp = .85;


    /*
     * MOTOR DECLARATIONS
     */

    public DcMotor frontLift, backLift; //TODO private

    /*
     * SENSOR DECLARATIONS
     */
    public MRColorSensorV3 jewelColor, frontFloorColor, backFloorColor;
    public MRRangeSensor_V2 rangeSensor;
    public GyroSensor gyro;
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

        this.frontLift.setDirection(DcMotorSimple.Direction.REVERSE); //Positive is up
        this.backLift.setDirection(DcMotorSimple.Direction.REVERSE);

        /*
         * SERVO INITS
         */
        this.jewelArm = hardwareMap.servo.get("servo7");
        this.frontClaw = new Claw(hardwareMap, frontServoNames, open, release, grab, close);
        this.backClaw = new Claw(hardwareMap, backServoNames, open, release, grab, close);
        this.servoController1 = hardwareMap.servoController.get("Servo Controller 0");
        this.servoController2 = hardwareMap.servoController.get("Servo Controller 1");

         /*
         * SENSOR INITS
         */

        this.gyro = hardwareMap.gyroSensor.get("gyro");

        I2cDevice range = hardwareMap.i2cDevice.get("range");
        I2cDevice color1 = hardwareMap.i2cDevice.get("color");
        I2cDevice color2 = hardwareMap.i2cDevice.get("colorFloorBack");
        I2cDevice color3 = hardwareMap.i2cDevice.get("colorFloorFront");

        this.rangeSensor = new MRRangeSensor_V2(range, RANGE_SENSOR_ADDR);
        this.jewelColor = new MRColorSensorV3(color1, JEWEL_COLOR_SENSOR_ADDR);
        this.backFloorColor = new MRColorSensorV3(color2, BACK_FLOOR_COLOR_SENSOR_ADDR);
        this.frontFloorColor = new MRColorSensorV3(color3, FRONT_FLOOR_COLOR_SENSOR_ADDR);

        this.telemetry = telemetry;
        this.dim = hardwareMap.get(DeviceInterfaceModule.class, "Device Interface Module 1");

        this.vuforia = new Vuforia_V2(hardwareMap);
    }

    public void init(){

        frontClaw.open();
        backClaw.open();
        raiseJewelArm();
        setDimLed(true, true);
    }


    /*
     * COLOR SENSOR METHODS
     */
    public void activateColorSensors(){
        jewelColor.enableLed(true);
        frontFloorColor.enableLed(true);
        backFloorColor.enableLed(true);
    }

    public void deactivateColorSensors(){
        jewelColor.enableLed(false);
        frontFloorColor.enableLed(false);
        backFloorColor.enableLed(false);
    }



     /*
     * GYRO / DRIVE METHODS
     */

    public void calibrateGyro(){
        gyro.calibrate();
        while(gyro.isCalibrating());
    }

    public int getGyroAngle(){
        return  gyro.getHeading();
    }

    public boolean driveWithGyro(double x, double y, int targetAngle, boolean smooth) {

        boolean atAngle = false;
        int currentAngle = getGyroAngle();
        int net = currentAngle - targetAngle; //finds distance to target angle
        double rotation;

        if (Math.abs(net) > 180) { // if shortest path passes 0
            if (currentAngle > 180) //if going counterclockwise
                net = (currentAngle - 360) - targetAngle;

            else //if going clockwise
                net = (360 - targetAngle) + currentAngle;
        }

        // slows down as approaches angle with min threshold of .05
        // each degree adds/subtracts .95/180 values of speed
        rotation = Math.abs(net) * .85 / 180 + .10;

        if (net < 0) rotation *= -1; //if going clockwise, set rotation clockwise (-)

        if (!(Math.abs(net) > GYRO_THRESHOLD)){
            atAngle = true;
            rotation = 0;
        }

        driveWithFixedAngle(x, y, rotation, 360 - getGyroAngle() + targetAngle, smooth); //Drive with gyros rotation

        if(silent) {
            telemetry.addData("ROBOT11248", "Heading: " + getGyroAngle());
            telemetry.addData("ROBOT11248", "Net: " + net);
            telemetry.addData("ROBOT11248", "Speed: " + rotation);
            telemetry.addData("ROBOT11248", "Target: " + targetAngle);
            telemetry.update();
        }

        return atAngle;
    }

    public boolean driveWithGyro(double x, double y, int targetAngle) {
        return driveWithGyro(x,y,targetAngle, false);
    }


    /**
     *
     * @param x - x direction power
     * @param y - y direction power
     * @param rotate - power for rotation
     * @param fixedAngle -  angle orentation is fixed on - set = to 359 - getGyroAngle()
     */
    public void driveWithFixedAngle(double x, double y, double rotate, int fixedAngle, boolean smooth){

        setOffsetAngle((Math.toRadians(fixedAngle)));
        drive(x, y, rotate, smooth);
    }

    public void driveWithFixedAngle(double x, double y, double rotate, int fixedAngle) {

        driveWithFixedAngle(x, y, rotate, fixedAngle, false);
    }

    public boolean moveToAngle(int targetAngle){
        return driveWithGyro(0,0,targetAngle);
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

    /*
     * TELEMETRY
     */

    public void useTelemetry(boolean on){
        silent = on;
    }


}
