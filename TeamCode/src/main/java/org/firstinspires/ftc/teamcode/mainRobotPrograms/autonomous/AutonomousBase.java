package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mainRobotPrograms.RobotBase;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class AutonomousBase extends RobotBase
{
    /******** SENSOR STUFF ********/
    /**** Gyro ****/
    protected GyroSensor gyroscope;
    protected int desiredHeading = 0; //Massively important to maintaining stability through the drives.

    //Just resets the gyro.
    protected void zeroHeading() throws InterruptedException
    {
        sleep(400);
        gyroscope.resetZAxisIntegrator();
        sleep(400); //Resetting gyro heading has an annoying tendency to not actually zero, which is kinda annoying but not much can be done about it.
    }
    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.  This method makes sure that the value makes sense for calculations.
    protected int gyroAdjustFactor; //Changed based on swerves.
    protected int getValidGyroHeading()
    {
        //Get the heading.
        int heading = gyroscope.getHeading ();

        //Determine the actual heading on a logical basis (which makes sense with the calculations).
        if (heading > 180 && heading < 360)
            heading -= 360;

        //What this does is enable the 180 degree turn to be effectively made without resulting in erratic movement.
        if (desiredHeading > 160 && heading < 0)
            heading += 360;
        else if (desiredHeading < -160 && heading > 0)
            heading -= 360;

        heading += gyroAdjustFactor;

        return heading;
    }

    /**** Color Sensors (3) ****/
    protected ColorSensor option1ColorSensor, option2ColorSensor, bottomColorSensor; //Must have different I2C addresses.
    protected boolean option1Red, option2Red, option1Blue, option2Blue;
    protected void updateColorSensorStates()
    {
        //Threshold is currently 2, but this could be changed.
        option1Red = option1ColorSensor.red () >= 2;
        option1Blue = option1ColorSensor.blue () >= 2;
        option2Red = option2ColorSensor.red () >= 2;
        option2Blue = option2ColorSensor.blue () >= 2;
    }

    /**** Encoders ****/
    //Since this method takes a half-second or so to complete, try to run it as little as possible.
    protected void initializeAndResetEncoders()
    {
        //Required before any encoder values are examined.  Super important and afforded variable times.
        boolean initializedSuccessfully = false;
        int additionalTime = 0;
        while (!initializedSuccessfully)
        {
            try
            {
                //This set of instructions is MASSIVELY important.  The RUN_WITHOUT_ENCODER mode doesn't actually make the thing not use encoders, it just prevents the encoders from directly regulating motor powers.  Weird names for the RunMode options by the FTC folks.
                for (DcMotor motor : leftDriveMotors)
                    motor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
                for (DcMotor motor : rightDriveMotors)
                    motor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
                sleep (150 + additionalTime); //Take a short break after each step, since the SDK we are using is not synchronous (it messed up our workflow)
                initializedSuccessfully = true;
            } catch (Exception e)
            {
                outputNewLineToDrivers ("Error in RUN_USING_ENCODERS!");
                additionalTime += 100;
            }
        }

        initializedSuccessfully = false;
        additionalTime = 0;
        while (!initializedSuccessfully)
        {
            try
            {
                //This HAS to have "RUN_USING_ENCODER" before it for some reason, or it just hangs forever.
                for (DcMotor motor : leftDriveMotors)
                    motor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                for (DcMotor motor : rightDriveMotors)
                    motor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                sleep (150 + additionalTime);
                initializedSuccessfully = true;
            }
            catch (Exception e)
            {
                outputNewLineToDrivers ("Error in STOP_AND_RESET_ENCODERS!");
                additionalTime += 100;
            }
        }

        initializedSuccessfully = false;
        additionalTime = 0;
        while (!initializedSuccessfully)
        {
            try
            {
                //This prevents the encoders from trying to regulate the motors on their own (they aren't qualified for that sort of work!), and affecting the gyro.
                for (DcMotor motor : leftDriveMotors)
                    motor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                for (DcMotor motor : rightDriveMotors)
                    motor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                sleep (80 + additionalTime);
                initializedSuccessfully = true;
            }
            catch (Exception e)
            {
                outputNewLineToDrivers ("Error in RUN_WITHOUT_ENCODER!");
                additionalTime += 100;
            }
        }

        lastCheckTime = System.currentTimeMillis ();
        previousDistance = 0;
    }
    protected int getDistanceDriven ()
    {
        return (int) ((leftDriveMotors.get(1).getCurrentPosition () + rightDriveMotors.get (1).getCurrentPosition ()) / 2.0);
    }

    /**** Range Sensor(s) ****/
    protected ModernRoboticsI2cRangeSensor frontRangeSensor;
    protected void driveUntilDistanceFromObstacle (double stopDistance) throws InterruptedException
    {
        //Required variables.
        double lastValidDistance = 150;
        //Linear trend downwards as we approach the obstacle.
        double driveCoefficient = 0.004, driveIntercept = 0.30;
        //Store this in case the increase() method changes the move power.
        double initialMovementPower = movementPower;

        double distanceFromStop = lastValidDistance;
        while (distanceFromStop > 0)
        {
            //Scrub the input to get a valid result (255 is the default invalid value of the range sensors).
            double perceivedDistanceFromObstacle = frontRangeSensor.cmUltrasonic ();
            if (perceivedDistanceFromObstacle >= 255)
                perceivedDistanceFromObstacle = lastValidDistance;
            else
                lastValidDistance = perceivedDistanceFromObstacle;

            //Calculate the distance until stopping.
            distanceFromStop = perceivedDistanceFromObstacle - stopDistance;

            //Calculate the new movement power based on this result.
            //The (movement power - initial movement power) expression incorporates encoder adjustments in the event that the bot is not moving.
            movementPower = driveCoefficient * distanceFromStop + driveIntercept + (movementPower - initialMovementPower);

            //Increase movement power if NECESSARY.
            increaseMovementPowerIfMovingTooSlowly ();

            //Adjust the movement power based on the gyro sensor.
            adjustMotorPowersBasedOnGyroSensor ();

            outputConstantDataToDrivers (
                    new String[]
                            {
                                    "Dist until stop: " + distanceFromStop,
                                    "Movement power: " + movementPower
                            }
            );
        }

        stopDriving ();
    }

    /******** INITIALIZATION ********/
    //Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //Initialize color sensors.
        bottomColorSensor = initialize(ColorSensor.class, "Bottom Color Sensor");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x3c));
        bottomColorSensor.enableLed(true);
        option1ColorSensor = initialize(ColorSensor.class, "Option 1 Color Sensor");
        option1ColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        option1ColorSensor.enableLed(false);
        option2ColorSensor = initialize(ColorSensor.class, "Option 2 Color Sensor");
        option2ColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        option2ColorSensor.enableLed(false);

        //Initialize the range sensors for autonomous.
        frontRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor");
        frontRangeSensor.setI2cAddress(I2cAddr.create8bit(0x90));
        //The range sensors are odd and often return .269 with this method unless the robot is restarted.
        if (frontRangeSensor.getDistance(DistanceUnit.CM) < 1.0)
            outputNewLineToDrivers("Front range sensor misconfigured!");

        //initialize gyroscope (will output whether it was found or not.
        gyroscope = initialize(GyroSensor.class, "Gyroscope");
        if (gyroscope != null)
        {
            //Start gyroscope calibration.
            outputNewLineToDrivers("Gyroscope Calibrating...");
            gyroscope.calibrate();

            //Pause to prevent odd errors in which it says it's configured but is actually LYING.
            sleep(1000);

            while (gyroscope.isCalibrating())
                sleep(50);

            zeroHeading();

            outputNewLineToDrivers("Gyroscope Calibration Complete!");
        }
    }

    /******** CHILD CLASS INHERITANCE ********/
    //All child classes should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;

    /******** MOVEMENT POWER CONTROL ********/
    //Used to set drive move power initially.
    protected double movementPower = 0;
    protected void setMovementPower (double movementPower)
    {
        this.movementPower = movementPower;

        setLeftPower (movementPower);
        setRightPower (movementPower);
    }
    //Stops all drive motors.
    protected void stopDriving ()
    {
        setLeftPower(0);
        setRightPower(0);
    }

    /******** SENSOR ADJUSTMENT ********/
    //Method that adjusts the heading based on the gyro heading and logarithmic mathematics.  Called once per idle().
    protected void adjustMotorPowersBasedOnGyroSensor() throws InterruptedException
    {
        double driveCoefficient = 0.006, driveIntercept = 0.22;

        //Desired heading is 0.
        double offFromHeading = getValidGyroHeading() - desiredHeading;

        //Change motor powers based on offFromHeading.
        double motorPowerChangeFactor = Math.signum(movementPower) * Math.signum(offFromHeading) * (Math.abs(offFromHeading) * driveCoefficient + driveIntercept);

        double rightPower = movementPower * (1 - motorPowerChangeFactor), leftPower = movementPower * (1 + motorPowerChangeFactor);

        //Now set the motor power of each motor equal to the current motor power plus the correction factor.
        setLeftPower(Range.clip(rightPower, -1, 1));
        setRightPower(Range.clip(leftPower, -1, 1));

//       outputConstantDataToDrivers(
//               new String[] {
//                       "Desired heading = " + desiredHeading,
//                       "Off from heading = " + offFromHeading,
//                       "Right power = " + rightPower,
//                       "Left power = " + leftPower,
//                       "Motor power change factor = " + motorPowerChangeFactor
//               }
//       );

        //Idle regardless.
        idle();
    }
    private long lastCheckTime = 0;
    private double previousDistance = 0;
    protected void increaseMovementPowerIfMovingTooSlowly () throws InterruptedException
    {
        int drivenDistance = getDistanceDriven ();
        if ((System.currentTimeMillis () - lastCheckTime) >= 100 && Math.abs(getDistanceDriven () - previousDistance) <= 10)
        {
            outputNewLineToDrivers ("Increasing encoder move power.");

            movementPower += Math.signum (movementPower) * 0.02;
            previousDistance = drivenDistance;
            lastCheckTime = System.currentTimeMillis ();
        }
    }

    /******** TURNING ********/
    //Used to turn to a specified heading, and returns the difference between the desired angle and the actual angle achieved.
    protected enum TurnMode {
        LEFT, RIGHT, BOTH
    }
    //This battery factor, when updated, remains updated for all future turns so that the robot does not have to start changing it again.
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        if (gyroscope != null)
        {
            double turnCoefficient = 0.004, turnIntercept = 0.23, initialTurnIntercept = turnIntercept;

            this.desiredHeading = desiredHeading;

            //Get the startTime so that we know when to end.
            long startTime = System.currentTimeMillis();
            int priorHeading = getValidGyroHeading();
            long lastCheckedTime = startTime;

            int currentHeading;
            //Adjust as fully as possible but not beyond the time limit.
            while(System.currentTimeMillis() - startTime < maxTime)
            {
                //Verify that the heading that we thought was perfectly on point actually is on point.
                currentHeading = getValidGyroHeading();
                if (currentHeading == this.desiredHeading)
                {
                    stopDriving ();
                    sleep(300);
                    currentHeading = getValidGyroHeading ();
                    //Verify that it really is at the correct heading (like never happens ever) and if it really was, then try something else.
                    if (currentHeading == this.desiredHeading)
                        break;
                }

                //Protection against stalling, increases power if no observed heading change in last fraction of a second.
                if (System.currentTimeMillis() - lastCheckedTime >= 300 && (System.currentTimeMillis () - startTime) > 1000)
                {
                    int headingChange = Math.abs(priorHeading - currentHeading);
                    //Don't start increasing power at the very start of the turn before the robot has had time to accelerate.
                    if (headingChange <= 1)
                    {
                        turnIntercept += 0.06;
                        outputNewLineToDrivers ("Increased turn power");
                    }
                    else if (headingChange >= 7 && turnIntercept > initialTurnIntercept)
                    {
                        turnIntercept -= 0.02;
                        outputNewLineToDrivers ("Decreased turn power");
                    }

                    //Update other variables.
                    priorHeading = currentHeading;
                    lastCheckedTime = System.currentTimeMillis();
                }

                //Turn at a speed proportional to the distance from the ideal heading.
                int thetaFromHeading = currentHeading - this.desiredHeading;

                //Logarithmic turning that slows down upon becoming close to heading but is not scary fast when far from desired heading.
                //Have to shift graph to left in order to prevent log10 from returning negative values upon becoming close to heading.
                //Logarithmic: double turnPower = Math.signum(thetaFromHeading) * (Math.log10(Math.abs(thetaFromHeading) + 1) * coefficient + intercept);
                double turnPower = Math.signum(thetaFromHeading) * (Math.abs(thetaFromHeading) * turnCoefficient + turnIntercept);

                //Set clipped powers.
                if (mode != TurnMode.RIGHT)
                    setLeftPower(-1 * Range.clip(turnPower, -1, 1));
                if (mode != TurnMode.LEFT)
                    setRightPower(Range.clip(turnPower, -1, 1));

                //Output required data.
//                outputConstantDataToDrivers(
//                        new String[]
//                                {
//                                        "Turning to heading " + this.desiredHeading,
//                                        "Current heading = " + currentHeading,
//                                        "Turn Power is " + turnPower,
//                                        "I have " + (maxTime - (System.currentTimeMillis() - startTime)) + "ms left.",
//                                        "Turn coefficient = " + turnCoefficient,
//                                        "Min turn speed = " + turnIntercept
//                                }
//                );
            }

            stopDriving();
        }
        else
        {
            //Turn pretty normally with no gyro sensor attached.
            int directionCoefficient = (desiredHeading < 0 ? -1 : 1);
            setLeftPower(-1 * .5 * directionCoefficient);
            setRightPower(.5 * directionCoefficient);
            sleep((long) Math.abs(desiredHeading));
        }
    }

    /******** DRIVING METHODS ********/
    //Used to driveForTime in a straight line with the aid of the gyroscope.
    protected void driveForTime(double power, long length) throws InterruptedException
    {
        setMovementPower (power); // Set the initial power.

        if (gyroscope != null)
        {
            //Required variables.
            long startTime = System.currentTimeMillis();

            //Gyroscope turning mechanics.
            while (System.currentTimeMillis() - startTime < length)
                adjustMotorPowersBasedOnGyroSensor();
        }
        else
        {
            sleep(length);
        }

        //Stop the bot.
        stopDriving();

        outputNewLineToDrivers("Drove for " + length + " at " + power + ".");
    }
    protected void driveForDistance (double power, int length) throws InterruptedException
    {
        int powerSign = (int) (Math.signum(power));
        length = Math.abs(length); //Otherwise this will go haywire.
        double initialDrivePosition = getDistanceDriven ();
        double desiredPosition = initialDrivePosition + powerSign * length;

        setMovementPower (power);

        //TESTING
        //If we are at position 100 and we want to go to position 10.
        //length = 90, power = -0.3
        //initialDrivePosition = 100, desiredPosition = 10
        //while (10 * -1 >= 100 * -1) evaluates to false.  while (-10 >= -100) while (true)
        //this works.

        //If we are at position 100 and we want to go to position 150
        //length = 50, power = 0.3
        //initialDrivePosition = 100, desiredPosition = 150
        //while (150 * 1 >= 100 * 1) while (true);

        //If we are at position -100 and we want to go to -150:
        //length = 50, power = -0.3
        //initialDrivePosition = -100, desiredPosition = -150;
        //while(150 >= 100)

        //If we are at position -100 and we want to go to -50
        //length = 50, power = 0.3
        //initialDrivePosition = -100, desiredPosition = -50
        //while (-50 >= -100) while (true)

        while (desiredPosition * powerSign >= getDistanceDriven () * powerSign)
        {
            //Increase power if moving very slowly.
            increaseMovementPowerIfMovingTooSlowly ();

            //Adjust the motor powers based on the gyro even while driving with encoders.
            adjustMotorPowersBasedOnGyroSensor ();

            //Give the drivers a bit of insight into which encoders are currently working (two out of four are currently operational).
//            outputConstantDataToDrivers(
//                    new String[] {
//                            "Driving at " + power + " for " + length,
//                            "Encoder val 1 = " + leftDriveMotors.get(0).getCurrentPosition (),
//                            "Encoder val 2 = " + leftDriveMotors.get(1).getCurrentPosition (),
//                            "Encoder val 3 = " + rightDriveMotors.get(0).getCurrentPosition (),
//                            "Encoder val 4 = " + rightDriveMotors.get(1).getCurrentPosition ()
//                    }
//            );
        }

        //End the drive upon reaching the target destination.
        stopDriving ();
    }

    /******** CUSTOM ACTIONS ********/
    protected void pressButton() throws InterruptedException
    {
        //Determine the length to push the pusher out based on the distance from the wall.
        double distanceFromWall = sideRangeSensor.cmUltrasonic ();
        if (distanceFromWall >= 255)
        {
            //Possible that this was a misreading.
            idle();
            distanceFromWall = sideRangeSensor.cmUltrasonic ();
            if (distanceFromWall >= 255) //It can't actually be 255.
                distanceFromWall = 20;
        }
        double extendLength = 65 * distanceFromWall;
        extendLength = Range.clip(extendLength, 0, 3000);
        outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(0);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(1);
        sleep((long) (extendLength - 200));
        rightButtonPusher.setPosition(.5);
    }

    protected void shootBallsIntoCenterVortex () throws InterruptedException
    {
        flywheels.setPower(0.30);
        sleep(300);
        harvester.setPower(-1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);
    }
}
