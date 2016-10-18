package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

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
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareOmegas
{
    /* Public OpMode members. */
    ColorSensor     leftColorSensor  = null;
    ColorSensor     rightColorSensor = null;
    public DcMotor  leftMotor        = null;
    public DcMotor  rightMotor       = null;
    public CRServo  leftBeaconator   = null;
    public CRServo  rightBeaconator  = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("left_drive");
        rightMotor  = hwMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        // Connect to servo (Assume PushBot Left Hand)
        // Change the text in quotes to match any servo name on your robot.
        leftBeaconator = hwMap.crservo.get("left_beaconator");
//        rightBeaconator = hwMap.crservo.get("right_beaconator"); // Unimplemented for the time being.

        rightColorSensor = hwMap.colorSensor.get("left_color_sensor");
//        leftColorSensor = hwMap.colorSensor.get("right_color_sensor"); // Unimplemented for the time being.

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

    public void beaconatorSequence(CRServo beaconator, long milliseconds){
        ElapsedTime timePushed = new ElapsedTime();

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

