package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;
import org.firstinspires.ftc.teamcode.programflow.RunState;

//For added simplicity while coding autonomous with the new FTC system. Utilizes inheritance and polymorphism.
public abstract class AutoBase extends MainRobotBase
{
    /******** SENSOR STUFF ********/

    /**** Range Sensors ****/
    protected ModernRoboticsI2cRangeSensor frontRangeSensor;

    /**** Color Sensors (3) ****/
    protected ColorSensor bottomColorSensor; //Must have different I2C addresses.
    protected boolean option1Red, option2Red, option1Blue, option2Blue;
    protected void updateColorSensorStates()
    {
        //Threshold is currently 2, but this could be changed.
        option1Blue = option1ColorSensor.blue () >= 2;
        option1Red = option1ColorSensor.red () >= 1 && !option1Blue; //Since blue has an annoying tendency to see red and blue color values.
        option2Blue = option2ColorSensor.blue () >= 2;
        option2Red = option2ColorSensor.red () >= 1 && !option2Blue;
    }

    /**** Gyro ****/
    protected GyroSensor gyroscope;
    private int desiredHeading = 0; //Massively important to maintaining stability through the drives.

    //Just resets the gyro.
    private void zeroHeading() throws InterruptedException
    {
        sleep(400);
        gyroscope.resetZAxisIntegrator();
        sleep(400);
    }
    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.
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

