/**
 * Initializes autonomous-specific hardware, with just sensors.  Also includes driving methods.
 */

package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTAsyncTask;
import org.firstinspires.ftc.teamcode.hardware.*;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

//For added simplicity while coding autonomous with the new FTC system. Utilizes inheritance and polymorphism.
public abstract class AutoBase extends MainRobotBase
{
    /******** SENSOR STUFF ********/

    /**** Range Sensors ****/
    protected NiFTRangeSensor frontRangeSensor, sideRangeSensor;

    /**** Color Sensors (3) ****/
    protected NiFTColorSensor option1ColorSensor, option2ColorSensor, bottomColorSensor, particleColorSensor;
    protected boolean option1Red, option2Red, option1Blue, option2Blue;

    protected void updateColorSensorStates ()
    {
        //Threshold is currently 2, but this could be changed.
        option1Blue = option1ColorSensor.sensor.blue () >= 3;
        option1Red = option1ColorSensor.sensor.red () >= 3; //Since blue has an annoying tendency to see red and blue color values.
        option2Blue = option2ColorSensor.sensor.blue () >= 3;
        option2Red = option2ColorSensor.sensor.red () >= 3;
    }

    /**** Gyro ****/
    protected NiFTGyroSensor gyroscope;

    /******** Robot Driving ********/
    private int getRobotEncoderPosition ()
    {
        return (int) ((leftDrive.ENCODER_MOTOR.getCurrentPosition () + rightDrive.ENCODER_MOTOR.getCurrentPosition ()) / 2.0);
    }

    protected enum PowerUnits
    {
        RevolutionsPerSecond (1), RevolutionsPerMinute (1.0 / 60);

        public final double conversionFactor;

        PowerUnits (double conversionFactor)
        {
            this.conversionFactor = conversionFactor;
        }
    }

    protected enum SensorStopType { Distance, Ultrasonic, BottomColorAlpha }

    //A new instance is instantiated upon starting a new drive.
    protected final class SelfAdjustingDriveTask extends NiFTAsyncTask
    {
        private final double RPS;
        private final boolean useRangeSensor;
        //Possible constructors.
        public SelfAdjustingDriveTask (double RPS)
        {
            this (RPS, false);
        }
        public SelfAdjustingDriveTask (double RPS, boolean useRangeSensor)
        {
            super("Self Adjusting Drive");

            this.RPS = RPS;
            this.useRangeSensor = useRangeSensor;
        }

        @Override
        protected void onBeginTask () throws InterruptedException
        {
            leftDrive.startPIDTask ();
            rightDrive.startPIDTask ();

            while (true)
            {
                double gyroAdjustment = gyroscope.getOffFromHeading () * 0.15 * Math.signum (RPS);
                double rangeAdjustment = useRangeSensor ? (15 - sideRangeSensor.validDistCM (15)) * 0.15 : 0;

                double totalAdjustment = gyroAdjustment + rangeAdjustment;

                double leftPower = RPS * (1 + totalAdjustment), rightPower = RPS * (1 - totalAdjustment);

                leftDrive.setRPS (leftPower);
                rightDrive.setRPS (rightPower);

                processConsole.updateWith (
                        "Gyro adjustment = " + gyroAdjustment,
                        "Range adjustment = " + rangeAdjustment,
                        "Left RPS = " + leftPower,
                        "Right RPS = " + rightPower
                );

                NiFTFlow.pauseForMS (50);
            }
        }

        @Override
        protected void onQuitTask()
        {
            leftDrive.stopPIDTask ();
            rightDrive.stopPIDTask ();
            leftDrive.setRPS(0);
            rightDrive.setRPS(0);
        }
    }

    //This method is the task which looks for the termination and returns to the original task once complete.
    protected void drive (SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure) throws InterruptedException
    {
        drive (sensorStopType, stopValue, powerUnit, powerMeasure, false);
    }
    protected void drive (SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure, boolean useRangeSensorAdjustment) throws InterruptedException
    {
        //Create a new output console entirely for this process.
        NiFTConsole.ProcessConsole driveTerminationConsole = new NiFTConsole.ProcessConsole ("Drive Terminator");

        //Create the drive task.
        SelfAdjustingDriveTask driveTask = new SelfAdjustingDriveTask (powerMeasure * powerUnit.conversionFactor, useRangeSensorAdjustment);
        driveTask.run();

        //Calculate values which will be used later but only need one calculation.
        int powerSign = (int) (Math.signum (powerMeasure));
        int desiredEncoderStopPosition = sensorStopType == SensorStopType.Distance ? getRobotEncoderPosition () + (int)(stopValue) * powerSign : 0;

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

                    int currentDistance = getRobotEncoderPosition ();
                    reachedFinalDest = desiredEncoderStopPosition * powerSign <= currentDistance * powerSign;

                    driveTerminationConsole.updateWith (
                            "Current encoder position = " + currentDistance,
                            "Stopping at position = " + stopValue
                    );

                    break;

                case Ultrasonic:
                    double currentRangeVal = frontRangeSensor.validDistCM (255, 300);
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

            NiFTFlow.pauseForSingleFrame ();
        }

