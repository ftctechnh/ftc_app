package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is the iRads robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 *
 * DcMotors:
 * Motor channel:  Left  drive motor:        "leftDriveMotor"
 * Motor channel:  Right drive motor:        "rightDriveMotor"
 * Motor channel:  Left  launch motor:       "leftLaunchMotor"
 * Motor channel:  Right launch motor:       "rightLaunchMotor"
 * Motor channel:  Cap ball lift motor:      "liftMotor"
 * Servos:
 * Servo channel:  Servo to push button:     "buttonPusher"
 */
public class Hardware_iRads
{
    /* Public OpMode members. */
    // DcMotors:
    public DcMotor  leftDriveMotor      = null;
    public DcMotor  rightDriveMotor     = null;
    public DcMotor  leftLaunchMotor     = null;
    public DcMotor  rightLaunchMotor    = null;
    public DcMotor  liftMotor           = null;
    // Servos:
    public Servo    buttonPusher        = null;


    public static final double MID_SERVO            =  0.5 ;

    public static final double LAUNCH_WHEEL_DIAMETER_INCHES =  4;
    public static final double DRIVE_WHEEL_DIAMETER_INCHES  =  4;

    public static final int TICKS_PER_ROT_DRIVE     =  28*60; // Ticks per rotation
    public static final int TICKS_PER_ROT_LIFT      =  28*60; // Ticks per rotation
    public static final int TICKS_PER_ROT_LAUNCH    =  28*1 ; // Ticks per rotation

    public static final int MAX_DRIVE_SPEED_TPS     =  1680 ; // Ticks per second
    public static final int LIFT_MAX_SPEED_TPS      =  1680 ; // Ticks per second
    public static final int MAX_LAUNCH_SPEED_TPS    =   280 ; // Ticks per second

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware_iRads(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDriveMotor    = hwMap.dcMotor.get("leftDriveMotor");
        rightDriveMotor   = hwMap.dcMotor.get("rightDriveMotor");
        leftLaunchMotor   = hwMap.dcMotor.get("leftLaunchMotor");
        rightLaunchMotor  = hwMap.dcMotor.get("rightLaunchMotor");
        liftMotor         = hwMap.dcMotor.get("liftMotor");
        // Set Motor Direction
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftLaunchMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightLaunchMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftDriveMotor.setPower(0);
        rightDriveMotor.setPower(0);
        leftLaunchMotor.setPower(0);
        rightLaunchMotor.setPower(0);
        liftMotor.setPower(0);


        // Set all motors to run with encoders.
        // Use RUN_WITHOUT_ENCODERS if encoders are NOT installed.
        // Use RUN_USING_ENCODERS if encoders ARE installed.
        leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        leftLaunchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightLaunchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        // Set ENCODER mode max speed (in Ticks per second)
        leftDriveMotor.setMaxSpeed(MAX_DRIVE_SPEED_TPS);
        rightDriveMotor.setMaxSpeed(MAX_DRIVE_SPEED_TPS);
        leftLaunchMotor.setMaxSpeed(MAX_LAUNCH_SPEED_TPS);
        rightLaunchMotor.setMaxSpeed(MAX_LAUNCH_SPEED_TPS);
        liftMotor.setMaxSpeed(LIFT_MAX_SPEED_TPS);


        // Define and initialize ALL installed servos.
        buttonPusher = hwMap.servo.get("buttonPusher");
        buttonPusher.setPosition(MID_SERVO);

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
}

