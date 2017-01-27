package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    protected ColorSensor leftColorSensor, rightColorSensor, bottomColorSensor; //Must have different I2C addresses.
    protected TouchSensor touchSensor;
    protected ModernRoboticsI2cRangeSensor frontRangeSensor, backRangeSensor;

    // Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //initialize color sensors for either side (do in _AutonomousBase because they are useless during teleop.
        leftColorSensor = initialize(ColorSensor.class, "colorLeft");
        leftColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        leftColorSensor.enableLed(true);
        bottomColorSensor = initialize(ColorSensor.class, "colorBottom");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        bottomColorSensor.enableLed(true);

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

            outputNewLineToDrivers("Gyroscope Calibration Complete!");
        }

        //Init the range sensors for autonomous.
        frontRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor");
        frontRangeSensor.setI2cAddress(I2cAddr.create8bit(0x99c));
        backRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor");
        backRangeSensor.setI2cAddress(I2cAddr.create8bit(0x99c));
    }

    //All children should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;

    @Override
    protected void driverStationSaysSTOP()
    {

    }

    //Used to set drive move power initially.
    protected double movementPower = .5f;
    protected void setMovementPower(double movementPower)
    {
        this.movementPower = movementPower;

        setLeftPower(movementPower);
        setRightPower(movementPower);
    }

    //Just resets the gyro.
    protected void zeroHeading() throws InterruptedException
    {
        sleep(400);
        gyroscope.resetZAxisIntegrator();
        sleep(400); //Resetting gyro heading has an annoying tendency to not actually zero, which is kinda annoying but not much can be done about it.
    }

    //Account for drifting tendency of the bot.
    protected void adjustHeading() throws InterruptedException
    {
        int headingOffset = getValidGyroHeading();
        turnToHeading(-headingOffset, TurnMode.BOTH);
    }

    //More complex method that adjusts the heading based on the gyro heading.
    protected double gyroLeftCoefficient = .5, gyroRightCoefficient = .5;
    protected double gyroOffCourseSensitivity = 42; //0 < r < 100
    protected void updateMotorPowersBasedOnGyroHeading() throws InterruptedException
    {
        if (gyroscope != null)
        {
            // Get the heading info.
            int heading = getValidGyroHeading();

            //Create values.
            double gyroFactor = (heading) / (100.0 - gyroOffCourseSensitivity);
            gyroLeftCoefficient += gyroFactor;
            gyroRightCoefficient -= gyroFactor;

            //Clamp values.
            double newLeftPower = gyroLeftCoefficient * movementPower, newRightPower = gyroRightCoefficient * movementPower;
            newLeftPower = Range.clip(newLeftPower, -1, 1);
            newRightPower = Range.clip(newRightPower, -1, 1);

            //Set the motor powers.
            setLeftPower(newLeftPower);
            setRightPower(newRightPower);

            //Output data to the DS.
            //Note: the 2nd parameter "%.2f" changes the output of the max decimal points.
            outputConstantDataToDrivers(
                    new String[] {
                            "Heading: " + heading,
                            "L Coeff: " + gyroLeftCoefficient,
                            "R Coeff: " + gyroRightCoefficient
                    }
            );
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

    private double rangeLeftCoefficient = .5, rangeRightCoefficient = .5;
    private double rangeOffCourseSensitivity = 42; //0 < r < 100
    protected void updateMotorPowersBasedOnRangeSensors() throws InterruptedException
    {
        if (frontRangeSensor != null && backRangeSensor != null) {
            //Drive to the i^th color sensor line, then stop.
            double frontDist = frontRangeSensor.getDistance(DistanceUnit.CM), backDist = backRangeSensor.getDistance(DistanceUnit.CM);
            double diff = frontDist - backDist;

            rangeRightCoefficient += diff / (100 - rangeOffCourseSensitivity);
            rangeLeftCoefficient -= diff / (100 - rangeOffCourseSensitivity);

            //Measure the ideal length of the robot from the wall and verify it's position here.
            double newLeftPower = movementPower * rangeLeftCoefficient, newRightPower = movementPower * rangeRightCoefficient;
            newLeftPower = Range.clip(newLeftPower, -1, 1);
            newRightPower = Range.clip(newRightPower, -1, 1);

            setLeftPower(newLeftPower);
            setRightPower(newRightPower);

            //Output debugging data
            outputConstantDataToDrivers(
                    new String[]
                            {
                                    "Wall distance from front = " + frontDist,
                                    "Wall distance from back = " + backDist,
                                    "Left coefficient = " + rangeLeftCoefficient,
                                    "Right coefficient = " + rangeRightCoefficient
                            }
            );
        }
        else
        {
            outputConstantDataToDrivers(
                    new String[]
                            {
                                    "I'M DRIVIN' BLIND!"
                            }
            );
        }
        idle();
    }

    //Used to turn to a specified heading.
    private double initialTurnPower = .1; //Can be any value less than 1 (but should be less than .5)
    protected void setInitialTurnPower(double initialTurnPower) {this.initialTurnPower = initialTurnPower;}
    private double successiveTurnReduction = 3.6; //Should be greater than 1 or will likely go on forever.
    protected void setSuccessiveTurnReduction (double successiveTurnReduction) {this.successiveTurnReduction = successiveTurnReduction;}
    private double accelerationFactor = 0.00012; //The rate at which the bot slowly speeds up while turning.
    protected void setAccelerationFactor(double accelerationFactor) {this.accelerationFactor = accelerationFactor;}
    private double precisionFactor = 5; //precisionFactor * 2 radius is acceptable.
    protected void setPrecisionFactor (double precisionFactor) {this.precisionFactor = precisionFactor;}
    enum TurnMode {
        LEFT, RIGHT, BOTH
    }

    protected void turnToHeading (int desiredHeading, TurnMode mode) throws InterruptedException
    {
        //Just exit the method if the heading is already achieved.
        if (desiredHeading == 0)
            return;

        if (gyroscope != null) {
            zeroHeading(); //Initialization step.

            //This variable changes for each successive turn.
            double turnPower = initialTurnPower;
            int previousHeading = getValidGyroHeading();
            while (true) // Will eventually be BROKEN out of.
            {
                double incrementValue = turnPower * accelerationFactor;
                int initialSign = Integer.signum(getValidGyroHeading() - desiredHeading);
                //Wait until the desired value is turned over or reached.
                int currentSign = initialSign;
                do {
                    if (mode == TurnMode.LEFT || mode == TurnMode.BOTH)
                        setLeftPower(currentSign * turnPower);

                    if (mode == TurnMode.RIGHT || mode == TurnMode.BOTH)
                        setRightPower(-1 * currentSign * turnPower);

                    currentSign = Integer.signum(getValidGyroHeading() - desiredHeading);
                    idle();

                    outputConstantDataToDrivers(new String[]{
                            "Current gyro heading = " + getValidGyroHeading() + " and dHeading is " + desiredHeading + " so sign is " + currentSign
                    });

                    if (previousHeading == getValidGyroHeading())
                        turnPower += incrementValue; //Increase the value by a marginal amount over time to prevent stalling.

                    previousHeading = getValidGyroHeading();

                    if (turnPower > 1)
                        turnPower = 1;
                }
                while (currentSign == initialSign && opModeIsActive());

                stopDriving();
                sleep(300); //Give the gyro a short break to adjust.

                if (!(getValidGyroHeading() - precisionFactor <= desiredHeading && desiredHeading <= getValidGyroHeading() + precisionFactor))
                    turnPower /= successiveTurnReduction;
                else
                    return; //Exit the loop if the end has been achieved.
            }
        }
        else
        {
            int directionCoefficient = (desiredHeading < 0 ? -1 : 1);
            setRightPower(-1 * .5 * directionCoefficient);
            setLeftPower(.5 * directionCoefficient);

            sleep((long) Math.abs(desiredHeading));
        }
    }

    //Used to driveForTime in a straight line with the aid of the gyroscope.
    protected void driveForTime(double power, long length) throws InterruptedException
    {
        if (gyroscope != null) {
            //Add the output to the driver station.
            outputNewLineToDrivers("Driving at " + power + " power, for " + length + " milliseconds, with a gyroscope");

            zeroHeading(); // Set the direction to move.
        }

        setMovementPower(power); // Set the initial power.

        if (gyroscope != null) {
            //Required variables.
            double startTime = System.currentTimeMillis();

            sleep(200);

            //Gyroscope turning mechanics.
            while (opModeIsActive() && System.currentTimeMillis() - startTime < length)
                updateMotorPowersBasedOnGyroHeading();

            //Stop the bot.
        }
        else
        {
            sleep(length);
        }

        stopDriving();

        outputNewLineToDrivers("Drove for " + length + " at " + power + ".");
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
