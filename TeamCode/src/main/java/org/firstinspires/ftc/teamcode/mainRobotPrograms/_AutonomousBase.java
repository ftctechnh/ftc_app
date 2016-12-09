package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.I2cAddr;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class _AutonomousBase extends _RobotBase
{
    //Only used during autonomous.
    protected GyroSensor gyroscope;
    protected ColorSensor leftColorSensor, rightColorSensor, bottomColorSensor; //Must have different I2C addresses.
    protected Servo leftSensorServo, rightSensorServo;
    protected final double RIGHT_SERVO_CLOSED = 1.0, LEFT_SERVO_CLOSED = 1.0;
    protected final double LEFT_SERVO_OPEN = 0.48, RIGHT_SERVO_OPEN = 0.48;

    // Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //initialize color sensors for either side (do in _AutonomousBase because they are useless during teleop.
        leftColorSensor = initialize(ColorSensor.class, "colorLeft");
        leftColorSensor.setI2cAddress(I2cAddr.create8bit(0x2c));
        leftColorSensor.enableLed(true);
//        rightColorSensor = initialize(ColorSensor.class, "colorRight");
//        rightColorSensor.setI2cAddress(I2cAddr.create8bit(0x3c));
//        rightColorSensor.enableLed(true);
        bottomColorSensor = initialize(ColorSensor.class, "colorBottom");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        bottomColorSensor.enableLed(true);

        leftSensorServo = initialize(Servo.class, "servoLeft");
        leftSensorServo.setPosition(LEFT_SERVO_CLOSED);
        rightSensorServo = initialize(Servo.class, "servoRight");
        rightSensorServo.setPosition(RIGHT_SERVO_CLOSED);

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
    protected abstract void driverStationSaysGO();

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
    protected void zeroHeading()
    {
        sleep(500);
        gyroscope.resetZAxisIntegrator();
        sleep(500);
    }

    //Account for drifting tendency of the bot.
    protected void adjustHeading()
    {
        int headingOffset = getValidGyroHeading();
        turnToHeading(-headingOffset);
    }

    //More complex method that adjusts the heading based on the gyro heading.
    protected double offCourseSensitivity = 92; //Max of 100, Min of 0 (DON'T DO 100 OR DIV BY 0 ERROR)
    protected void updateMotorPowersBasedOnGyroHeading()
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
    protected double initialTurnPower = .25; //Can be any value less than 1 (but should be less than .5)
    protected double successiveTurnReduction = 2; //Should be greater than 1.
    protected double incrementFactor = 0.00015; //The rate at which the bot slowly speeds up.
    protected double precisionFactor = 3; //precisionFactor * 2 radius is acceptable.
    protected void turnToHeading (int desiredHeading)
    {
        //Just exit the method if the heading is already achieved.
        if (desiredHeading == 0)
            return;

        zeroHeading(); //Initialization step.

        //This variable changes for each successive turn.
        double turnPower = initialTurnPower;
        while (opModeIsActive()) // Will eventually be BROKEN out of.
        {
            double incrementValue = turnPower * incrementFactor;
            int initialSign = Integer.signum(getValidGyroHeading() - desiredHeading);
            //Wait until the desired value is turned over or reached.
            int currentSign = initialSign;
            do
            {
                //Set new motor powers.
                for (DcMotor lMotor : leftDriveMotors)
                    lMotor.setPower(currentSign * turnPower);
                for (DcMotor rMotor : rightDriveMotors)
                    rMotor.setPower(-1 * currentSign * turnPower);

                currentSign = Integer.signum(getValidGyroHeading() - desiredHeading);
                idle();

                outputConstantLinesToDriverStation(new String[] {
                        "Current gyro heading = " + getValidGyroHeading() + " and dHeading is " + desiredHeading + " so sign is " + currentSign
                });

                turnPower += incrementValue; //Increase the value by a marginal amount over time to prevent stalling.

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
    protected void driveForTime(double power, long length)
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

        adjustHeading();
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

    //Should be constant among all classes.
    protected void pushButton()
    {
        //Press button
        pusher.setPower(.1);
        sleep(500);
        pusher.setPower(-.1);
        sleep(500);
        pusher.setPower(0);

    }
}