        //End the drive task
        driveTask.stop();

        //Private console no longer required.
        driveTerminationConsole.destroy ();

        //Brake for 100 ms in order to make sure we have completely stopped.
        hardBrake (100);
    }

    //Used to turn to a specified heading, and returns the difference between the desired angle and the actual angle achieved.
    protected enum TurnMode { LEFT, RIGHT, BOTH }

    //This battery factor, when updated, remains updated for all future turns so that the robot does not have to start changing it again.
    protected void turnToHeading (int desiredHeading, TurnMode mode, long maxTime) throws InterruptedException
    {
        //Create a console to which output will be displayed.
        NiFTConsole.ProcessConsole turnConsole = new NiFTConsole.ProcessConsole ("Turning");

        //Set gyro desiredHeading.
        gyroscope.setDesiredHeading (desiredHeading);

        //Start the PID control for the drive motors.
        leftDrive.startPIDTask ();
        rightDrive.startPIDTask ();

        //Get the startTime so that we know when to end.
        long startTime = System.currentTimeMillis ();

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
            double turnPower = Math.signum (offFromHeading) * (Math.abs (offFromHeading) * 0.05 + .2);

            //Set clipped powers.
            if (mode != TurnMode.RIGHT)
                leftDrive.setRPS (-1 * Range.clip (turnPower, -1, 1));
            if (mode != TurnMode.LEFT)
                rightDrive.setRPS (Range.clip (turnPower, -1, 1));

            turnConsole.updateWith (
                    "Turning to " + desiredHeading + " degrees",
                    "Current heading is " + gyroscope.getValidGyroHeading (),
                    "Turn RPS is " + turnPower,
                    "Stopping if possible in " + (maxTime - (System.currentTimeMillis () - startTime) + "ms")
            );

            NiFTFlow.pauseForSingleFrame ();
        }
        while (System.currentTimeMillis () - startTime < maxTime || Math.abs (gyroscope.getOffFromHeading ()) >= 10);

        //Disable PID on the drive again.
        leftDrive.stopPIDTask ();
        rightDrive.stopPIDTask ();

        //Remove the console being used to output to the drivers.
        turnConsole.destroy ();

        //Brake to become fully stationary.
        hardBrake (100);
    }

    //Stops all drive motors and pauses for a moment.
    protected void hardBrake (long msDelay) throws InterruptedException
    {
        leftDrive.setRPS (0);
        rightDrive.setRPS (0);

        NiFTFlow.pauseForMS (msDelay);
    }

    /******** INITIALIZATION ********/
    //Initialize everything required in autonomous that isn't initialized in MainRobotBase (sensors)
    @Override
    protected void initializeOpModeSpecificHardware () throws InterruptedException
    {
        //The range sensors are especially odd to initialize, and will often require a robot RPS cycle.
        NiFTConsole.outputNewSequentialLine ("Validating Front Range Sensor...");
        frontRangeSensor = new NiFTRangeSensor ("Front Range Sensor", 0x90);
        NiFTConsole.appendToLastSequentialLine (frontRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");

        NiFTConsole.outputNewSequentialLine ("Validating Side Range Sensor...");
        sideRangeSensor = new NiFTRangeSensor ("Back Range Sensor", 0x10);
        NiFTConsole.appendToLastSequentialLine (sideRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");

        //Initialize color sensors.
        NiFTConsole.outputNewSequentialLine ("Fetching Color Sensors...");
        option1ColorSensor = new NiFTColorSensor ("Option 1 Color Sensor", 0x4c, false);
        option2ColorSensor = new NiFTColorSensor ("Option 2 Color Sensor", 0x5c, false);
        bottomColorSensor = new NiFTColorSensor ("Bottom Color Sensor", 0x3c, true);
        particleColorSensor = new NiFTColorSensor ("particleColorSensor", 0x6c, true);
        NiFTConsole.appendToLastSequentialLine ("OK!");

        //Initialize gyroscope.
        NiFTConsole.outputNewSequentialLine ("Calibrating Gyroscope...");
        gyroscope = new NiFTGyroSensor ("Gyroscope"); //Calibrates immediately.
        NiFTConsole.appendToLastSequentialLine ("OK!");

        NiFTConsole.outputNewSequentialLine ("Initialization completed!");
    }


    /******** CHILD CLASS INHERITANCE ********/
    //All child classes should have special instructions.
    protected abstract void driverStationSaysGO () throws InterruptedException;
}
