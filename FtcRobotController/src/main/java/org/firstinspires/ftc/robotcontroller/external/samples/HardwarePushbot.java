package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
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
public class HardwarePushbot
{
    /* Public OpMode members. */
    public DcMotor  leftMotor   = null;
    public DcMotor  rightMotor  = null;
    public Servo    leftClaw    = null;
    public Servo    rightClaw   = null;
    public CRServo BallElevator    = null;
    public DcMotor  LeftBallLauncher   = null;
    public DcMotor  RightBallLauncher   = null;
    public Servo    pusher  = null;
    public Servo    beacon  = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushbot(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("left_drive");
        rightMotor  = hwMap.dcMotor.get("right_drive");
        //armMotor    = hwMap.dcMotor.get("left_arm");
        RightBallLauncher = hwMap.dcMotor.get("RightLauncher");
        LeftBallLauncher = hwMap.dcMotor.get("LeftLauncher");

        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        RightBallLauncher.setDirection(DcMotor.Direction.FORWARD);
        LeftBallLauncher.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        RightBallLauncher.setPower(0);
        LeftBallLauncher.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        pusher = hwMap.servo.get("pusher");
        beacon = hwMap.servo.get("beacon");
        BallElevator = hwMap.crservo.get("BallElevator");
        pusher.setPosition(.50);
        beacon.setPosition(0.1);
        BallElevator.setPower(0.0);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
<<<<<<< HEAD
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {
=======
     */
    public void waitForTick(long periodMs) {
>>>>>>> refs/remotes/ftctechnh/master

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
<<<<<<< HEAD
        if (remaining > 0)
            Thread.sleep(remaining);
=======
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
>>>>>>> refs/remotes/ftctechnh/master

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