        return heading;
    }

    //Used to turn to a specified heading, and returns the difference between the desired angle and the actual angle achieved.
    protected enum TurnMode {
        LEFT, RIGHT, BOTH
    }
    //This battery factor, when updated, remains updated for all future turns so that the robot does not have to start changing it again.
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        this.desiredHeading = desiredHeading;

        //Get the startTime so that we know when to end.
        long startTime = System.currentTimeMillis();

        int currentHeading = getValidGyroHeading();
        //Adjust as fully as possible but not beyond the time limit.
        while(RunState.getState () != RunState.DriverSelectedState.STOP &&
                (System.currentTimeMillis() - startTime < maxTime || Math.abs(currentHeading - desiredHeading) >= 10))
        {
            //Verify that the heading that we thought was perfectly on point actually is on point.
            currentHeading = getValidGyroHeading();
            if (currentHeading == this.desiredHeading)
            {
                hardBrake (200);
                currentHeading = getValidGyroHeading ();
                if (currentHeading == this.desiredHeading)
                    break;
            }

            //Turn at a speed proportional to the distance from the ideal heading.
            int thetaFromHeading = currentHeading - this.desiredHeading;

            double turnPower = Math.signum(thetaFromHeading) * (Math.abs(thetaFromHeading) * 0.0042 + 0.23);

            //Set clipped powers.
            if (mode != TurnMode.RIGHT)
                leftDrive.setRPS (-1 * Range.clip(turnPower, -1, 1));
            if (mode != TurnMode.LEFT)
                rightDrive.setRPS (Range.clip(turnPower, -1, 1));

            rightDrive.updateMotorPowerWithPID ();
            leftDrive.updateMotorPowerWithPID ();

            idle();
        }

        hardBrake (100);
    }

    /******** DRIVING METHODS ********/

    protected enum PowerUnits
    {
        RevolutionsPerSecond(1), RevolutionsPerMinute(60);

        public final double conversionFactor;
        PowerUnits(double conversionFactor)
        {
            this.conversionFactor = conversionFactor;
        }
    }
    protected enum SensorStopType
    {
        Distance, Ultrasonic, BottomColorAlpha
    }
    protected enum SensorDriveAdjustment
    {
        UseRangeSensor, DontUseRangeSensor
    }
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double measure) throws InterruptedException
    {
        drive(sensorStopType, stopValue, powerUnit, measure, SensorDriveAdjustment.DontUseRangeSensor);
    }
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure, SensorDriveAdjustment sensorAdjustmentType) throws InterruptedException
    {
        //Set initial power, and then modify it later.
        leftDrive.setRPS (powerMeasure);
        rightDrive.setRPS (powerMeasure);

        //Allows us to know when we stop.
        boolean reachedFinalDest = false;

        long lastUpdateTime = 0, adjustRate = 50;

        //Actual adjustment aspect of driving.
        while (!reachedFinalDest && RunState.getState () != RunState.DriverSelectedState.STOP)
        {
            if (System.currentTimeMillis () - lastUpdateTime > adjustRate)
            {
                //Do this before setting new powers, since it will adjust erratically otherwise.
                rightDrive.updateMotorPowerWithPID ();
                leftDrive.updateMotorPowerWithPID ();

                /** GYROSCOPE ADJUSTMENT **/
                //For each result, positive favors left side and negative the right side.
                int offFromHeading = desiredHeading - getValidGyroHeading ();

                //Change motor powers based on offFromHeading.
                double gyroAdjustment = Math.signum (offFromHeading) * (Math.abs (offFromHeading) * .006 + .22);

                /** RANGE SENSOR ADJUSTMENT **/
                double rangeSensorAdjustment = 0;
                if (sensorAdjustmentType == SensorDriveAdjustment.UseRangeSensor)
                {
                    double rangeSensorReading = sideRangeSensor.cmUltrasonic ();
                    if (rangeSensorReading >= 50 || rangeSensorReading <= 0)
                        rangeSensorReading = 15;

                    //Desired range sensor values.
                    double offFromDistance = rangeSensorReading - 15;

                    //Change motor powers based on offFromHeading.
                    rangeSensorAdjustment = offFromDistance * 0.03;
                }


                //Set resulting movement powers based on calculated values.  Can be over one since this is fixed later
                double totalAdjustmentFactor = Math.signum (powerMeasure) * gyroAdjustment + rangeSensorAdjustment;
                rightDrive.setRPS (powerMeasure * (1 - totalAdjustmentFactor));
                leftDrive.setRPS (powerMeasure * (1 + totalAdjustmentFactor));

                switch (sensorStopType)
                {
                    case Distance:
                        int powerSign = (int) (Math.signum (powerMeasure));
                        reachedFinalDest = stopValue * powerSign >= ((leftDrive.encoderMotor.getCurrentPosition () + rightDrive.encoderMotor.getCurrentPosition ()) / 2.0) * powerSign;
                        break;

                    case Ultrasonic:
                        reachedFinalDest = frontRangeSensor.cmUltrasonic () >= stopValue;
                        break;

                    case BottomColorAlpha:
                        reachedFinalDest = bottomColorSensor.alpha () >= stopValue;
                        break;
                }
            }

            idle();
        }

        hardBrake (100);
    }

    //Stops all drive motors and pauses for a moment
    protected void hardBrake(long msDelay) throws InterruptedException
    {
        leftDrive.setRPS (0);
        leftDrive.setRPS (0);
        sleep(msDelay);
    }

    /******** CUSTOM ACTIONS ********/
    protected void shootBallsIntoCenterVortex () throws InterruptedException
    {
        flywheels.setRPS (0.32);
        sleep(300);
        harvester.setRPS (-1.0);
        sleep(2200);
        flywheels.setRPS (0);
        harvester.setRPS (0);
    }


    /******** INITIALIZATION ********/
    //Initialize everything required in autonomous that isn't initialized in MainRobotBase (sensors)
    @Override
    protected void initializeOpModeSpecificHardware() throws InterruptedException
    {
        //Initialize color sensors.
        ConsoleManager.outputNewLineToDrivers ("Initializing Color Sensors...");
        option1ColorSensor = initialize(ColorSensor.class, "Option 1 Color Sensor");
        option1ColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        option1ColorSensor.enableLed(false);
        option2ColorSensor = initialize(ColorSensor.class, "Option 2 Color Sensor");
        option2ColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        option2ColorSensor.enableLed(false);
        bottomColorSensor = initialize(ColorSensor.class, "Bottom Color Sensor");
        bottomColorSensor.setI2cAddress(I2cAddr.create8bit(0x3c));
        bottomColorSensor.enableLed(true);
        ConsoleManager.appendToLastOutputtedLine ("OK!");

        //Initialize the range sensors for autonomous.
        frontRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor");
        frontRangeSensor.setI2cAddress(I2cAddr.create8bit(0x90));
        //The range sensors are odd and often return .269 with this method unless the robot is restarted.
        ConsoleManager.outputNewLineToDrivers ("Initializing Front Range Sensor...");
        if (frontRangeSensor.getDistance(DistanceUnit.CM) < 1.0)
            ConsoleManager.appendToLastOutputtedLine ("FAILED!");
        else
            ConsoleManager.appendToLastOutputtedLine ("OK!");

        //Initialize encoders.
        ConsoleManager.outputNewLineToDrivers ("Initializing Encoders...");
        leftDrive.resetEncoder ();
        rightDrive.resetEncoder ();
        ConsoleManager.appendToLastOutputtedLine ("OK!");

        //Initialize gyroscope.
        gyroscope = initialize(GyroSensor.class, "Gyroscope");
        if (gyroscope != null)
        {
            //Start gyroscope calibration.
            ConsoleManager.outputNewLineToDrivers("Initializing Gyroscope...");
            gyroscope.calibrate();

            //Pause to prevent odd errors in which it says it's configured but is actually LYING.
            sleep(1000);

            while (gyroscope.isCalibrating())
                sleep(50);

            zeroHeading();

            ConsoleManager.appendToLastOutputtedLine ("OK!");
        }
    }


    /******** CHILD CLASS INHERITANCE ********/
    //All child classes should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;
}
