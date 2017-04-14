package org.firstinspires.ftc.teamcode.autonomous;

import android.os.AsyncTask;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;
import org.firstinspires.ftc.teamcode.smarthardware.AdvancedMotorController;
import org.firstinspires.ftc.teamcode.threads.ProgramFlow;
import org.firstinspires.ftc.teamcode.smarthardware.SmartColorSensor;
import org.firstinspires.ftc.teamcode.smarthardware.SmartGyroSensor;
import org.firstinspires.ftc.teamcode.smarthardware.SmartRangeSensor;

//For added simplicity while coding autonomous with the new FTC system. Utilizes inheritance and polymorphism.
public abstract class AutoBase extends MainRobotBase
{
    /******** SENSOR STUFF ********/

    /**** Range Sensors ****/
    protected SmartRangeSensor frontRangeSensor, sideRangeSensor;

    /**** Color Sensors (3) ****/
    protected SmartColorSensor option1ColorSensor, option2ColorSensor, bottomColorSensor, particleColorSensor;
    protected boolean option1Red, option2Red, option1Blue, option2Blue;
    protected void updateColorSensorStates()
    {
        //Threshold is currently 2, but this could be changed.
        option1Blue = option1ColorSensor.sensor.blue () >= 2;
        option1Red = option1ColorSensor.sensor.red () >= 1 && !option1Blue; //Since blue has an annoying tendency to see red and blue color values.
        option2Blue = option2ColorSensor.sensor.blue () >= 2;
        option2Red = option2ColorSensor.sensor.red () >= 1 && !option2Blue;
    }

    /**** Gyro ****/
    protected SmartGyroSensor gyroscope;

    /******** Robot Driving ********/
    protected enum PowerUnits
    {
        RevolutionsPerSecond(1), RevolutionsPerMinute(1.0/60);

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

    //A new instance is instantiated upon starting a new drive.
    private final class SelfAdjustingDriveTask extends AsyncTask<Void, Void, Void>
    {
        private final double power;
        private final boolean useRangeSensor;
        private final ConsoleManager.ProcessConsole selfAdjustingDriveConsole;

        public SelfAdjustingDriveTask(double power)
        {
            this(power, false);
        }
        public SelfAdjustingDriveTask(double power, boolean useRangeSensor)
        {
            selfAdjustingDriveConsole = new ConsoleManager.ProcessConsole ("Self Adjusting Drive");

            this.power = power;
            this.useRangeSensor = useRangeSensor;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                while (true)
                {
                    double gyroAdjustment = gyroscope.getOffFromHeading () * 0.15 * Math.signum(power);
                    double rangeAdjustment = useRangeSensor ? sideRangeSensor.getDistOffFromIdealWallDist () * 0.15 : 0;

                    double totalAdjustment = gyroAdjustment + rangeAdjustment;

                    double leftPower = power * (1 + totalAdjustment), rightPower = power * (1 - totalAdjustment);

                    selfAdjustingDriveConsole.updateWith (
                            "Gyro adjustment = " + gyroAdjustment,
                            "Range adjustment = " + rangeAdjustment,
                            "Left power = " + leftPower,
                            "Right power = " + rightPower
                    );

                    ProgramFlow.pauseForMS (50);
                }
            }
            catch (InterruptedException e) //When the robot has reached its destination.
            {
                ConsoleManager.outputNewSequentialLine ("Stop requested: Stopping Intelligent Drive Task!");
                cancel (true);
            }

            return null;
        }

        //Either cancelled here or by the main thread.
        @Override
        protected void onCancelled ()
        {
            selfAdjustingDriveConsole.destroy ();
        }
    }

