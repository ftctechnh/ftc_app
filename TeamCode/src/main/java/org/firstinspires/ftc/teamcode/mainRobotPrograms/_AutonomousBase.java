package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.TouchSensor;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class _AutonomousBase extends _RobotBase
{
    //Only used during autonomous.
    protected GyroSensor gyroscope;
    protected ColorSensor leftColorSensor, rightColorSensor, bottomColorSensor; //Must have different I2C addresses.
    protected TouchSensor touchSensor;

    // Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //initialize color sensors for either side (do in _AutonomousBase because they are useless during teleop.
        leftColorSensor = initialize(ColorSensor.class, "colorLeft");
        leftColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        leftColorSensor.enableLed(true);
        rightColorSensor = initialize(ColorSensor.class, "colorRight");
        rightColorSensor.setI2cAddress(I2cAddr.create8bit(0x2c));
        rightColorSensor.enableLed(true);
        bottomColorSensor = initialize(ColorSensor.class, "colorBottom");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        bottomColorSensor.enableLed(true);

        touchSensor = initialize(TouchSensor.class, "touchSensor");

        //initialize gyroscope (will output whether it was found or not.
        gyroscope = initialize(GyroSensor.class, "Gyroscope");
        if (gyroscope != null)
        {
            //Start gyroscope calibration.
            outputNewLineToDriverStation("Gyroscope Calibrating...");
            gyroscope.calibrate();

            //Pause to prevent errors.
            sleep(1000);

            while (opModeIsActive() && gyroscope.isCalibrating())
                sleep(50);

            outputNewLineToDriverStation("Gyroscope Calibration Complete!");
        }

    }

    //All children should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;

    //Used to set drive move power initially.
    protected double movementPower = .5f;
    protected void setMovementPower(double movementPower)
    {
        this.movementPower = movementPower;

        for (DcMotor lMotor: leftDriveMotors)
            lMotor.setPower(movementPower);
        for (DcMotor rMotor : rightDriveMotors)
            rMotor.setPower(movementPower);
    }

    //Just resets the gyro.
    protected void zeroHeading() throws InterruptedException
    {
        sleep(200);
        gyroscope.resetZAxisIntegrator();
        sleep(800);
    }

    //Account for drifting tendency of the bot.
    protected void adjustHeading() throws InterruptedException
    {
        int headingOffset = getValidGyroHeading();
        turnToHeading(-headingOffset, turnMode.BOTH);
    }

    //More complex method that adjusts the heading based on the gyro heading.
    protected double offCourseSensitivity = 42; //Max of 100, Min of 0 (DON'T DO 100 OR DIV BY 0 ERROR)
    protected void updateMotorPowersBasedOnGyroHeading() throws InterruptedException
    {
        if (gyroscope != null)
        {
            // Get the heading info.
            int heading = getValidGyroHeading();

            //Create values.
            double gyroFactor = (heading) / (100.0 - offCourseSensitivity);
            double leftPower = movementPower + gyroFactor;
            double rightPower = movementPower - gyroFactor;

            //Clamp values.
            if (leftPower > 1)
                leftPower = 1;
            else if (leftPower < -1)
                leftPower = -1;

            if (rightPower > 1)
                rightPower = 1;
            else if (rightPower < -1)
                rightPower = -1;

            //Set the motor powers.
            for (DcMotor lMotor : leftDriveMotors)
                lMotor.setPower(leftPower);
            for (DcMotor rMotor : rightDriveMotors)
                rMotor.setPower(rightPower);

            //Output data to the DS.
            //Note: the 2nd parameter "%.2f" changes the output of the max decimal points.
            outputConstantLinesToDriverStation(
                    new String[] {
                            "Heading: " + heading,
                            "L Power: " + leftPower,
                            "R Power: " + rightPower
                    }
            );
        } else
        {
            outputConstantLinesToDriverStation(
                    new String[] {
                            "Can't adjust heading, no gyro attached!"
                    }
            );
        }

        idle();
    }

    //Used to turn to a specified heading.
    protected double initialTurnPower = .1; //Can be any value less than 1 (but should be less than .5)
    protected void setInitialTurnPower(double turnPower) {initialTurnPower = turnPower;}
    protected double successiveTurnReduction = 3.6; //Should be greater than 1.
    protected double incrementFactor = 0.00012; //The rate at which the bot slowly speeds up.
    protected double precisionFactor = 5; //precisionFactor * 2 radius is acceptable.
    protected void setPrecision(double newPrecision) {precisionFactor = newPrecision;}

    enum turnMode {
        LEFT, RIGHT, BOTH
    }
    protected void turnToHeading (int desiredHeading, turnMode mode) throws InterruptedException
    {
        //Just exit the method if the heading is already achieved.
        if (desiredHeading == 0)
            return;

        zeroHeading(); //Initialization step.

        //This variable changes for each successive turn.
        double turnPower = initialTurnPower;
        int previousHeading = getValidGyroHeading();
        while (true) // Will eventually be BROKEN out of.
        {
            double incrementValue = turnPower * incrementFactor;
            int initialSign = Integer.signum(getValidGyroHeading() - desiredHeading);
            //Wait until the desired value is turned over or reached.
            int currentSign = initialSign;
            do
            {
                if (mode == turnMode.LEFT || mode == turnMode.BOTH)
                {
                    //Set new motor powers.
                    for (DcMotor lMotor : leftDriveMotors)
                        lMotor.setPower(currentSign * turnPower);
                }

                if (mode == turnMode.RIGHT || mode == turnMode.BOTH)
                {
                    for (DcMotor rMotor : rightDriveMotors)
                        rMotor.setPower(-1 * currentSign * turnPower);
                }

                currentSign = Integer.signum(getValidGyroHeading() - desiredHeading);
                idle();

                outputConstantLinesToDriverStation(new String[] {
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
            sleep(300); //Give the gyro a short break to check.

            if (!(getValidGyroHeading() - precisionFactor <= desiredHeading && desiredHeading <= getValidGyroHeading() + precisionFactor))
                turnPower /= successiveTurnReduction;
            else
                return; //Exit the loop if the end has been achieved.
        }
    }

    //Used to driveForTime in a straight line with the aid of the gyroscope.
    protected void driveForTime(double power, long length) throws InterruptedException
    {
        //Add the output to the driver station.
        outputNewLineToDriverStation("Driving at " + power + " power, for " + length + " milliseconds, with a gyroscope");

        zeroHeading(); // Set the direction to move.

        setMovementPower(power); // Set the initial power.

        //Required variables.
        double startTime = System.currentTimeMillis();

        sleep(300);

        //Gyroscope turning mechanics.
        while (opModeIsActive() && System.currentTimeMillis() - startTime < length)
            updateMotorPowersBasedOnGyroHeading();

        //Stop the bot.
        stopDriving();

        outputNewLineToDriverStation("Drove for " + length + " at " + power + ".");
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
        for (DcMotor lMotor: leftDriveMotors)
            lMotor.setPower(0);
        for (DcMotor rMotor : rightDriveMotors)
            rMotor.setPower(0);
    }
}
