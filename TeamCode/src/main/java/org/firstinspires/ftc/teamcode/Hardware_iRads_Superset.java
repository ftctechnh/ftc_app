package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode. (It isn't even a working Hardware calss. See below.)
 *
 * This class is an EXAMPLE of a superset of possible iRads Hardware classes. 
 * The intent is to provide redundant standardized naming conventions for possible 
 * hardware configurations.
 * This class defines 11 Motors, but the hardware can only support 8 Motor
 * connections.  
 *
 * To use this class, copy it in Android studio, rename it something
 * descriptive, and delete the Servos and Motors that are not required for your 
 * configuration.
 *
 * ...
 *
 * This hardware class assumes the following device names have been configured on the robot:
 *
 * DcMotors:
 * Motor channel:  Left  drive motor:        "leftDriveMotor"
 * Motor channel:  Right drive motor:        "rightDriveMotor"
 * Motor channel:  Left  launch motor:       "leftLaunchMotor"
 * Motor channel:  Right launch motor:       "rightLaunchMotor"
 * Motor channel:  Cap ball lift motor:      "liftMotor"
 * Motor channel:  Rotary sweeper  motor:    "sweeperMotor"
 * Motor channel:  Ball elevator motor:      "ballElevatorMotor"
 * For 4-wheel drive:
 * Motor channel:  NorthWest drive motor:    "nwDriveMotor"
 * Motor channel:  NorthEast drive motor:    "neDriveMotor"
 * Motor channel:  SouthWest drive motor:    "swDriveMotor"
 * Motor channel:  SouthEast drive motor:    "seDriveMotor"
 * Servos:
 * Servo channel:  Servo to push button:     "buttonPusher"
 * Servo channel:  Servo for left flipper:   "leftFlipper"
 * Servo channel:  Servo for right flipper:  "rightFlipper"
 * Servo channel:  Servo load launcher:      "ballLoader"
 * Servo channel:  Servo to push button:     "ballLoader"
 */
public class Hardware_iRads_Superset
{
    /* Public OpMode members. */
    // DcMotors:
    public DcMotor  leftDriveMotor      = null;
    public DcMotor  rightDriveMotor     = null;
    public DcMotor  leftLaunchMotor     = null;
    public DcMotor  rightLaunchMotor    = null;
    public DcMotor  liftMotor           = null;
    public DcMotor  sweeperMotor        = null;
    public DcMotor  ballElevatorMotor   = null;
        // 4-wheel drive DcMotors:
    public DcMotor  nwDriveMotor        = null;
    public DcMotor  neDriveMotor        = null;
    public DcMotor  swDriveMotor        = null;
    public DcMotor  seDriveMotor        = null;
    // Servos:
    public Servo    buttonPusher        = null;
    public Servo    leftFlipper         = null;
    public Servo    rightFlipper        = null;
    public Servo    ballLoader          = null;

    public static final double MID_SERVO          =  0.5 ;
    public static final double LIFT_UP_POWER      =  0.45 ;
    public static final double LIFT_DOWN_POWER    = -0.45 ;
    public static final double MIN_LAUNCH_POWER   =  0.45 ;
    public static final double MAX_LAUNCH_POWER   =  0.85 ;

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
    public Hardware_iRads_Superset(){

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
        sweeperMotor      = hwMap.dcMotor.get("sweeperMotor");
        ballElevatorMotor = hwMap.dcMotor.get("ballElevatorMotor");
            // 4-wheel drive:
        nwDriveMotor      = hwMap.dcMotor.get("nwDriveMotor");
        neDriveMotor      = hwMap.dcMotor.get("neDriveMotor");
        swDriveMotor      = hwMap.dcMotor.get("swDriveMotor");
        seDriveMotor      = hwMap.dcMotor.get("seDriveMotor");


        // Set Motor Direction
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftLaunchMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightLaunchMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
            // 4-wheel drive:
        nwDriveMotor.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        neDriveMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        swDriveMotor.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        seDriveMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors



        // Set all motors to zero power
        leftDriveMotor.setPower(0);
        rightDriveMotor.setPower(0);
        leftLaunchMotor.setPower(0);
        rightLaunchMotor.setPower(0);
        liftMotor.setPower(0);
        sweeperMotor.setPower(0);
        ballElevatorMotor.setPower(0);
            // 4-wheel drive:
        nwDriveMotor.setPower(0);
        neDriveMotor.setPower(0);
        swDriveMotor.setPower(0);
        seDriveMotor.setPower(0);


        // Set all motors to run with encoders.
        // Use RUN_WITHOUT_ENCODERS if encoders are NOT installed.
        // Use RUN_USING_ENCODERS if encoders ARE installed.
        leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        leftLaunchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightLaunchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        sweeperMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        ballElevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
            // 4-wheel drive:
        nwDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        neDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        swDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        seDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        // Set ENCODER mode max speed (in Ticks per second)
        leftDriveMotor.setMaxSpeed(MAX_DRIVE_SPEED_TPS);
        rightDriveMotor.setMaxSpeed(MAX_DRIVE_SPEED_TPS);
        leftLaunchMotor.setMaxSpeed(MAX_LAUNCH_SPEED_TPS);
        rightLaunchMotor.setMaxSpeed(MAX_LAUNCH_SPEED_TPS);
        liftMotor.setMaxSpeed(LIFT_MAX_SPEED_TPS);



        // Define and initialize ALL installed servos.
        buttonPusher = hwMap.servo.get("buttonPusher");
        leftFlipper  = hwMap.servo.get("leftFlipper");
        rightFlipper = hwMap.servo.get("rightFlipper");
        ballLoader   = hwMap.servo.get("ballLoader");
        buttonPusher.setPosition(MID_SERVO);
        leftFlipper.setPosition(MID_SERVO);
        rightFlipper.setPosition(MID_SERVO);
        ballLoader.setPosition(MID_SERVO);

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