    //This method is the task which looks for the termination and returns to the original task once complete.
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure) throws InterruptedException
    {
        drive(sensorStopType, stopValue, powerUnit, powerMeasure, false);
    }
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure, boolean useRangeSensorAdjustment) throws InterruptedException
    {
        //Create a new output console entirely for this process.
        ConsoleManager.ProcessConsole driveTerminationConsole = new ConsoleManager.ProcessConsole ("Drive Terminator");

        //Create the drive task.
        SelfAdjustingDriveTask driveTask = new SelfAdjustingDriveTask (powerMeasure * powerUnit.conversionFactor, useRangeSensorAdjustment);
        driveTask.executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR);

        //Allows us to know when we stopEasyTask.
        boolean reachedFinalDest = false;

        //Actual adjustment aspect of driving.
        while (!reachedFinalDest)
        {
            //Causes program termination.
            switch (sensorStopType)
            {
                case Distance:
                    //End early if this is pointless.
                    if (stopValue == 0)
                        break;

                    int powerSign = (int) (Math.signum (powerMeasure));
                    double currentDistance = (leftDrive.encoderMotor.getCurrentPosition () + rightDrive.encoderMotor.getCurrentPosition ()) / 2.0;
                    reachedFinalDest = stopValue * powerSign <= currentDistance * powerSign;

                    driveTerminationConsole.updateWith (
                            "Current encoder position = " + currentDistance,
                            "Stopping at position = " + stopValue
                    );

                    break;

                case Ultrasonic:
                    double currentRangeVal = frontRangeSensor.ultrasonicDistCM ();
                    reachedFinalDest = currentRangeVal <= stopValue;

                    driveTerminationConsole.updateWith (
                            "Current range sensor val = " + currentRangeVal,
                            "Stopping at range = " + stopValue
                    );

                    break;

                case BottomColorAlpha:
                    int currentBottomAlpha = bottomColorSensor.sensor.alpha ();
                    reachedFinalDest = currentBottomAlpha >= stopValue;

                    driveTerminationConsole.updateWith (
                            "Current bottom alpha = " + currentBottomAlpha,
                            "Stopping at alpha >= " + stopValue
                    );

                    break;
            }

            ProgramFlow.pauseForSingleFrame ();
        }

        //End the drive task
        driveTask.cancel (true);

        //Private console no longer required.
        driveTerminationConsole.destroy ();

        //Brake for 100 ms in order to make sure we have completely stopped.
        hardBrake (100);
    }

    //Used to turn to a specified heading, and returns the difference between the desired angle and the actual angle achieved.
    protected enum TurnMode {
        LEFT, RIGHT, BOTH
    }
    //This battery factor, when updated, remains updated for all future turns so that the robot does not have to start changing it again.
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        //Create a console to which output will be displayed.
        ConsoleManager.ProcessConsole turnConsole = new ConsoleManager.ProcessConsole ("Turning");

        //Set gyro desiredHeading.
        gyroscope.setDesiredHeading (desiredHeading);

        //Start the PID control for the drive motors.
        leftDrive.startPIDTask ();
        rightDrive.startPIDTask ();

        //Get the startTime so that we know when to end.
        long startTime = System.currentTimeMillis();

        //Adjust as fully as possible but not beyond the time limit.
        double offFromHeading;
        do
        {
            offFromHeading = gyroscope.getOffFromHeading ();

            //Verify that the heading that we thought was perfectly on point actually is on point.
            if (offFromHeading == 0)
            {
                //Brake for a moment
                hardBrake (200);

                //Verify the heading and break if it has been reached.
                if (gyroscope.getOffFromHeading () == 0)
                    break;
            }

            //Calculate turning speed.
            double turnPower = Math.signum(offFromHeading) * (Math.abs(offFromHeading) * 0.05 + .2);

            //Set clipped powers.
            if (mode != TurnMode.RIGHT)
                leftDrive.setRPS (-1 * Range.clip(turnPower, -1, 1));
            if (mode != TurnMode.LEFT)
                rightDrive.setRPS (Range.clip(turnPower, -1, 1));

            turnConsole.updateWith (
                    "Turning to " + desiredHeading + " degrees",
                    "Current heading is " + gyroscope.getValidGyroHeading (),
                    "Turn power is " + turnPower,
                    "Stopping if possible in " + (maxTime - (System.currentTimeMillis () - startTime) + "ms")
            );

            ProgramFlow.pauseForSingleFrame ();
        }
        while (System.currentTimeMillis() - startTime < maxTime || Math.abs(gyroscope.getOffFromHeading ()) >= 10);

        //Disable PID on the drive again.
        leftDrive.stopPIDTask ();
        rightDrive.stopPIDTask ();

        //Remove the console being used to output to the drivers.
        turnConsole.destroy ();

        //Brake to become fully stationary.
        hardBrake (100);
    }

    //Stops all drive motors and pauses for a moment.
    protected void hardBrake(long msDelay) throws InterruptedException
    {
        leftDrive.setRPS (0);
        rightDrive.setRPS (0);

        ProgramFlow.pauseForMS (msDelay);
    }

    /******** CUSTOM ACTIONS ********/
    protected void shootBallsIntoCenterVortex () throws InterruptedException
    {
        flywheels.startPIDTask ();
        harvester.startPIDTask ();

        flywheels.setRPS (19);
        ProgramFlow.pauseForMS (300);
        harvester.setRPS (5);
        ProgramFlow.pauseForMS (2200);
        flywheels.setRPS (0);
        harvester.setRPS (0);

        flywheels.stopPIDTask ();
        harvester.stopPIDTask ();
    }

    /******** INITIALIZATION ********/
    //Initialize everything required in autonomous that isn't initialized in MainRobotBase (sensors)
    @Override
    protected void initializeOpModeSpecificHardware() throws InterruptedException
    {
        //The range sensors are especially odd to initialize, and will often require a robot power cycle.
        ConsoleManager.outputNewSequentialLine ("Validating Front Range Sensor...");
        frontRangeSensor = new SmartRangeSensor (initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor"), 0x90);
        ConsoleManager.appendToLastSequentialLine (frontRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");

        ConsoleManager.outputNewSequentialLine ("Validating Side Range Sensor...");
        sideRangeSensor = new SmartRangeSensor (initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor"), 0x10);
        ConsoleManager.appendToLastSequentialLine (sideRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");

        //Initialize color sensors.
        ConsoleManager.outputNewSequentialLine ("Fetching Color Sensors...");
        option1ColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Option 1 Color Sensor"), 0x4c, false);
        option2ColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Option 2 Color Sensor"), 0x5c, false);
        bottomColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Bottom Color Sensor"), 0x3c, true);
        particleColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "particleColorSensor"), 0x6c, true);
        ConsoleManager.appendToLastSequentialLine ("OK!");

        //Initialize gyroscope.
        ConsoleManager.outputNewSequentialLine("Calibrating Gyroscope...");
        gyroscope = new SmartGyroSensor (initialize(GyroSensor.class, "Gyroscope")); //Calibrates immediately.
        ConsoleManager.appendToLastSequentialLine ("OK!");

        ConsoleManager.outputNewSequentialLine ("Initialization completed!");
    }


    /******** CHILD CLASS INHERITANCE ********/
    //All child classes should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;
}
