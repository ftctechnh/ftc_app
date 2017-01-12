package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import static android.R.attr.delay;
import static android.R.attr.left;

import java.lang.Thread.*;

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
    public static final double MID_SERVO       =  0.5 ;
    /* local OpMode members. */
    HardwareMap hwMap           = null;
    Telemetry telemetry         = null;
    OpMode activeOpMode         = null;

    /* Matthew, Willow, Alyssa - I just changed the 'period' variable below from private to public.
       This means that you can see this variable from your opModes (e.g. robot.period.reset()).
       This is a useful object for calculating how much time has elapsed (in milliseconds)
       Usage Example:
       // 1st: Define a variable for how many milliseconds you want to do something
       int maxTimeInMilliSecondsIwantMyFunctionToTake = 5000; // 5000mS = 5 seconds

       // 2nd: Reset the elapsed timer with the reset() method
       period.reset(); // Call reset to 'start' the timer

       // 3rd: Do a bunch of work inside a while loop()
       // Here is an example that shows how you could try to see a blue beacon while driving and then
       // stop either when you found it, or when your timer expired.
          boolean iSawBlue = false;

          // Start Driving Forwards
          leftDrive.setPower(1);
          leftDrive.setPower(1);
          while(period.time() < maxTimeInMilliSecondsIwantMyFunctionToTake) {
             // Keep checking the doIseeBlue() function inside this while loop to look for the beacon
             if(doISeeBlue()) {
                 // Hooray: We found the beacon!  Set our boolean variable to true!
                 iSawBlue = true;
                 break; // This 'break' will halt the while loop.
          }
          // Stop driving.  We either saw blue, or we timed out after 5 seconds.
          leftDrive.setPower(0);
          rightDrive.setPower(0);

          if(iSawBlue == true) {
             // Do something neat if we saw blue (maybe activate our beacon pusher (or drive again until we see red)
          }
     */
    public ElapsedTime period  = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /* Public OpMode members. */
    public DcMotor leftMotor   = null;
    public DcMotor rightMotor  = null;
    public CRServo ballElevator = null;
    public DcMotor leftBallLauncher = null;
    public DcMotor rightBallLauncher = null;
    public DcMotor sweeper = null;
    public DcMotor capBall  = null;

    //public UltrasonicSensor UDS = null;
    public Servo   beaconLeft  = null;
    public Servo   beaconRight  = null;

    public ModernRoboticsI2cRangeSensor UDS = null; // Best for longer range sensing (>12")
    public OpticalDistanceSensor ODS = null; // Best for short-range sensing (<12")
    public ColorSensor beaconSensor = null;
    public ColorSensor floorSensor = null;
    public ModernRoboticsI2cGyro gyro = null;
    public TouchSensor touchSensor = null;
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
     *                will turn the robot right the specified degrees, negative numbers will turn the
     *                robot left the specified number of degrees.
     *
     */
    public void GyroTurn(float speed, float degrees) {
        simpleGyroTurn(speed,degrees,5000);
    }


    public void DistanceDrive(float distance, DistanceUnit distanceUnit, float power) {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        while (UDS.getDistance(distanceUnit) > distance) {

        }

        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    //// TODO: 1/10/2017 Fix this some time in the future.
    private boolean opModeIsActive(){
        boolean isActive = true;
        // If we're running a linear op mode, then make sure we stop when our opmode is no longer active
        if(activeOpMode instanceof LinearOpMode)
        {
            isActive = ((LinearOpMode)activeOpMode).opModeIsActive();
        }
        return isActive;
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
        while (floorSensor.red() < Red){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        while (floorSensor.green() < Green){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        while (floorSensor.blue() < Blue){
            rightMotor.setPower(0.5);
            leftMotor.setPower(0.5);
        }
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }

//    // This function is currently disabled.  It just drives straight for now.
//    public void gyroDrive ( double speed,
//                            double distance,
//                            double angle) {
//
//        int     newLeftTarget;
//        int     newRightTarget;
//        int     moveCounts;
//        double  max;
//        double  error;
//        double  steer;
//        double  leftSpeed;
//        double  rightSpeed;
//
//        // Ensure that the opmode is still active
//        if (opModeIsActive()) {
//
//            // Determine new target position, and pass to motor controller
//            moveCounts = (int)(distance * COUNTS_PER_INCH);
//            newLeftTarget = leftMotor.getCurrentPosition() + moveCounts;
//            newRightTarget = rightMotor.getCurrentPosition() + moveCounts;
//
//            // Set Target and Turn On RUN_TO_POSITION
//            leftMotor.setTargetPosition(newLeftTarget);
//            rightMotor.setTargetPosition(newRightTarget);
//
//            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            // start motion.
//            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
//            leftMotor.setPower(speed);
//            rightMotor.setPower(speed);
//
//            // keep looping while we are still active, and BOTH motors are running.
//            while (opModeIsActive() &&
//                    (leftMotor.isBusy() && rightMotor.isBusy())) {
//
//                // DISABLED THE STEERING BY SETTING IT TO ZERO BECAUSE THIS DOESN'T WORK ON OUR ROBOT
//                // adjust relative speed based on heading error.
//                //error = getError(angle);
//                steer = 0;//getSteer(error, P_DRIVE_COEFF);
//
//                // if driving in reverse, the motor correction also needs to be reversed
//                if (distance < 0)
//                    steer *= -1.0;
//
//                leftSpeed = speed - steer;
//                rightSpeed = speed + steer;
//
//                // Normalize speeds if any one exceeds +/- 1.0;
//                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
//                if (max > 1.0)
//                {
//                    leftSpeed /= max;
//                    rightSpeed /= max;
//                }
//
//                leftMotor.setPower(leftSpeed);
//                rightMotor.setPower(rightSpeed);
//
//                // Display drive status for the driver.
//                //telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
//                //telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
//                //telemetry.addData("Actual",  "%7d:%7d",      robot.leftMotor.getCurrentPosition(),
//                        rightMotor.getCurrentPosition();
//                //telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
//                //telemetry.update();
//            }
//
//            // Stop all motion;
//            leftMotor.setPower(0);
//            rightMotor.setPower(0);
//
//            // Turn off RUN_TO_POSITION
//            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//    }


    public boolean doIseeBlue () {

        return (beaconSensor.blue() >= 5);
    }

    // Right/Clockwise = Positive Turn Degrees
    // Left/CounterClockwise = Negative Turn Degrees
    // timeoutMs provides a sanity check to make sure we don't turn forever
    void simpleGyroTurn(double speed, double turnDegrees, int timeoutMs)
    {
        // Our turn degrees has a bit of slop in it.  Try to correct the user input to match reality
        // If turnDegrees == 100, then reset to 75 (which makes our robot turn 100)
        if((turnDegrees > 15) || (turnDegrees < 15)) {
            turnDegrees *= .75;
        }
        else {
            turnDegrees *= .85;
        }

        double targetHeading = gyro.getIntegratedZValue() - turnDegrees;

        // Early Return (do nothing) if we are giving a zero degree turn
        if(turnDegrees == 0) return;

        // Power should be scaled based on how far away we are from our target
        period.reset();
        double MinMotorPower = 0.05; // 5% speed is the slowest we'll try to turn
        double MaxMotorPower = speed;

        // Make sure our Max Power is at least as big as our minimum power
        if (MaxMotorPower < MinMotorPower) MaxMotorPower = MinMotorPower;

        double MaxSpeedError = 75; //If we're more than MaxSpeedError Degrees away from our target heading, use the MaxSpeed to get there
        double MinSpeedError = 25; //If we're less than MinSpeedError Degrees away from our target heading, use the MinSpeed to get there
        double errorDegrees;
        double targetSpeed;
        if(turnDegrees > 0) {
            while (period.time() < timeoutMs && opModeIsActive()) {
                int currentHeading = gyro.getIntegratedZValue();
                errorDegrees = currentHeading - targetHeading;

                // Exit our while loop because we're at our destination
                if(errorDegrees <= 0) break;

                targetSpeed = MaxMotorPower;
                if(errorDegrees < MaxSpeedError) {
                    if(errorDegrees > MinSpeedError)
                    {
                        targetSpeed = (MaxMotorPower-MinMotorPower)/(MaxSpeedError-MinSpeedError)*(errorDegrees-MinSpeedError)+MinMotorPower;
                    }
                    else
                    {
                        targetSpeed = MinMotorPower;

                    }
                }
                telemetry.addData("RT", "Robot Heading = %d, targetHeading: %.02f, targetSpeed: %.02f", currentHeading,targetHeading, targetSpeed);
                telemetry.update();
                leftMotor.setPower(targetSpeed);
                rightMotor.setPower(-targetSpeed);
            }
        }
        else {
            while (period.time() < timeoutMs && opModeIsActive()) {
                int currentHeading = gyro.getIntegratedZValue();
                errorDegrees = targetHeading - currentHeading;

                // Exit our while loop because we're at our destination
                if(errorDegrees <= 0) break;

                targetSpeed = MaxMotorPower;
                if(errorDegrees < MaxSpeedError) {
                    if(errorDegrees > MinSpeedError)
                    {
                        targetSpeed = (MaxMotorPower-MinMotorPower)/(MaxSpeedError-MinSpeedError)*(errorDegrees-MinSpeedError)+MinMotorPower;
                    }
                    else
                    {
                        targetSpeed = MinMotorPower;

                    }
                }
                telemetry.addData("LT", "Robot Heading = %d, targetHeading: %.02f, targetSpeed: %.02f", currentHeading,targetHeading, targetSpeed);
                telemetry.update();

                leftMotor.setPower(-targetSpeed);
                rightMotor.setPower(targetSpeed);
             }
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

// These gyro turn functions do not work on our robot.  No investigation why yet.  Just use 'simpleGyroTurn' function instead.
//    public void gyroTurn (  double speed, double angle) {
//
//        // keep looping while we are still active, and not on heading.
//        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
//            // Update telemetry & Allow time for other processes to run.
//            //telemetry.update();
//        }
//    }
//
//    public void gyroHold( double speed, double angle, double holdTime) {
//
//        ElapsedTime holdTimer = new ElapsedTime();
//
//        // keep looping while we have time remaining.
//        holdTimer.reset();
//        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
//            // Update telemetry & Allow time for other processes to run.
//            onHeading(speed, angle, P_TURN_COEFF);
//            //telemetry.update();
//        }
//
//        // Stop all motion;
//        leftMotor.setPower(0);
//        rightMotor.setPower(0);
//    }

//    boolean onHeading(double speed, double angle, double PCoeff) {
//        double   error ;
//        double   steer ;
//        boolean  onTarget = false ;
//        double leftSpeed;
//        double rightSpeed;
//
//        // determine turn power based on +/- error
//        error = getError(angle);
//
//        if (Math.abs(error) <= HEADING_THRESHOLD) {
//            steer = 0.0;
//            leftSpeed  = 0.0;
//            rightSpeed = 0.0;
//            onTarget = true;
//        }
//        else {
//            steer = getSteer(error, PCoeff);
//            rightSpeed  = speed * steer;
//            leftSpeed   = -rightSpeed;
//        }
//
//        // Send desired speeds to motors.
//        leftMotor.setPower(leftSpeed);
//        rightMotor.setPower(rightSpeed);
//
//        // Display it for the driver.
//        //telemetry.addData("Target", "%5.2f", angle);
//        //telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
//        //telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);
//
//        return onTarget;
//    }
//
//    /**
//     * getError determines the error between the target angle and the robot's current heading
//     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
//     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
//     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
//     */
//    public double getError(double targetAngle) {
//
//        double robotError;
//
//        // calculate error in -179 to +180 range  (
//        robotError = targetAngle - gyro.getIntegratedZValue();
//        while (robotError > 180)  robotError -= 360;
//        while (robotError <= -180) robotError += 360;
//        return robotError;
//    }
//
//    /**
//     * returns desired steering force.  +/- 1 range.  +ve = steer left
//     * @param error   Error angle in robot relative degrees
//     * @param PCoeff  Proportional Gain Coefficient
//     * @return
//     */
//    public double getSteer(double error, double PCoeff) {
//        return Range.clip(error * PCoeff, -1, 1);
//    }
//


    public void WaitForReflectedLight(int intensity, boolean enableLed){
        floorSensor.enableLed(enableLed);
        while (floorSensor.alpha() < intensity){
        }
    }

    /* Constructor */
    public InvadersVelocityVortexBot(){

    }

    public void  ohshoot()
    {
      rightBallLauncher.setPower(1);
      leftBallLauncher.setPower(1);

     //five second delay
        sleepMs(5000);

        ballElevator.setPower(1);

     //insert 5 second delay
        sleepMs(5000);

     ballElevator.setPower(0);
      rightBallLauncher.setPower(0);
      leftBallLauncher.setPower(0);


    }


    public void sleepMs(int millis)
    {
        try { Thread.sleep(millis); } catch (Exception e) {}

    }




    /* Initialize standard Hardware interfaces */
    public void init(OpMode activeOpMode) {
        // Save reference to Hardware map
        hwMap = activeOpMode.hardwareMap;

        // Save reference to the OpMode's Telemetry
        telemetry = activeOpMode.telemetry;

        // Save reference to the active OpMode
        this.activeOpMode = activeOpMode;

        // Define and Initialize Motors
        leftMotor   = hwMap.dcMotor.get("backLeft");
        rightMotor  = hwMap.dcMotor.get("backRight");
        rightBallLauncher = hwMap.dcMotor.get("RightLauncher");
        leftBallLauncher = hwMap.dcMotor.get("LeftLauncher");
        capBall = hwMap.dcMotor.get("CapBall");
        sweeper = hwMap.dcMotor.get("Sweeper");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBallLauncher.setDirection(DcMotor.Direction.FORWARD);
        leftBallLauncher.setDirection(DcMotor.Direction.REVERSE);
        capBall.setDirection(DcMotorSimple.Direction.FORWARD);
        sweeper.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        rightBallLauncher.setPower(0);
        leftBallLauncher.setPower(0);
        capBall.setPower(0);
        sweeper.setPower(0);

        // Set all non-driving motors to run without encoders.
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        capBall.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        beaconLeft = hwMap.servo.get("beaconLeft");
        beaconRight = hwMap.servo.get("beaconRight");
        ballElevator = hwMap.crservo.get("BallElevator");
        beaconLeft.setPosition(0.1);
        beaconRight.setPosition(0.1);
        ballElevator.setPower(0.0);

        // Define our sensors
        touchSensor = hwMap.touchSensor.get("downLimit");
        UDS = hwMap.get(ModernRoboticsI2cRangeSensor.class, "UDS");
        ODS = hwMap.opticalDistanceSensor.get("ODS");
        beaconSensor = hwMap.colorSensor.get("beaconSensor");
        floorSensor = hwMap.colorSensor.get("floorSensor");
        gyro = (ModernRoboticsI2cGyro)hwMap.gyroSensor.get("gyroSensor");

        gyro.calibrate();

        // make sure the gyro is calibrated before continuing
        while (gyro.isCalibrating())  {
            sleepMs(50);
        }
        gyro.resetZAxisIntegrator();

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


    void setCapBallMotorPower(double power)
    {
        capBall.setPower(power);
    }

    void setBallElevator(float power)
    {
        //@todo Write to a file what we're about to do to the motor here
        ballElevator.setPower(power);
    }

    void setLauncherPower(float power){
        leftBallLauncher.setPower(-power);
        rightBallLauncher.setPower(-power);
    }

    void setSweeperPower(float power){
        sweeper.setPower(power);
    }


    /*
 *  Method to perfmorm a relative move, based on encoder counts.
 *  Encoders are not reset as the move is based on the current position.
 *  Move will stop if any of three conditions occur:
 *  1) Move gets to the desired position
 *  2) Move runs out of time
 *  3) Driver stops the opmode running.
 */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rightMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftMotor.setTargetPosition(newLeftTarget);
            rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            period.reset();
            leftMotor.setPower(Math.abs(speed));
            rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (period.seconds() < timeoutS) &&
                    (leftMotor.isBusy() && rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftMotor.getCurrentPosition(),
                        rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleepMs(250);   // optional pause after each move
        }
    }

}
