package org.firstinspires.ftc.team8200;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.

 */
public class HardwareK9bot {

    /* Public OpMode members. */
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;

    public DcMotor leftWheelShooter = null;
    public DcMotor rightWheelShooter = null;


    public DcMotor harvester = null;


    public DcMotor elevator = null;
    public Servo armLeft = null;
    public Servo armRight = null;
    public DeviceInterfaceModule dim = null;

    public LightSensor lightSensorLeft = null; //our Lego Light Sensor
    public LightSensor lightSensorRight = null;
    public ColorSensor colorSensorLeft = null;
    public ColorSensor colorSensorRight = null;
    //our AdaFruit color sensor
    public AnalogInput distanceSensor = null; //our MaxBotix ultrasonic distance sensor

    public static final int LED_CHANNEL = 5;


    /* Local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public HardwareK9bot() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("leftmotor");
        rightMotor = hwMap.dcMotor.get("rightmotor");
        leftWheelShooter = hwMap.dcMotor.get("leftws");
        rightWheelShooter = hwMap.dcMotor.get("rightws");

        /*  -- Because we are using a legacy module, we are not using these names at this time ---
        harvester = hwMap.dcMotor.get("harvester");
        elevator = hwMap.dcMotor.get("elevator");
        */
      //  arm = hwMap.servo.get("arm");
        //legacyController = hwMap.dcMotorController.get("legacy");
        elevator = hwMap.dcMotor.get("elevator");


        dim = hwMap.deviceInterfaceModule.get("dim"); //the Device Interface module connects the sensors
        //sets LED channel (by default 5) to output mode
        //this lights the sensor's LED
        dim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

       // distanceSensor = hwMap.get(AnalogInput.class, "distance");
        lightSensorLeft = hwMap.lightSensor.get("lsleft");
        lightSensorRight = hwMap.lightSensor.get("lsright");

        //colorSensorLeft = hwMap.colorSensor.get("colorleft");
        //colorSensorRight = hwMap.colorSensor.get("colsorRight");


        //Reverses direction of these motors to ease coding

        rightWheelShooter.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        leftWheelShooter.setPower(0);
        rightWheelShooter.setPower(0);

        /* Sets harvester and elevator to 0 power - disabled because we are using legacy module
        harvester.setPower(0);
        elevator.setPower(0);
        */

        //Sets arms to default position
      //  arm.setPosition(0);

        // Set all drivetrain motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        //leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    /*
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
/*
    public void waitForTick(long periodMs) {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();

    }
    */
}


