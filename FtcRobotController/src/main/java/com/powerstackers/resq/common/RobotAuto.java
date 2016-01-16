package com.powerstackers.resq.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Derek on 1/14/2016.
 */
public class RobotAuto {

//    private ColorSensor colorSensor;
//    private TouchSensor touchSensor;
    public static DcMotor motorBrush;
    public static DcMotor motorLift;
    public static DcMotor motorFRight;
    public static DcMotor motorFLeft;
    public static DcMotor motorBRight;
    public static DcMotor motorBLeft;
    public static Servo servoBeacon;

//    set range of servo
    public static final double servoBeacon_MIN_RANGE  = 0.00;
    public static final double servoBeacon_MAX_RANGE  = 1.00;

//    position of servo <Value of Variable>
    public static double servoBeaconPosition;


    public static double enRightPosition = 0.0;
    public static double enLeftPosition = 0.0;

    public static final double EnRightpower = -1;
    public static final double EnLeftpower = -1;

    // Robot Movements in steps
    public static final double EnRightS1 = -9600;
    public static final double EnLeftS1 = 9600;
    public static final double EnRightS2 = -500;
    public static final double EnLeftS2 = 500;


    public RobotAuto(OpMode mode) {

        mode.hardwareMap.logDevices();

        /*Motors
         *
         */
        motorBrush = mode.hardwareMap.dcMotor.get("motorBrush");
        motorLift = mode.hardwareMap.dcMotor.get("motorLift");
        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorFRight = mode.hardwareMap.dcMotor.get("motorFRight");
        motorFLeft = mode.hardwareMap.dcMotor.get("motorFLeft");
        motorFRight.setDirection(DcMotor.Direction.REVERSE);
        motorBRight = mode.hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = mode.hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);

        /*Servos
         *
         */
        servoBeacon = mode.hardwareMap.servo.get("servoBeacon");
        servoBeaconPosition = 0.50;

        //Sensors
//        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
//        colorSensor.enableLed(true);
//        touchSensor = hardwareMap.touchSensor.get("touchSensor");

    }

    public void loop() {

        servoBeaconPosition = Range.clip(servoBeaconPosition, servoBeacon_MIN_RANGE, servoBeacon_MAX_RANGE);

    }

}
