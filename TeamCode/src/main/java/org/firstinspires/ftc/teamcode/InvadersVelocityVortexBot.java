package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
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
public class InvadersVelocityVortexBot
{
    /* Public OpMode members. */
    public DcMotor leftMotor   = null;
    public DcMotor rightMotor  = null;
    public CRServo ballElevator = null;
    public DcMotor leftBallLauncher = null;
    public DcMotor rightBallLauncher = null;
    public Servo   pusher  = null;
    public Servo   beacon  = null;
    public DcMotor capBall  = null;

    public UltrasonicSensor UDS = null;
    public ColorSensor color1 = null;


//FUNCTIONS

    public void GyroTurn(float speed, float degrees) {
        //I think I will set this up so that if it is a positive number, the robot will turn left, and if it is negative it will turn right.
        //// TODO: 12/15/2016 This probably won't work, so Dad, please take a look at this. You can probably tell what I'm trying to do.
        //// TODO: 12/27/2016 Nvm, this probably will work, but we can't test because of the fuse problems.
        //Yay! I can commit!
        int GyroDegrees = 0;
        if(degrees > 0 == true){
            GyroDegrees = 0;
            while (GyroDegrees < degrees == true){
                leftMotor.setPower(0.5);
                rightMotor.setPower(-0.5);
            }

            leftMotor.setPower(0);
            rightMotor.setPower(0);


        }
        else {
            GyroDegrees = 0;
            while (GyroDegrees < degrees == true) {
                leftMotor.setPower(-0.5);
                rightMotor.setPower(0.5);
            }

            leftMotor.setPower(0);
            rightMotor.setPower(0);



        }
    }

    public void DistanceDrive(float distance, float power) {
        while (UDS.getUltrasonicLevel() > distance) {
            leftMotor.setPower(power);
            rightMotor.setPower(power);
        }

        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    public void ColorDrive(int Red, int Blue, int Green){
        while (color1.red() < Red){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        while (color1.green() < Green){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        while (color1.blue() < Blue){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }



    public static final double MID_SERVO       =  0.5 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public InvadersVelocityVortexBot(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("front_left");
        rightMotor  = hwMap.dcMotor.get("front_right");
        rightBallLauncher = hwMap.dcMotor.get("RightLauncher");
        leftBallLauncher = hwMap.dcMotor.get("LeftLauncher");
        capBall = hwMap.dcMotor.get("CapBall");

        UDS = hwMap.ultrasonicSensor.get("UDS");
        color1 = hwMap.colorSensor.get("color1");

        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        rightBallLauncher.setDirection(DcMotor.Direction.FORWARD);
        leftBallLauncher.setDirection(DcMotor.Direction.REVERSE);
        capBall.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        rightBallLauncher.setPower(0);
        leftBallLauncher.setPower(0);
        capBall.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        capBall.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        pusher = hwMap.servo.get("pusher");
        beacon = hwMap.servo.get("beacon");
        ballElevator = hwMap.crservo.get("ballElevator");
        pusher.setPosition(.50);
        beacon.setPosition(0.1);
        ballElevator.setPower(0.0);
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

