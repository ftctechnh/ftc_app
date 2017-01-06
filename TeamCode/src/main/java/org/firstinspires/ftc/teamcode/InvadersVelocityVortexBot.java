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
 * In this case that robot is the IfSpace Invaders 2016/2017 Velocity Vortex season robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * @todo Matthew, Alyssa, or Willow, please update these Motor Channel comments to match what we are
 * actually using.  These current defintions were left over from last-year's pushbot robot.
 * Also, please add in the Sensor definitions as well.  The goal is that the comment section here
 * clearly shows all of the sensors/motors were using on the robot and has names that match what
 * we are supposed to use in our Robot Controller Configuration file.
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
/*
     @todo Matthew, Willow, or Alyssa - Please add in a new state variable for each old-style
     I2C sensor on our robot (we should have two color sensors total, one gyro sensor) of type
     FtcI2cDeviceState.  Note: The Ultrasonic sensor is a newer I2C device that doesn't work with
     the FtcI2cDeviceState class.

     Reference GitHub Commit: https://github.com/IfSpace/ftc_app/commit/9bc2de71a8464b4a608382e7af89070efbdd0301
     An example variable for our ultrasonic distance sensor (which we declared on line 46 above as
     'UDS' would look like this:
          public FtcI2cDeviceState UDSstate;
     Notes:
     1) Don't forget to #import the new FtcI2cDeviceState class at the top of this file so the
        FtcI2cDeviceState class type is recognized.  e.g. "import org.firstinspires.ftc.teamcode.FtcI2cDeviceState;"
     2) Don't forget to instantiate your new sensor-state variables at the same time you create
        the sensor variables.  Hint: This is done in the init() function in this class.
     3) We probably want to disable all of our sensors by default in our init(), so after you new
        up each sensor-state variable, you should disable it by calling its setEnabled(false)
        function.
     4) Don't forget to delete all of these comments once you have finished the work so it doesn't
        clutter up this file with leftover notes.
*/

//FUNCTIONS

    /**
     * GyroTurn function allows our robot to do make precise +/- degrees turns using the gyro sensor
     * @param speed currently unused.
     * @param degrees indicates the number of degrees to turn left or right.  Positive numbers
     *                will turn the robot left the specified degrees, negative numbers will turn the
     *                robot right the specified number of degrees.
     */
    public void GyroTurn(float speed, float degrees) {
/*
        @todo Matthew, Alyssa, or Willow - we need to decide here what 'degrees' means here.
           Should it be an Absolute heading  (ie turn to 350° always means the same direction,
           regardless of what direction we were facing when we started).
           If we use this approach, we should only zeroize our gyro sensor once (at the start of the
           op-mode) and then every time we pass in a degree value, or robot will turn precisely to
           the specified direction regardless of where it happened to be facing at the time.

           Or...
           Should it be a Relative heading (ie turn to 10° means turn left by ten degrees).
           If we use this approach, then we should zeroize the gyro at the start of this function,
           wait a moment for it to stabilize, and then turn left/right by the specified degrees.

           I don't actually know which will be easier to use in the long run, but the logic/math
           below will need to be updated slightly depending on how we decide to proceed.
 */

        int GyroDegrees = 0;
        if(degrees > 0 == true){
            GyroDegrees = 0;
            leftMotor.setPower(0.2);
            rightMotor.setPower(-0.2);
            while (GyroDegrees < degrees == true){
                // @todo Matthew, Alyssa, or Willow - this while loop doesn't update the value of
                // GyroDegrees so we'll be stuck here forever in an endless loop.  We need to read
                // the gyro sensor's value here.
            }
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }
        else {
            GyroDegrees = 0;
            leftMotor.setPower(-0.2);
            rightMotor.setPower(0.2);
            while (GyroDegrees < degrees == true) {
                // @todo Matthew, Alyssa, or Willow - this while loop doesn't update the value of
                // GyroDegrees so we'll be stuck here forever in an endless loop.  We need to read
                // the gyro sensor's value here.
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

    /**
     * ColorDrive @todo Matthew, Alyssa, or Willow - we need to decide how this function is going to
     * work.  How do we use this to drive to the white line?  The current logic below has us
     * driving until red matches, then checking green, then blue.  But what if blue matched first
     * but we drove past it looking for a red match?  This is going to be tricky.  I think we may
     * either need to key off of just a single color or rename the red, blue, green variables to
     * something like redChange, blueChange, and greenChange and then drive until all three colors
     * have changed from their initial values to at least what our changed values are.  Testing this
     * by passing a color sensor over a black sheet of paper with a white taped line on it may help
     * you decide what is best to look for.
     * @param Red @todo add the color parameter descriptions for Red, Blue, and Green once you decide what they mean.
     * @param Blue
     * @param Green
     */
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

