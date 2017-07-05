package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by kawaiiPlat on 6/10/2017.
 */

public class TileRunner {

    // Motor objects
    public DcMotor leftDrive1   = null;
    public DcMotor leftDrive2   = null;

    public DcMotor rightDrive1  = null;
    public DcMotor rightDrive2  = null;

    public DcMotor shooter      = null;
    public DcMotor snorfler     = null;
    public DcMotor lifter       = null;


    // Servo objects
    public Servo particleServo      = null;
    public Servo frontTouchServo    = null;


    // Sensor objects
    public TouchSensor frontTouch           = null;
    public TouchSensor debugTouch           = null;

    public AnalogInput sparkfunLeft         = null;
    public AnalogInput sparkfunRight        = null;

    public OpticalDistanceSensor leftODS    = null;
    public OpticalDistanceSensor rightODS   = null;

    public ColorSensor colorSensor          = null;


    // Hardware map
    HardwareMap hwMap = null;


    // Elapsed time
    private ElapsedTime elapsedTime = null;


    // Constructor
    public TileRunner() {
    }


    // Hardware map initialization
    public void init(HardwareMap ahwMap) {

        // Get the hardware map
        hwMap = ahwMap;


        // Get the motors
        leftDrive1  = hwMap.dcMotor.get("xmotor1");
        leftDrive2  = hwMap.dcMotor.get("xmotor2");

        rightDrive1 = hwMap.dcMotor.get("ymotor1");
        rightDrive2 = hwMap.dcMotor.get("ymotor2");

        shooter     = hwMap.dcMotor.get("utilitymotor1");
        snorfler    = hwMap.dcMotor.get("utilitymotor2");
        lifter      = hwMap.dcMotor.get("balllifter");


        // Get the servos
        particleServo   = hwMap.servo.get("particleservo");
        frontTouchServo = hwMap.servo.get("fronttouchservo");


        // Get the sensors
        frontTouch      = hwMap.touchSensor.get("fronttouch");
        debugTouch      = hwMap.touchSensor.get("debugtouch");

        sparkfunLeft    = hwMap.analogInput.get("sparkfunLightSensor1");
        sparkfunRight   = hwMap.analogInput.get("sparkfunLightSensor2");

        leftODS         = hwMap.opticalDistanceSensor.get("leftODS");
        rightODS        = hwMap.opticalDistanceSensor.get("rightODS");

        colorSensor     = hwMap.colorSensor.get("colorsensor");





        // Set the motor directions
        leftDrive1.setDirection (DcMotorSimple.Direction.REVERSE);
        leftDrive2.setDirection (DcMotorSimple.Direction.REVERSE);

        rightDrive1.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive2.setDirection(DcMotorSimple.Direction.FORWARD);

        shooter.setDirection    (DcMotorSimple.Direction.FORWARD);
        snorfler.setDirection   (DcMotorSimple.Direction.FORWARD);
        lifter.setDirection     (DcMotorSimple.Direction.FORWARD);


        // Set the motor powers to zero
        leftDrive1  .setPower(0);
        leftDrive2  .setPower(0);

        rightDrive1 .setPower(0);
        rightDrive2 .setPower(0);

        shooter     .setPower(0);
        snorfler    .setPower(0);
        lifter      .setPower(0);




        // Set the motors to not use encoders
        leftDrive1.setMode  (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDrive2.setMode  (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightDrive1.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive2.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooter.setMode     (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        snorfler.setMode    (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lifter.setMode      (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)elapsedTime.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        elapsedTime.reset();
    }

}