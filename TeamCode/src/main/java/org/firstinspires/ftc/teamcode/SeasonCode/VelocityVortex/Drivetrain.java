package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import android.util.Log;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * <p>
 *      Here's our drivetrain. Things pertaining solely to the drivetrain go here.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * <p>
 *      Hardware checklist: <br>
 *      1. 4 Motors <br>
 *      2. A gyroscopic sensor <br>
 *      3. A color sensor <br>
 *      4. A range sensor <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class Drivetrain
{
    /**
     * Internal class of VVDriveTrain that manages the drivetrain's power.
     */
    final static class Power
    {
        // Power values
        double drive;
        double strafe;
        double rotate;

        private boolean _isReverse;             // Used to toggle reversal of drivetrain


        /**
         * Constructor- Takes nothing and sets the powers to 0
         */
        Power()
        {
            drive = 0;
            strafe = 0;
            rotate = 0;

            _isReverse = false;
        }


        /**
         * @return Returns whether the power is reversed or not
         */
        final boolean isReversed() {
            return _isReverse;
        }


        /**
         * Toggles the muting of the robot's drivetrain. This allows for easier slow movements.
         */
        final void slow()
        {
            final double SLOW_MULTIPLIER = .6;      // Multiplier used to slow the robot down

            drive *= SLOW_MULTIPLIER;
            strafe *= SLOW_MULTIPLIER;
            rotate *= SLOW_MULTIPLIER;
        }


        /**
         * Toggles the reversal of the robot's drive and strafe values. This is because the
         * harvester faces forward while the shooter faces backwards. Only use when gyro drive is
         * off.
         */
        final void reverse()
        {
            drive *= -1;
            strafe *= -1;
        }
    }


    private Robot _robot = null;               // Robot we're working with

    // The motors in our drivetrain.
    private DcMotor _motorFL;                   // Front left motor
    private DcMotor _motorBL;                   // Back left motor
    private DcMotor _motorFR;                   // Front right motor
    private DcMotor _motorBR;                   // Back right motor

    boolean useAntiRotate = false;              // Whether to use the anti rotate-lock

    SensorGyro gyro;                            // Modern Robotics gyro
    SensorColor frontColor;                     // Modern Robotics color
    SensorColor backColor;                      // Another color sensor
    SensorColor lineColor;                      // Yet another color sensor, this one on the bottom
    SensorRange range;                          // Modern Robotics range


    /**
     * Powering the drivetrain has two modes, scaling power and not scaling power. This enumeration
     * helps make it clear what exactly the drivetrain power function wants.
     */
    enum DrivetrainMode
    {
        SCALED,
        NOT_SCALED
    }


    /**
     * Constructor- Takes in a VVRobot and copies it. This is to ensure that we're getting a
     * hardware mapped robot.
     *
     * @param ROBOT A VVRobot to copy
     */
    Drivetrain(final Robot ROBOT)
    {
        _robot = ROBOT;
    }


    /**
     * Maps the drivetrain
     */
    void mapDrivetrain()
    {
        HardwareAux mapHelper = new HardwareAux(_robot);

        _motorFL = mapHelper.mapMotor("fl" , DcMotorSimple.Direction.REVERSE);
        _motorBL = mapHelper.mapMotor("bl" , DcMotorSimple.Direction.REVERSE);
        _motorFR = mapHelper.mapMotor("fr" , DcMotorSimple.Direction.FORWARD);
        _motorBR = mapHelper.mapMotor("br" , DcMotorSimple.Direction.FORWARD);

        setEncoderRunUsingEncoders();
    }


    /**
     * Maps the sensors
     */
    void mapSensors()
    {
        // Gyroscope
        gyro = new SensorGyro(_robot);
        gyro.mapGyro("gyro" , ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN);

        frontColor = new SensorColor(_robot);
        frontColor.mapColor("frontColor" , 0x7c);

        backColor = new SensorColor(_robot);
        backColor.mapColor("backColor" , 0x6c);

        lineColor = new SensorColor(_robot);
        lineColor.mapColor("lineColor" , 0x5c);

        range = new SensorRange(_robot);
        range.mapRange("range");
    }


    /**
     * Set run mode to RUN_USING_ENCODER.
     */
    void setEncoderRunUsingEncoders()
    {
        try
        {
            _motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            _motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            _motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            _motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(NullPointerException e)
        {
            Log.e("Error" , "Cannot set \"RUN_USING_ENCODER\", check your mapping");
        }
    }


    private void resetEncoders()
    {
        try
        {
            _motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            _motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            _motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            _motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot reset encoders, check your mapping");
        }
    }


    /**
     * Sets the needed encoder counts for each wheel given their powers and a distance to travel.
     *
     * @param distance The distance needed for the robot as a whole to travel.
     */
    private boolean setEncoderPos(double distance)
    {
        final int ENCODER_COUNTS = 1_120;                           // Counts in 1 rotation
        final double WHEEL_DIAMETER = 5.08;                         // Diameter of wheel (cm)
        final double WHEEL_CIRCUM = WHEEL_DIAMETER * Math.PI;       // Circumference of wheel (cm)

        boolean success = true;             // Tells whether setting encoders is successful or not

        /*
            The encoder counts needed for each wheel.
            When mecanum wheels move in an off-direction (angle not a multiple of 45),
            each wheel moves at a different speed. That's why we need separate variables.
         */
        int frontLeftEncoder;
        int backLeftEncoder;
        int frontRightEncoder;
        int backRightEncoder;

        double avgMotion;               // Average speed of the motors
        int avgCounts;                  // Average number of encoder counts to use

        try
        {
            /*
                With 45 degree rollers, the strafe distance on Mecanum wheels is the same
                as normal forward and backward distance(theoretically). Because the wheels move at
                different speeds, I opted to average them to find correct distance.
             */
            avgMotion = Math.abs((_motorFL.getPower()) + Math.abs(_motorBL.getPower()) +
                    Math.abs(_motorFR.getPower()) + Math.abs(_motorBR.getPower())) / 4;


            // Calculate the average number of counts for the motors.
            avgCounts = (int)(distance / WHEEL_CIRCUM * ENCODER_COUNTS);


            // Set the individual counts.
            frontLeftEncoder = (int)(avgCounts * _motorFL.getPower() / avgMotion);
            backLeftEncoder = (int)(avgCounts * _motorBL.getPower() / avgMotion);
            frontRightEncoder = (int)(avgCounts * _motorFR.getPower() / avgMotion);
            backRightEncoder = (int)(avgCounts * _motorBR.getPower() / avgMotion);

            // Set the encoder values to the motors
            _motorFL.setTargetPosition(frontLeftEncoder);
            _motorBL.setTargetPosition(backLeftEncoder);
            _motorFR.setTargetPosition(frontRightEncoder);
            _motorBR.setTargetPosition(backRightEncoder);
        }
        catch(NullPointerException e)
        {
            Log.e("Error" , "Cannot set encoder target positions, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Handles powering the drivetrain, given a value to drive in, a value to strafe in,
     * and a value to rotate. Adjusted for mecanum wheels.
     *
     * @param power Object that holds robot powers
     * @param MODE  Mode to operate drivetrain in (scaled or not scaled)
     * @return Returns true if the operation was successful, false otherwise
     */
    boolean drive(Drivetrain.Power power, final Drivetrain.DrivetrainMode MODE)
    {
        boolean success = true;         // Holds whether the action is successful or not

        // We're using mecanum wheels, so the power is not directly proportional to the
        // joystick input. We have to add and subtract things first.
        double powerFL;
        double powerBL;
        double powerFR;
        double powerBR;

        // The operators between the variables might change
        // based on the way the wheels are installed
        powerFL = power.drive + power.strafe + power.rotate;
        powerBL = power.drive - power.strafe + power.rotate;
        powerFR = power.drive - power.strafe - power.rotate;
        powerBR = power.drive + power.strafe - power.rotate;

        if (MODE == Drivetrain.DrivetrainMode.SCALED)
        {
            powerFL = UtilBasic.scaleValue(powerFL);
            powerBL = UtilBasic.scaleValue(powerBL);
            powerFR = UtilBasic.scaleValue(powerFR);
            powerBR = UtilBasic.scaleValue(powerBR);
        }

        // Set power
        try
        {
            // Process of powering on is staggered between sides in a sort of X-config. This is so
            // that minor delays don't end up rotating the robot too much

//            if(useAntiRotate)
//                antiRotateLock(gyro.heading());

            _motorFL.setPower(powerFL);
            _motorBR.setPower(powerBR);
            _motorFR.setPower(powerFR);
            _motorBL.setPower(powerBL);
        }
        catch(Exception e)
        {
            Log.e("Error", "Cannot power drivetrain, perhaps there's a mapping issue.");
            success = false;
        }

        return success;
    }


    /**
     * Calculates robot orientation and makes moving easier by dynamically shifting where the
     * "front" of the robot is.
     * <br>
     * <p>
     * With driver assist, forward is always away from you, no matter which direction
     * the robot is oriented. In fact, that applies to all directions. the robot will move
     * the way the joystick is pointed, no matter its orientation. Maybe you won't have
     * to mentally rotate the field next time when you're driving. Or maybe you would
     * prefer that because it's "what we've always done".
     * </p>
     * <br>
     * <p>
     * If you don't like it, you'll:
     * 1. Hurt my feelings
     * 2. Toggle it off (or don't toggle it on in the first place)
     * </p>
     *
     * @param power Object that holds robot powers
     * @param MODE  Mode to operate drivetrain in (scaled or not scaled)
     *
     * @return Returns true if the operation was successful, false otherwise
     */
    boolean gyroDrive(Drivetrain.Power power, final Drivetrain.DrivetrainMode MODE)
    {
        // Object used to hold the coordinates of the x and y values returned from the joystick
        UtilPoint joyStickCoord = new UtilPoint(power.strafe, power.drive,
                UtilPoint.Type.CARTESIAN);

        int heading;            // Heading we get from the gyro

        boolean success = true; // Holds whether or not this method is successful

        // Get our gyro heading
        heading = gyro.heading();

        if (heading == -1)
            success = false;

        // Set the gyro heading as the new forward, so add joystick angle
        // Subtract from 90 because forward is 90 degrees on the joystick
        joyStickCoord.setTheta(90 - heading + joyStickCoord.theta());

        // Set values
        power.strafe = joyStickCoord.x();
        power.drive = joyStickCoord.y();

        success = success && drive(power, MODE);

        return success;
    }


    /**
     * Turns the robot with a rudimentary control system
     *
     * @param TARGET Target angle between 0 and 360 degrees
     *
     * @return True if attempt to turn was successful or not attempted, false otherwise
     */
    boolean turnTo(final int TARGET , final double MAX_SPEED)
    {
        final int TOLERANCE = 3;                                    // "Close enough" amount
        final double MIN_SPEED = .06;                               // Minimum speed to rotate at
        final double SPEED_MULTIPLIER = 10;                         // Constant to adjust speed

        boolean success = true;                                     // Tells whether turning was
                                                                    // successful or not

        int initHeading = _robot.drivetrain.gyro.heading();         // Initial heading of the robot
        double speed;                                               // Speed of rotation
        double error = UtilBasic.angleError(initHeading , TARGET);  // Error between init and target
        double initError = error;                                   // Initial error
        double distanceModifier = Math.abs(error) / 180;            // Distance modifier, turning
                                                                    // should be slower when there's
                                                                    // less distance to cover

        while(Math.abs(error) > TOLERANCE)
        {
            try
            {
                error = UtilBasic.angleError(_robot.drivetrain.gyro.heading() , TARGET);

                speed = error / initError * SPEED_MULTIPLIER * distanceModifier;

                if(speed < MIN_SPEED)
                    speed = MIN_SPEED;

                if(speed > MAX_SPEED)
                    speed = MAX_SPEED;

                _robot.drivePower.drive = 0;
                _robot.drivePower.strafe = 0;
                _robot.drivePower.rotate = Math.abs(error) / error * -1 * speed;
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
            }
            catch(Exception e)
            {
                Log.e("Error" , "Cannot turn drivetrain, check your mapping");
                success = false;
            }
        }

        return success;
    }


    /**
     * @// FIXME: 2/25/2017 Overshoots sometimes, undershoots sometimes, just right sometimes
     *
     * Drives to a specified distance measured by encoders
     *
     * @param distance The distance to drive to
     * @param heading The heading/direction in which to travel
     * @param speed The speed at which to travel
     *
     * @return True if attempt to drive is successful, false otherwise
     */
    boolean driveTo(final double distance , final double heading , final double speed)
    {
        // Create a new point to facilitate with cartesian/polar conversions
        UtilPoint myPoint = new UtilPoint(speed , heading , UtilPoint.Type.POLAR);

        boolean keepDriving = true;         // Determines whether or not to keep driving
        boolean success = true;             // Determines whether driving was successful or not

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;
        _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);

        resetEncoders();
        setEncoderRunUsingEncoders();
        setEncoderPos(distance);

        // While any of the motors are doing things, keep setting power
        while(keepDriving)
        {
            if(Math.abs(_motorFL.getCurrentPosition()) >= Math.abs(_motorFL.getTargetPosition()) &&
               Math.abs(_motorBL.getCurrentPosition()) >= Math.abs(_motorBL.getTargetPosition()) &&
               Math.abs(_motorFR.getCurrentPosition()) >= Math.abs(_motorFR.getTargetPosition()) &&
               Math.abs(_motorBR.getCurrentPosition()) >= Math.abs(_motorBR.getTargetPosition()))
                keepDriving = false;

            try
            {
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
            }
            catch(Exception e)
            {
                Log.e("Error" , "Cannot power drivetrain, check your mapping");
                success = false;
            }
        }

        return success;
    }


    /**
     * Drives until the range sensor reads a value less than or equal to the distance specified
     * in the arguments
     *
     * @param distance The target distance for the range sensor to pick up
     * @param heading The heading/direction to drive at
     * @param speed The speed at which to drive at
     */
    boolean driveToDistance(final double distance , final double heading , final double speed)
    {
        // Create a new point to facilitate with cartesian/polar conversions
        UtilPoint myPoint = new UtilPoint(speed , heading , UtilPoint.Type.POLAR);

        boolean success = true;         // Determines whether driving is successful or not

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(_robot.drivetrain.range.distance(DistanceUnit.CM) > distance)
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);

        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot drive to a range, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Drives in a certain direction until the color sensor picks up the specified color
     *
     * @param HEADING The direction to drive in
     * @param SPEED The speed to drive at
     * @param COLOR The target color that determines when the robot should stop
     * @param SENSOR The color sensor used to detect the color
     *
     * @return True if attempt to drive successful, false otherwise
     */
    boolean driveToColor(final double HEADING , final double SPEED , final SensorColor.Color COLOR ,
                         final SensorColor SENSOR)
    {
        // Create a new point to facilitate with cartesian/polar conversions
        UtilPoint myPoint = new UtilPoint(SPEED , HEADING , UtilPoint.Type.POLAR);

        boolean success = true;         // Determines whether driving is successful or not

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(SENSOR.getColor() != COLOR)
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Drive to color failed, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Drives in a certain direction until the color sensor picks up light
     *
     * @param HEADING The direction at which to travel at
     * @param SPEED The speed to travel at
     *
     * @return True if attempt to drive successful, false otherwise
     */
    boolean driveToLight(final double HEADING , final double SPEED , final SensorColor SENSOR)
    {
        // Create a new point to facilitate with cartesian/polar conversions
        UtilPoint myPoint = new UtilPoint(SPEED , HEADING , UtilPoint.Type.POLAR);

        boolean success = true;         // Determines whether driving is successful or not

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(SENSOR.getColor() == SensorColor.Color.UNKNOWN)
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Drive to color failed, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Drives in a certain direction for a certain time
     *
     * @param HEADING The direction at which to drive in
     * @param SPEED The speed at which to drive at
     * @param TIME The time to drive for
     *
     * @return True if attempt to drive successful, false otherwise
     */
    boolean driveForTime(final double HEADING , final double SPEED , final double TIME)
    {
        // Create a new point to facilitate with cartesian/polar conversions
        UtilPoint myPoint = new UtilPoint(SPEED , HEADING , UtilPoint.Type.POLAR);

        long initialTime = System.currentTimeMillis();

        boolean success = true;         // Determines whether driving is successful or not

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(System.currentTimeMillis() < initialTime + TIME)
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Drive to color failed, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Drives in a certain direction until the robot is on a line
     *
     * @param HEADING The direction at which to drive in
     * @param SPEED The speed at which to drive at
     *
     * @return True if attempt successful, false otherwise
     */
    boolean driveToLine(final double HEADING, final double SPEED)
    {
        UtilPoint myPoint = new UtilPoint(SPEED , HEADING , UtilPoint.Type.POLAR);

        boolean success = true;

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(_robot.drivetrain.lineColor.getColor() != SensorColor.Color.WHITE)
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Drive to line failed");
            success = false;
        }

        return success;
    }


    /**
     * Drives in a certain direction until the robot is on a line
     *
     * @param HEADING The direction at which to drive in
     * @param SPEED The speed at which to drive at
     * @param TIMEOUT The amout of time to travel before calling quits and returning false
     *
     * @return True if attempt successful, false otherwise
     */
    boolean driveToLine(final double HEADING, final double SPEED , final int TIMEOUT)
    {
        UtilPoint myPoint = new UtilPoint(SPEED , HEADING , UtilPoint.Type.POLAR);
        UtilPulsar timer = new UtilPulsar();

        boolean success = true;

        _robot.drivePower.drive = myPoint.y();
        _robot.drivePower.strafe = myPoint.x();
        _robot.drivePower.rotate = 0;

        try
        {
            while(_robot.drivetrain.lineColor.getColor() != SensorColor.Color.WHITE)
            {
                _robot.drivetrain.drive(_robot.drivePower , DrivetrainMode.NOT_SCALED);

                if(timer.pulse(TIMEOUT))
                {
                    success = false;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            Log.e("Error" , "Drive to line failed");
            success = false;
        }

        return success;
    }


    /**
     * Control system, helps to mitigate some slippage in the form of unintended rotation. Call when
     * robot rotation is not desired.
     *
     * @param TARGET The target angle to lock to
     */
    void antiRotateLock(final int TARGET)
    {
        final int SPEED_CONSTANT = 6;       // Constant used to fine tune controller
        final int CLOSE_ENOUGH = 5;         // Some slack so that the controller isn't too
                                            // overzealous

        double distanceModifier;
        int error = UtilBasic.angleError(gyro.heading() , TARGET);


        if(gyro.heading() > TARGET + CLOSE_ENOUGH || gyro.heading() < TARGET - CLOSE_ENOUGH)
        {
            distanceModifier = Math.abs(error) / 180;

            _robot.drivePower.rotate = -Math.abs(error) / error * distanceModifier * SPEED_CONSTANT;
        }
        else
            _robot.drivePower.rotate = 0;
    }


    /**
     * Attempts to stop the drivetrain
     *
     * @return True if attempt successful, false otherwise
     */
    boolean stop()
    {
        boolean success = true;         // Tells if attempt to stop drivetrain is successful or not

        try
        {
            _motorFL.setPower(0);
            _motorBL.setPower(0);
            _motorFR.setPower(0);
            _motorBR.setPower(0);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot stop drivetrain, check your mapping");
            success = false;
        }

        return success;
    }
}