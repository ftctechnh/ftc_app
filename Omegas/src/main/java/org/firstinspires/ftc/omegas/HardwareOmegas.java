package org.firstinspires.ftc.omegas;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * It will be updated to accurately reflect the peripherals and components
 * available on our finished FTC Velocity Vortex robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:                "left_drive"
 * Motor channel:  Right drive motor:                "right_drive"
 * Servo channel:  Servo to push left beaconator:    "left_beaconator"
 * Servo channel:  Servo to open right beaconator:   "right_beaconator"
 * Color sensor:  Color sensor for left beaconator:  "left_color_sensor"
 * Color sensor:  Color sensor for right beaconator: "right_color_sensor"
 */
public class HardwareOmegas
{
    /* Public OpMode members. */
    public ColorSensor  leftColorSensor  = null;
    public ColorSensor  rightColorSensor = null;
    public DcMotor      leftFrontMotor   = null;
    public DcMotor      leftBackMotor    = null;
    public DcMotor      rightFrontMotor  = null;
    public DcMotor      rightBackMotor   = null;
    public CRServo      leftBeaconator   = null;
    public CRServo      rightBeaconator  = null;

    public ArrayList<DcMotor> motors;

    public static final double  MID_SERVO       =  0.5;
    public static final double  ARM_UP_POWER    =  0.45;
    public static final double  ARM_DOWN_POWER  = -0.45;
    public static final double  MS_PER_RADIAN   = 0;
    public static       boolean isExtending     = false;

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontMotor  = hwMap.dcMotor.get("left_front");
        leftBackMotor   = hwMap.dcMotor.get("left_back");
        rightFrontMotor = hwMap.dcMotor.get("right_front");
        rightBackMotor  = hwMap.dcMotor.get("right_back");

        motors = new ArrayList<DcMotor>(){
            {
                add(leftFrontMotor);
                add(leftBackMotor);
                add(rightFrontMotor);
                add(rightBackMotor);
            }
        };

        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);  // Set to REVERSE if using AndyMark motors
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);   // Set to REVERSE if using AndyMark motors
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);  // Set to FORWARD if using AndyMark motors

        // Connect to servo (Assume PushBot Left Hand)
        // Change the text in quotes to match any servo name on your robot.
        leftBeaconator = hwMap.crservo.get("left_beaconator");
//        rightBeaconator = hwMap.crservo.get("right_beaconator");

        leftColorSensor = hwMap.colorSensor.get("left_color_sensor");
//        rightColorSensor = hwMap.colorSensor.get("right_color_sensor");

//        leftColorSensor.enableLed(true);
//        rightColorSensor.enableLed(true);

        // Set all motors to zero power, and to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        for (DcMotor motor : motors) {
            motor.setPower(0.0);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
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
        long  remaining = periodMs - (long)period.milliseconds();

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

    public void rotate(long radians) {
        ElapsedTime timePushed = new ElapsedTime();

        while (timePushed.milliseconds() < radians * MS_PER_RADIAN) {
            leftBackMotor.setPower(1.0);
            leftFrontMotor.setPower(1.0);
            rightBackMotor.setPower(-1.0);
            rightFrontMotor.setPower(-1.0);
        }
    }

    public void beaconatorSequence(CRServo beaconator, long milliseconds) {
        ElapsedTime timePushed = new ElapsedTime();
        if (isExtending) return; else isExtending = true;

        while (true) {
            if (timePushed.milliseconds() < milliseconds) {
                powerServo(beaconator, 1.0);
            } else if (timePushed.milliseconds() < milliseconds * 2) {
                retractServo(beaconator);
            } else {
                powerServo(beaconator, 0.0);
                return;
            }
        }
    }

    public void powerServo(CRServo beaconator, double power) {
        beaconator.setDirection(CRServo.Direction.FORWARD);
        beaconator.setPower(Math.abs(power));
    }

    public void retractServo(CRServo beaconator) {
        beaconator.setDirection(CRServo.Direction.REVERSE);
        beaconator.setPower(1.0);
    }
}

