package org.firstinspires.ftc.teamcode.Competition;

import org.firstinspires.ftc.teamcode.FtcI2cDeviceState;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is the IfSpace Invaders 2016/2017 Velocity Vortex season robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Ref: .\IfSpaceInvaders_ModernRobotics_Hardware_Defintions.txt for details
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
             // Keep checking the soIseeBlueLeft() function inside this while loop to look for the beacon
             if(soIseeBlueLeft()) {
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
    public DcMotor leftDrive   = null;
    public DcMotor rightDrive  = null;
    public CRServo ballElevator = null;
    public DcMotor leftBallLauncher = null;
    public DcMotor rightBallLauncher = null;
    public DcMotor sweeper = null;
    public DcMotor capBall  = null;

    public Servo   beaconLeft  = null;
    public Servo   beaconRight  = null;

    public ModernRoboticsI2cRangeSensor UDSLeft = null;   // Default I2C Address: 0x26
    public ModernRoboticsI2cRangeSensor UDSRight = null;  // Default I2C Address: 0x28

    public ColorSensor beaconSensorLeft = null;
    public FtcI2cDeviceState beaconSensorLeftState;

    public ColorSensor beaconSensorRight = null;
    public FtcI2cDeviceState beaconSensorRightState;

    public ColorSensor floorSensor = null;
    public FtcI2cDeviceState floorSensorState;

    public ModernRoboticsI2cGyro gyro = null;
    public FtcI2cDeviceState gyroState;

    public TouchSensor touchSensor = null;
/*
     @todo Matthew, Willow, or Alyssa - Please add in a new state variable for each old-style
     I2C sensor on our robot (we should have two color sensors total, one gyro sensor) of type
     FtcI2cDeviceState.  Note: The Ultrasonic sensor is a newer I2C device that doesn't work with
     the FtcI2cDeviceState class.

     Reference GitHub Commit: https://github.com/IfSpace/ftc_app/commit/9bc2de71a8464b4a608382e7af89070efbdd0301
     An example variable for our ultrasonic distance sensor (which we declared on line 46 above as
     'UDSLeft' would look like this:
          public FtcI2cDeviceState UDSLeftstate;
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
    public void GyroTurn(double speed, double degrees,int timeoutMs) {
        simpleGyroTurn(speed,degrees,timeoutMs);
    }


    public void DriveToWall(float distance, DistanceUnit distanceUnit, double power, int timeoutMs) {
        period.reset();
        setDriveTrainPower(power);
        while ((period.time() < timeoutMs) && opModeIsActive()) {
            if(UDSLeft.getDistance(distanceUnit) <= distance) break;
            if(UDSRight.getDistance(distanceUnit) <= distance) break;
        }
        setDriveTrainPower(0);
    }

    private boolean opModeIsActive(){
        boolean isActive = true;
        // If we're running a linear op mode, then make sure we stop when our opmode is no longer active
        if(activeOpMode instanceof LinearOpMode)
        {
            isActive = ((LinearOpMode)activeOpMode).opModeIsActive();
        }
        return isActive;
    }

    public void AlignToWall(double DistanceFromTarget, DistanceUnit unit){
        double rightDistance = UDSRight.getDistance(unit);
        double leftDistance = UDSLeft.getDistance(unit);
        int timeoutMs = 3000; // Limit this function to a maximum of 3s
        //@todo getMaxSpeed and setMaxSpeed were removed from ftc_app SDK v3.0
        //int oldMaxSpeedRight = rightDrive.getMaxSpeed();
        //int oldMaxSpeedLeft = leftDrive.getMaxSpeed();
        //leftDrive.setMaxSpeed(250);
        //rightDrive.setMaxSpeed(250);

        if(leftDistance > rightDistance == true){
            telemetry.addData("ATW TURNING", "RIGHT");
            telemetry.addData("RANGE: ","R: %.02f, L: %.02f", rightDistance, leftDistance);
            telemetry.update();
            //Thread.yield();
            //sleepMs(3000);

            period.reset();
            leftDrive.setPower(1);
            rightDrive.setPower(-1);
            while(opModeIsActive() && (period.time() <= timeoutMs) && (leftDistance > rightDistance)){
                rightDistance = UDSRight.getDistance(unit);
                leftDistance = UDSLeft.getDistance(unit);
                telemetry.addData("RANGE: ","R: %.02f, L: %.02f", rightDistance, leftDistance);
                telemetry.update();
            }
        }
        else if (leftDistance < rightDistance == true){
            telemetry.addData("ATW TURNING", "LEFT");
            telemetry.addData("RANGE: ","R: %.02f, L: %.02f", rightDistance, leftDistance);
            telemetry.update();
            //Thread.yield();
            //sleepMs(3000);

            period.reset();
            leftDrive.setPower(-1);
            rightDrive.setPower(1);
            while(opModeIsActive() && (period.time() <= timeoutMs) && (leftDistance < rightDistance)){
                rightDistance = UDSRight.getDistance(unit);
                leftDistance = UDSLeft.getDistance(unit);
                telemetry.addData("RANGE: ","R: %.02f, L: %.02f", rightDistance, leftDistance);
                telemetry.update();
            }
        }
        else if (leftDistance == rightDistance){
            // Do Nothing - We are aligned to the wall
            telemetry.addData("TURNING", "-----");
            telemetry.addData("RANGE: ","R: %.02f, L: %.02f", rightDistance, leftDistance);
            telemetry.update();
            //Thread.yield();
            //sleepMs(3000);
        }
        rightDrive.setPower(0);
        leftDrive.setPower(0);
        //@todo getMaxSpeed and setMaxSpeed were removed from ftc_app SDK v3.0
        //leftDrive.setMaxSpeed(oldMaxSpeedLeft);
        //rightDrive.setMaxSpeed(oldMaxSpeedRight);
    }

    public void setDriveTrainPower(double power)
    {
        setDriveTrainPower(power,power);
    }

    public void setDriveTrainPower(double leftPower, double rightPower)
    {
        if(leftDrive != null) leftDrive.setPower(leftPower);
        if(rightDrive != null) rightDrive.setPower(rightPower);
    }

    public void sensorTroubleshoot(boolean wiggletest){
        int leftDrivestartpos = leftDrive.getCurrentPosition();
        int rightDrivestartpos = rightDrive.getCurrentPosition();
        double rightbeaconstartpos = beaconRight.getPosition();
        double leftbeaconstartpos = beaconLeft.getPosition();
        double gyrostartpos = gyro.getHeading();
        int leftstartblue = beaconSensorLeft.blue();
        int leftstartred = beaconSensorLeft.red();
        int rightstartred = beaconSensorRight.red();
        int rightstartblue = beaconSensorRight.blue();
        int floorsensorastart = floorSensor.alpha();
        double rightudsstart = UDSRight.getDistance(DistanceUnit.INCH);
        double leftudsstart = UDSLeft.getDistance(DistanceUnit.INCH);
        int Errors = 0;

        rightDrive.setPower(0.2);
        leftDrive.setPower(0.2);
        sleepMs(500);
        rightDrive.setPower(0);
        leftDrive.setPower(0);
        if (rightDrivestartpos != rightDrive.getCurrentPosition()){
            telemetry.addData("Right Encoder Functioning Properly", "");
        }
        else{
            telemetry.addData("Check Right Encoder", "");
            Errors ++;
        }
        if(leftDrivestartpos != leftDrive.getCurrentPosition()){
            telemetry.addData("Left Encoder Functioning Properly", "");
        }
        else {
            telemetry.addData("Check Left Encoder", "");
            Errors ++;
        }
        if (leftudsstart != UDSLeft.getDistance(DistanceUnit.INCH)){
            telemetry.addData("Left UDS Functioning Properly.", "");
        }
        else {
            telemetry.addData("Check Left UDS", "");
        }
        if (rightudsstart != UDSRight.getDistance(DistanceUnit.INCH)){
            telemetry.addData("Right UDS Functioning Properly","");
        }
        else {
            telemetry.addData("Check Right UDS", "");
        }

        beaconRight.setPosition(0.3);
        beaconLeft.setPosition(0.3);
        sleepMs(1000);
        beaconLeft.setPosition(leftbeaconstartpos);
        beaconRight.setPosition(rightbeaconstartpos);

        leftDrive.setPower(0.5);
        rightDrive.setPower(-0.5);
        sleepMs(500);
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        if (gyro.getHeading() != gyrostartpos){
            telemetry.addData("Gyro Function Correctly", "");
        }
        else {
            telemetry.addData("Check Gyro Sensor", "");
            Errors ++;
        }


        telemetry.update();
        sleepMs(10000);

        if (wiggletest == true){
            leftDrive.setPower(1);
            sleepMs(500);
            leftDrive.setPower(0);
            sleepMs(1000);
            rightDrive.setPower(1);
            sleepMs(500);
            rightDrive.setPower(0);
            sleepMs(1000);
            beaconLeft.setPosition(1);
            sleepMs(500);
            beaconLeft.setPosition(0);
            sleepMs(1000);
            beaconRight.setPosition(1);
            sleepMs(500);
            beaconRight.setPosition(0);
            sleepMs(1000);
            leftBallLauncher.setPower(1);
            sleepMs(500);
            leftBallLauncher.setPower(0);
            sleepMs(1000);
            rightBallLauncher.setPower(1);
            sleepMs(500);
            rightBallLauncher.setPower(0);
            sleepMs(1000);
            capBall.setPower(-1);
            sleepMs(250);
            capBall.setPower(1);
            sleepMs(250);
            capBall.setPower(0);
            sleepMs(1000);
        }


    }


//// This function is currently disabled.
    public void gyroDrive ( double speed,
                            double distance,
                            double angle) {

        int     newLeftTarget;
        int     newRightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;}


        // Ensure that the opmode is still active
//        if (opModeIsActive()) {
//
//            // Determine new target position, and pass to motor controller
//            moveCounts = (int)(distance * COUNTS_PER_INCH);
//            newLeftTarget = leftDrive.getCurrentPosition() + moveCounts;
//            newRightTarget = rightDrive.getCurrentPosition() + moveCounts;
//
//            // Set Target and Turn On RUN_TO_POSITION
//            leftDrive.setTargetPosition(newLeftTarget);
//            rightDrive.setTargetPosition(newRightTarget);
//
//            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            // start motion.
//            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
//            leftDrive.setPower(speed);
//            rightDrive.setPower(speed);
//
//            // keep looping while we are still active, and BOTH motors are running.
//            while (opModeIsActive() &&
//                    (leftDrive.isBusy() && rightDrive.isBusy())) {
//
//                error = getError(angle);
//                steer = getSteer(error, P_DRIVE_COEFF);
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
//                leftDrive.setPower(leftSpeed);
//                rightDrive.setPower(rightSpeed);
//
//                // Display drive status for the driver.
//                //telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
//                //telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
//                //telemetry.addData("Actual",  "%7d:%7d",      robot.leftDrive.getCurrentPosition(),
//                        rightDrive.getCurrentPosition();
//                //telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
//                //telemetry.update();
//            }
//
//            // Stop all motion;
//            leftDrive.setPower(0);
//            rightDrive.setPower(0);
//
//            // Turn off RUN_TO_POSITION
//            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//    }

    public boolean doIseeBlueLeft () {
        return (beaconSensorLeft.blue() >= 5);
    }

    public boolean doIseeRedLeft () {
        return (beaconSensorLeft.red() >= 5);
    }

    public boolean doIseeBlueRight () {
        return (beaconSensorRight.blue() >= 5);
    }

    public boolean doIseeRedRight () {
        return (beaconSensorRight.red() >= 5);
    }

    public void stop()
    {
        setCapBallMotorPower(0,CapBallState.OFF);
        setSweeperPower(0,SweeperDirection.IN);
        setLauncherState(LauncherState.OFF);
        setDriveTrainPower(0);
    }

    public void turnToAbsoluteHeading(double speed, double absoluteHeading, int timeoutMs) {
        simpleGyroTurn(speed,absoluteHeading-gyro.getIntegratedZValue(),timeoutMs);
    }

    // Right/Clockwise = Positive Turn Degrees
    // Left/CounterClockwise = Negative Turn Degrees
    // timeoutMs provides a sanity check to make sure we don't turn forever
    public void simpleGyroTurn(double speed, double turnDegrees, int timeoutMs)
    {
        // Our turn by degrees algorithm has a bit of slop in it.
        // Try to correct the user input to match reality
        // If turnDegrees == 100, then reset to 90 (which makes our robot turn 100)
        //turnDegrees *= .90;

        int currentHeading = gyro.getIntegratedZValue();
        double targetHeading = currentHeading - turnDegrees;

        // Early Return (do nothing) if we are giving a zero degree turn
        if(turnDegrees == 0) return;

        // Start our timeout-detection timer
        period.reset();

        if(turnDegrees > 0) {
            setDriveTrainPower(speed,-speed);
            while ((period.time() < timeoutMs) && opModeIsActive()) {
                currentHeading = gyro.getIntegratedZValue();
                // Exit our while loop because we're at our destination
                if((currentHeading - targetHeading) <= 0) break;
            }
        }
        else {
            setDriveTrainPower(-speed,speed);
            while ((period.time() < timeoutMs) && opModeIsActive()) {
                currentHeading = gyro.getIntegratedZValue();
                // Exit our while loop because we're at our destination
                if((targetHeading - currentHeading) <= 0) break;
             }
        }
        setDriveTrainPower(0);
    }

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            //telemetry.update();
        }
    }

    public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            //telemetry.update();
        }

        // Stop all motion;
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        leftDrive.setPower(leftSpeed);
        rightDrive.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }



    public void DriveToWhiteLine(double power, int intensity, boolean enableLed, int timeoutMs){
        setDriveTrainPower(power);
        floorSensor.enableLed(enableLed);
        period.reset();
        while (opModeIsActive()){
            int currentIntensity = floorSensor.alpha();
            telemetry.addData("Alpha: ", "%03d", currentIntensity);

            // Break out of the loop if we found the line
            if(currentIntensity >= intensity) break;

            // Break out of the loop if we have timed out
            if(period.time() > timeoutMs) break;
        }

        setDriveTrainPower(0);
        floorSensor.enableLed(false);
    }

    /* Constructor */
    public InvadersVelocityVortexBot(){

    }

    public void  ohshoot()
    {
        // turn on the shooter wheels
        setLauncherState(InvadersVelocityVortexBot.LauncherState.ON);

        //three second delay
        sleepMs(3000);

        //turn on the elevator
        setBallElevator(1,BallElevatorState.UP);

        // 3 second delay
        sleepMs(3000);

        // turn on the sweeper to load second shot (elevator is still on)
        setSweeperPower(1,SweeperDirection.IN);
        sleepMs(6000);

        //turn everything off
        setBallElevator(0,BallElevatorState.OFF);
        setSweeperPower(0,SweeperDirection.IN);
        setLauncherState(LauncherState.OFF);
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
        leftDrive   = hwMap.dcMotor.get("backLeft");
        rightDrive  = hwMap.dcMotor.get("backRight");
        rightBallLauncher = hwMap.dcMotor.get("RightLauncher");
        leftBallLauncher = hwMap.dcMotor.get("LeftLauncher");
        capBall = hwMap.dcMotor.get("CapBall");
        sweeper = hwMap.dcMotor.get("Sweeper");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBallLauncher.setDirection(DcMotor.Direction.FORWARD);
        leftBallLauncher.setDirection(DcMotor.Direction.REVERSE);
        capBall.setDirection(DcMotorSimple.Direction.REVERSE);
        sweeper.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all motors to zero power
        setDriveTrainPower(0);
        setCapBallMotorPower(0, CapBallState.UP);
        setSweeperPower(0,SweeperDirection.IN);
        setLauncherState(LauncherState.OFF);

        // Set all non-driving motors to run without encoders.
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBallLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        capBall.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define installed servos
        beaconLeft = hwMap.servo.get("beaconLeft");
        beaconRight = hwMap.servo.get("beaconRight");
        ballElevator = hwMap.crservo.get("BallElevator");
        ballElevator.setPower(0.0);

        // Define our sensors
        touchSensor = hwMap.touchSensor.get("downLimit");
        UDSLeft = hwMap.get(ModernRoboticsI2cRangeSensor.class, "UDSLeft");
        UDSRight = hwMap.get(ModernRoboticsI2cRangeSensor.class, "UDSRight");
        beaconSensorLeft = hwMap.colorSensor.get("beaconSensorLeft");
        beaconSensorRight = hwMap.colorSensor.get("beaconSensorRight");
        floorSensor = hwMap.colorSensor.get("floorSensor");

        // Custom I2C Addresses Go Here!
        ///@todo Need to validate that UDSLeft.setI2cAddress is working with new SDK.  Hasn't been tested since VelocityVortex championship
        UDSLeft.setI2cAddress(I2cAddr.create8bit(0x26));
        floorSensor.setI2cAddress(I2cAddr.create8bit(0x3A));
        beaconSensorRight.setI2cAddress(I2cAddr.create8bit(0x36));

        // Initialize Color Sensor LEDs to off
        beaconSensorLeft.enableLed(false);
        beaconSensorRight.enableLed(false);
        floorSensor.enableLed(false);

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

//Cold pizza normally.
    public enum CapBallState {
        UP,
        DOWN,
        OFF
    }

    public void setCapBallMotorPower(double power, CapBallState state)
    {
        // Early return if our config doesn't have a CapBall
        if(capBall == null) return;

        switch(state) {
            case UP:
                capBall.setPower(Math.abs(power));
                break;
            case DOWN:
                capBall.setPower(-Math.abs(power));
                break;
            case OFF:
                capBall.setPower(0);
                break;
        }
    }

    public enum BallElevatorState {
        UP,
        DOWN,
        OFF
    }

    public void setBallElevator(double power, BallElevatorState state)
    {
        // Early return if we don't have a ball elevator
        if(ballElevator == null) return;

        //@todo Write to a file what we're about to do to the motor here
        switch(state) {
            case UP:
                // Set positive value regardless of power positive/negative
                ballElevator.setPower(-Math.abs(power));
                break;
            case DOWN:
                // Set negative value regardless of power positive/negative
                ballElevator.setPower(Math.abs(power));
                break;
            case OFF:
                ballElevator.setPower(0);
                break;
        }
    }

    public enum LauncherState {
        ON,
        OFF
    }

    public void setLauncherState(LauncherState onOrOff){
        double power = 0; // Initialize power to OFF condition
        // If the onOrOff parameter equals ON, then set the power
        if(onOrOff == LauncherState.ON)
        {
            // Launchers are always at full power
            power = -1;
        }
        if(rightBallLauncher != null) rightBallLauncher.setPower(power);
        if(leftBallLauncher != null) leftBallLauncher.setPower(power);
    }

    public enum SweeperDirection {
        IN,
        OUT
    }

    public void setSweeperPower(float power, SweeperDirection direction) {
        if(sweeper != null) {
            if(direction == SweeperDirection.IN) {
                sweeper.setPower(-Math.abs(power));
            }
            else {
                sweeper.setPower(Math.abs(power));
            }
        }
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
            newLeftTarget = leftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            period.reset();
            setDriveTrainPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (period.seconds() < timeoutS) &&
                    (leftDrive.isBusy() && rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftDrive.getCurrentPosition(),
                        rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            setDriveTrainPower(0);

            // Turn off RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleepMs(250);   // optional pause after each move
        }
    }

    /*
 *  Method to perfmorm a relative move, based on encoder counts.
 *  Encoders are not reset as the move is based on the current position.
 *  Move will stop if any of three conditions occur:
 *  1) Move gets to the desired position
 *  2) Move runs out of time
 *  3) Driver stops the opmode running.
 */
    public void timedDrive(double speed, double durationMs) {
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            setDriveTrainPower(speed);
            period.reset();
            while((period.time() < durationMs) && opModeIsActive()) {
            }
            // Stop all motion;
            setDriveTrainPower(0);

            sleepMs(250);
        }
    }
}
