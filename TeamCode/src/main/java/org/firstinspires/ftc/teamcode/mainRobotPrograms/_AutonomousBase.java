package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class _AutonomousBase extends _RobotBase
{
    //Only used during autonomous.
    protected GyroSensor gyroscope;
    protected int desiredHeading = 0; //This variable is super helpful to the program as a whole since it is accounted for in both the adjustMotor methods and the turn method, and thus enables insufficient turns to be completed later on.

    protected ColorSensor option1ColorSensor, option2ColorSensor; //Must have different I2C addresses.
    protected ModernRoboticsI2cRangeSensor frontRangeSensor, sideRangeSensor;

    // Initialize everything required in autonomous that isn't initialized in RobotBase (sensors)
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //Initialize color sensors.
        option1ColorSensor = initialize(ColorSensor.class, "Option 1 Color Sensor");
        option1ColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        option1ColorSensor.enableLed(false);
        option2ColorSensor = initialize(ColorSensor.class, "Option 2 Color Sensor");
        option2ColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        option2ColorSensor.enableLed(false);

        //Initialize the range sensors for autonomous.
        frontRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor");
        frontRangeSensor.setI2cAddress(I2cAddr.create8bit(0x90));
        sideRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor");
        sideRangeSensor.setI2cAddress(I2cAddr.create8bit(0x10));
        //The range sensors are odd and often return .269 with this method unless the robot is restarted.
        if (frontRangeSensor.getDistance(DistanceUnit.CM) < 1.0 && sideRangeSensor.getDistance(DistanceUnit.CM) < 1.0)
            outputNewLineToDrivers("RANGE SENSORS MISCONFIGURED");

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

    //All children should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;

    //Used to set drive move power initially.
    protected double movementPower = .5;
    protected void setMovementPower(double movementPower)
    {
        this.movementPower = movementPower;

        setLeftPower(movementPower);
        setRightPower(movementPower);
    }

    //Method that adjusts the heading based on the gyro heading and logarithmic mathematics.  Called once per frame.
    private double offCourseGyroCorrectionFactor = .06; //Less means less sensitive.
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

//            outputConstantDataToDrivers(
//                    new String[] {
//                            "Off from heading = " + offFromHeading,
//                            "Motor power change = " + motorPowerChange
//                    }
//            );
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
    //This battery factor, when updated, remains updated for all future turns so that the robot does not have to start changing it again.
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        if (gyroscope != null)
        {
            this.desiredHeading = desiredHeading;

            //Get the startTime so that we know when to end.
            long startTime = System.currentTimeMillis();
            int priorHeading = getValidGyroHeading();
            long lastCheckedTime = startTime;
            double turnSpeedBatteryFactor = .16;

            int currentHeading = getValidGyroHeading();
            //Adjust as fully as possible but not beyond the time limit.
            while(System.currentTimeMillis() - startTime < maxTime && currentHeading != this.desiredHeading)
            {
                currentHeading = getValidGyroHeading();

                //Protection against stalling, increases power if no observed heading change in last fraction of a second.
                if (System.currentTimeMillis() - lastCheckedTime >= 400)
                {
                    //Don't start increasing power at the very start of the turn before the robot has had time to accelerate.
                    if (priorHeading == currentHeading && (System.currentTimeMillis () - startTime) > 800)
                        turnSpeedBatteryFactor += 0.07;

                    //Update other variables.
                    lastCheckedTime = System.currentTimeMillis();
                    priorHeading = currentHeading;
                }

                //Turn at a speed proportional to the distance from the ideal heading.
                int thetaFromHeading = currentHeading - this.desiredHeading;

                //Logarithmic turning that slows down upon becoming close to heading but is not scary fast when far from desired heading.
                //Have to shift graph to left in order to prevent log10 from returning negative values upon becoming close to heading.
                double turnPower = Math.signum(thetaFromHeading) * (Math.log10(Math.abs(thetaFromHeading) + 1) * turnSpeedBatteryFactor);

                //Set clipped powers.
                if (mode != TurnMode.RIGHT)
                    setLeftPower(-1 * Range.clip(turnPower, -1, 1));
                if (mode != TurnMode.LEFT)
                    setRightPower(Range.clip(turnPower, -1, 1));

                //Output required data.
                outputConstantDataToDrivers(
                        new String[]
                                {
                                        "Turning to heading " + this.desiredHeading,
                                        "Current heading = " + currentHeading,
                                        "Turn Power is " + turnPower,
                                        "I have " + (maxTime - (System.currentTimeMillis() - startTime)) + "ms left.",
                                        "Turn battery factor = " + turnSpeedBatteryFactor
                                }
                );
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

    //Used to driveForTime in a straight line with the aid of the gyroscope.
    protected void driveForTime(double power, long length) throws InterruptedException
    {
        setMovementPower(power); // Set the initial power.

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

        //This set of instructions is MASSIVELY important.  The RUN_WITHOUT_ENCODER mode doesn't actually make the thing not use encoders, it just prevents the encoders from directly regulating motor powers.  Weird names for the RunMode options by the FTC folks.
        for (DcMotor motor : leftDriveMotors)
            motor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
        for (DcMotor motor : rightDriveMotors)
            motor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(100); //Take a short break after each step, since the SDK we are using is not synchronous (it messed up our workflow)

        //This HAS to have "RUN_USING_ENCODER" before it for some reason, or it just hangs forever.
        for (DcMotor motor : leftDriveMotors)
            motor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        for (DcMotor motor : rightDriveMotors)
            motor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);

        //This prevents the encoders from trying to regulate the motors on their own (they aren't qualified for that sort of work!), and affecting the gyro.
        for (DcMotor motor : leftDriveMotors)
            motor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        for (DcMotor motor : rightDriveMotors)
            motor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(100);

        setMovementPower (power);

        boolean motorsBusy = true;
        while (motorsBusy)
        {
            //Adjust the motor powers based on the gyro even while driving with encoders.
            adjustMotorPowersBasedOnGyroSensor ();

            //These couple statements check to see if all of the motors are currently running.  If one is not, then the whole robot stops.
            motorsBusy = true;
            for (DcMotor motor : leftDriveMotors)
            {
                if (Math.abs(motor.getCurrentPosition ()) >= length)
                {
                    motorsBusy = false;
                    break; //End the for loop if one has reached the end of its drive length.
                }
            }
            if (motorsBusy)
            {
                for (DcMotor motor : rightDriveMotors)
                {
                    if (Math.abs(motor.getCurrentPosition ()) >= length)
                    {
                        motorsBusy = false;
                        break;
                    }
                }
            }

            //Give the drivers a bit of insight into which encoders are currently working (two out of four are currently operational).
            outputConstantDataToDrivers(
                    new String[] {
                            "Driving at " + power + " for " + length,
                            "Encoder val 1 = " + leftDriveMotors.get(0).getCurrentPosition (),
                            "Encoder val 2 = " + leftDriveMotors.get(1).getCurrentPosition (),
                            "Encoder val 3 = " + rightDriveMotors.get(0).getCurrentPosition (),
                            "Encoder val 4 = " + rightDriveMotors.get(1).getCurrentPosition ()
                    }
            );
        }

        //End the drive upon reaching the target destination.
        stopDriving ();
    }

    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.  This method makes sure that the value makes sense for calculations.
    protected int gyroAdjustFactor; //Based on range sensors.
    protected int getValidGyroHeading()
    {
        //Get the heading.
        int heading = gyroscope.getHeading();

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

    //Just resets the gyro.
    protected void zeroHeading() throws InterruptedException
    {
        sleep(400);
        gyroscope.resetZAxisIntegrator();
        sleep(400); //Resetting gyro heading has an annoying tendency to not actually zero, which is kinda annoying but not much can be done about it.
    }

    //Stops all drive motors.
    protected void stopDriving ()
    {
        setLeftPower(0);
        setRightPower(0);
    }
}
