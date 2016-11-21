package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
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
public class HardwarePushbot_TT
{
    /* Public OpMode members. */
    /*public DcMotor  leftMotor   = null;
    public DcMotor  rightMotor  = null;
    public DcMotor  armMotor    = null;
    public Servo    leftClaw    = null;
    public Servo    rightClaw   = null;
    */

    // Defining our motors in here - Varun
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor armMotor = null;
    public ColorSensor color;    // Hardware Device Object
    public TouchSensor touch;  // Hardware Device Object
    public ModernRoboticsI2cGyro gyro;  // Hardware Device Object
    public DeviceInterfaceModule cdim;
    public DcMotor flyRight = null;
    public DcMotor flyLeft = null;
    /* public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;
    */

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushbot_TT(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontLeftMotor = hwMap.dcMotor.get("FrontLeft");
        backLeftMotor = hwMap.dcMotor.get("BackLeft");
        frontRightMotor = hwMap.dcMotor.get("FrontRight");
        backRightMotor = hwMap.dcMotor.get("BackRight");
        touch = hwMap.touchSensor.get("TouchSensor");
        color = hwMap.colorSensor.get("ColorSensor");
//        gyro = hwMap.gyroSensor.get("GyroSensor");
        gyro = (ModernRoboticsI2cGyro)hwMap.gyroSensor.get("GyroSensor");
        cdim = hwMap.deviceInterfaceModule.get("Device Interface Module 1");
        //armMotor    = hwMap.dcMotor.get("ArmMotor");
//        flyRight = hwMap.dcMotor.get("FlyLeft");
//        flyRight = hwMap.dcMotor.get("FlyLeft");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to FORWARD if using AndyMark motors
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to FORWARD if using AndyMark motors
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
//        armMotor.setDirection(DcMotor.Direction.REVERSE);
//        flyLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        flyRight.setDirection(DcMotorSimple.Direction.REVERSE);


        // Set all motors to zero power
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
//        armMotor.setPower(0);
//        flyLeft.setPower(0);
//        flyRight.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        flyLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        flyRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Define and initialize ALL installed servos.
        /*leftClaw = hwMap.servo.get("left_hand");
        rightClaw = hwMap.servo.get("right_hand");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);
        */

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

