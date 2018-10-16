package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by shrey on 2018-09-12.
 */
public class HardwarePushBot {
    /* Public OpMode members. */
    public static DcMotor  leftFrontDrive   = null;
    public static DcMotor  rightFrontDrive  = null;
    public static DcMotor  leftBackDrive    = null;
    public static DcMotor  rightBackDrive   = null;
    //public static ColorSensor colorSense    = null;
    //public static Blinker blink = null;

    /* local OpMode members. */
    com.qualcomm.robotcore.hardware.HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushBot(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(com.qualcomm.robotcore.hardware.HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontDrive  = hwMap.get(DcMotor.class, "left_front_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        leftFrontDrive.setPower(0);

        rightFrontDrive = hwMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive   = hwMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive  = hwMap.get(DcMotor.class, "right_back_drive");

        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        //leftArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Color Sensor initialization
        //colorSense = hwMap.get(ColorSensor.class, "color_sense");
        //blink = hwMap.get(Blinker.class, "ledController");
    }
 }

