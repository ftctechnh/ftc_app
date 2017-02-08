package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class _AutonomousBase extends _RobotBase
{
    //Only used during autonomous.
    protected GyroSensor gyroscope;
    protected int desiredHeading = 0; //This variable is super helpful to the program as a whole since it is accounted for for both the adjustMotor methods and the turn method.

    protected ColorSensor rightColorSensor, bottomColorSensor; //Must have different I2C addresses.
    protected ModernRoboticsI2cRangeSensor frontRangeSensor, backRangeSensor;

    // Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //initialize color sensors for either side (do in _AutonomousBase because they are useless during teleop.
        rightColorSensor = initialize(ColorSensor.class, "Right Color Sensor");
        rightColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        rightColorSensor.enableLed(false);
        bottomColorSensor = initialize(ColorSensor.class, "Bottom Color Sensor");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        bottomColorSensor.enableLed(true);

        //Init the range sensors for autonomous.
        frontRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor");
        frontRangeSensor.setI2cAddress(I2cAddr.create8bit(0x90));
        backRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor");
        backRangeSensor.setI2cAddress(I2cAddr.create8bit(0x10));

        //initialize gyroscope (will output whether it was found or not.
        gyroscope = initialize(GyroSensor.class, "Gyroscope");
        if (gyroscope != null)
        {
            //Start gyroscope calibration.
            outputNewLineToDrivers("Gyroscope Calibrating...");
            gyroscope.calibrate();

            //Pause to prevent odd errors.
            sleep(1000);

            while (opModeIsActive() && gyroscope.isCalibrating())
                sleep(50);

            zeroHeading();

            outputNewLineToDrivers("Gyroscope Calibration Complete!");
        }

        if (frontRangeSensor.getDistance(DistanceUnit.CM) < 1.0 && backRangeSensor.getDistance(DistanceUnit.CM) < 1.0)
        {
            outputNewLineToDrivers("RANGE SENSORS MISCONFIGURED");
        }
    }

    //All children should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;

    @Override
    protected void driverStationSaysSTOP()
    {

    }

    //Used to set drive move power initially.
    protected double movementPower = .5;
    protected void setMovementPower(double movementPower)
    {
        this.movementPower = movementPower;

        setLeftPower(movementPower);
        setRightPower(movementPower);
    }


    /************* GYRO READING GUIDE *************/
    /*
                                0
                                |
                                |
                                |
            -45                 |                 45
                    -           |        +
                                |
                                |
                                |
         -90 ------------------------------------------ 90
     */

    //Just resets the gyro.
    protected void zeroHeading() throws InterruptedException
    {
        sleep(400);
        gyroscope.resetZAxisIntegrator();
        sleep(400); //Resetting gyro heading has an annoying tendency to not actually zero, which is kinda annoying but not much can be done about it.
    }

    //Method that adjusts the heading based on the gyro heading and logarithmic mathematics.  Called once per frame.
    private double offCourseGyroCorrectionFactor = .1; //Less means less sensitive, .1 seems ideal.
    protected void adjustMotorPowersBasedOnGyroSensor() throws InterruptedException
    {
        if (gyroscope != null)
        {
            //Desired heading is 0.
            double offFromHeading = getValidGyroHeading() - desiredHeading;

            //If offFromHeading is positive, then we want to increase the right power and decrease the left power.  Vice versa also true
            //We also want some sort of coefficient for the amount that each power is changed by.
            double motorPowerChange = Math.signum(offFromHeading) * (Math.log10(Math.abs(offFromHeading) + 1) * offCourseGyroCorrectionFactor);

            //Now set the motor power of each motor equal to the current motor power plus the correction factor.
            setLeftPower(Range.clip(movementPower - motorPowerChange, -1, 1));
            setRightPower(Range.clip(movementPower + motorPowerChange, -1, 1));
        }
        else
        {
            outputConstantDataToDrivers(
                    new String[] {
                            "Can't adjust heading, no gyro attached!"
                    }
            );
        }

        idle();
    }

    //Method that adjusts the heading based on the range sensor and logarithmic mathematics.  Called once per frame.
    private double offCourseRangeCorrectionFactor = .06; //Less means less sensitive.
    protected void adjustMotorPowersBasedOnRangeSensors() throws InterruptedException
    {
        if (gyroscope != null)
        {
            //Desired heading is 0.
            double differenceInDistances = frontRangeSensor.cmUltrasonic() - backRangeSensor.cmUltrasonic();

            //If the difference is positive, the front is closer to the wall, meaning that we want to decrease the power for the right side and increase the power for the left side.
            double motorPowerChange = differenceInDistances * offCourseRangeCorrectionFactor;

            //Now set the motor power of each motor equal to the current motor power plus the correction factor.
            setLeftPower(Range.clip(movementPower + motorPowerChange, -1, 1));
            setRightPower(Range.clip(movementPower - motorPowerChange, -1, 1));
        }
        else
        {
            outputConstantDataToDrivers(
                    new String[] {
                            "Can't adjust heading, no gyro attached!"
                    }
            );
        }

        idle();
    }

    //Used to turn to a specified heading, and returns the difference between the desired angle and the actual angle achieved.
    enum TurnMode {
        LEFT, RIGHT, BOTH
    }
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        if (gyroscope != null)
        {
            this.desiredHeading = desiredHeading;

            //Get the startTime so that we know when to end.
            long startTime = System.currentTimeMillis();
            int priorHeading = getValidGyroHeading();
            long lastCheckedTime = startTime;
            double minimumTurnSpeed = 0; //This will increase in the event that the robot notices that we are not turning at all.

            int currentHeading = getValidGyroHeading();
            //Adjust as fully as possible but not beyond the time limit.
            while(System.currentTimeMillis() - startTime < maxTime && currentHeading != this.desiredHeading)
            {
                currentHeading = getValidGyroHeading();

                //Protection against stalling, increases power if no observed gyro change in last half second.
                if (System.currentTimeMillis() - lastCheckedTime >= 500)
                {
                    if (priorHeading == currentHeading)
                        minimumTurnSpeed += .05;

                    lastCheckedTime = System.currentTimeMillis();
                    priorHeading = currentHeading;
                }

                //Turn at a speed proportional to the distance from the ideal heading.
                int thetaFromHeading = currentHeading - this.desiredHeading;

                //Logarithmic turning that slows down upon becoming close to heading but is not scary fast when far from desired heading.
                //Have to shift graph to left in order to prevent log10 from returning negative values upon becoming close to heading.
                double turnPower = Math.signum(thetaFromHeading) * (Math.log10(Math.abs(thetaFromHeading) + 1) * .2 + minimumTurnSpeed);

                //Set clipped powers.
                if (mode != TurnMode.RIGHT)
                    setLeftPower(-1 * Range.clip(turnPower, -1, 1));
                if (mode != TurnMode.LEFT)
                    setRightPower(Range.clip(turnPower, -1, 1));

                outputConstantDataToDrivers(
                        new String[]
                                {
                                        "Turning to heading " + this.desiredHeading,
                                        "Current heading = " + currentHeading,
                                        "Turn Power is " + turnPower,
                                        "I have " + (maxTime - (System.currentTimeMillis() - startTime)) + "ms left."
                                }
                );
            }

            stopDriving();
        }
        else
        {
            //Turn pretty normally with no gyro sensor attached.
            int directionCoefficient = (desiredHeading < 0 ? -1 : 1);
            setLeftPower(.5 * directionCoefficient);
            setRightPower(-1 * .5 * directionCoefficient);
            sleep((long) Math.abs(desiredHeading));
        }
    }

    //Used to driveForTime in a straight line with the aid of the gyroscope.
    protected void driveForTime(double power, long length) throws InterruptedException
    {
        setMovementPower(power); // Set the initial power.

        if (gyroscope != null) {
            //Required variables.
            double startTime = System.currentTimeMillis();

            sleep(200);

            //Gyroscope turning mechanics.
            while (opModeIsActive() && System.currentTimeMillis() - startTime < length)
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

    protected void driveForDistance(double power, int length) throws InterruptedException
    {
        // Setup
        for (DcMotor motor : rightDriveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        for (DcMotor motor : leftDriveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        idle();

        // Drive to target position
        for (DcMotor motor : rightDriveMotors) {
            motor.setTargetPosition(length);
            motor.setPower(power);
        }
        for (DcMotor motor : leftDriveMotors) {
            motor.setTargetPosition(length);
            motor.setPower(power);
        }

        // Reset
        for (DcMotor motor : rightDriveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        for (DcMotor motor : leftDriveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

    }

    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.
    //This makes sure that the value makes sense for calculations.
    protected int getValidGyroHeading()
    {
        int heading = gyroscope.getHeading();

        if (heading > 180 && heading < 360)
            heading -= 360;

        return heading;
    }

    //Stops all drive motors.
    protected void stopDriving ()
    {
        setLeftPower(0);
        setRightPower(0);
    }
}
