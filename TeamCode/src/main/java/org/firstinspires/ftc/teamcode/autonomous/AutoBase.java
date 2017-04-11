package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;
import org.firstinspires.ftc.teamcode.threading.EasyAsyncTask;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;
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
    private int desiredHeading = 0; //Massively important to maintaining stability through the drives.

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

        int currentHeading = gyroscope.getValidGyroHeading(desiredHeading);
        //Adjust as fully as possible but not beyond the time limit.
        while(System.currentTimeMillis() - startTime < maxTime || Math.abs(currentHeading - desiredHeading) >= 10)
        {
            //Verify that the heading that we thought was perfectly on point actually is on point.
            currentHeading = gyroscope.getValidGyroHeading(desiredHeading);
            if (currentHeading == this.desiredHeading)
            {
                hardBrake (200);
                currentHeading = gyroscope.getValidGyroHeading (desiredHeading);
                if (currentHeading == this.desiredHeading)
                    break;
            }

            //Turn at a speed proportional to the distance from the ideal heading.
            int thetaFromHeading = currentHeading - this.desiredHeading;

            double turnPower = Math.signum(thetaFromHeading) * (Math.abs(thetaFromHeading) * 0.05 + .2);

            //Set clipped powers.
            if (mode != TurnMode.RIGHT)
                leftDrive.setRPS (-1 * Range.clip(turnPower, -1, 1));
            if (mode != TurnMode.LEFT)
                rightDrive.setRPS (Range.clip(turnPower, -1, 1));

            ProgramFlow.pauseForSingleFrame ();
        }

        hardBrake (100);
    }

    /******** DRIVING METHOD ********/
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

    //Create both tasks.
    private final EasyAsyncTask
            gyroAdjustmentTask = new EasyAsyncTask ()
    {
        @Override
        protected void taskToAccomplish () throws InterruptedException
        {
            while (true)
            {
                int offFromHeading = desiredHeading - gyroscope.getValidGyroHeading (desiredHeading);
                output = Math.signum (offFromHeading) * (Math.abs (desiredHeading) * 0.15);

                ProgramFlow.pauseForMS (30);
            }
        }

        @Override
        protected void taskOnCompletion ()
        {
            output = 0;
        }
    },
            rangeSensorAdjustmentTask = new EasyAsyncTask ()
    {
        @Override
        protected void taskToAccomplish () throws InterruptedException
        {
            while (true)
            {
                double observedDistance = sideRangeSensor.getVALIDDistCM ();
                if (observedDistance >= 255)
                    output = 0;
                else
                {
                    double offFromDist = 13 - sideRangeSensor.getVALIDDistCM (); //Potentially takes a while to actually return a value.
                    output = Math.signum (offFromDist) * (Math.abs (offFromDist) * 0.15);
                }

                ProgramFlow.pauseForMS (30);
            }
        }

        @Override
        protected void taskOnCompletion ()
        {
            output = 0;
        }
    };

    //Creates a driving thread and then waits for the stopEasyTask signal from the sensors.
    protected void drive(final SensorStopType sensorStopType, final double stopValue, final PowerUnits powerUnit, final double powerMeasure) throws InterruptedException
    {
        drive(sensorStopType, stopValue, powerUnit, powerMeasure, false);
    }
    protected void drive(final SensorStopType sensorStopType, final double stopValue, final PowerUnits powerUnit, final double powerMeasure, final boolean useRangeSensorAdjustment) throws InterruptedException
    {
        //Create the AsyncTask which will handle driving, with the other things encapsulated within it.
        EasyAsyncTask createdDriveTask = new EasyAsyncTask ()
        {
            @Override
            protected void taskToAccomplish () throws InterruptedException
            {
                //Start PID task.
                ConsoleManager.outputNewLineToDrivers ("Started driving task!");

                //Run the task.//Set up both motors.
                leftDrive.resetEncoder ();
                rightDrive.resetEncoder ();

                leftDrive.pidUpdateTask.startEasyTask ();
                rightDrive.pidUpdateTask.startEasyTask ();

                //Start both adjustment tasks.
                gyroAdjustmentTask.startEasyTask ();
                if (useRangeSensorAdjustment)
                    rangeSensorAdjustmentTask.startEasyTask ();

                //Start to drive, adjusting based on the tasks above.
                while (true)
                {
                    double adjustmentFactor = Math.signum (powerMeasure) * (Double) (gyroAdjustmentTask.output) + (Double) (rangeSensorAdjustmentTask.output);
                    leftDrive.setRPS (powerMeasure * (1 + adjustmentFactor));
                    rightDrive.setRPS (powerMeasure * (1 - adjustmentFactor));

                    ProgramFlow.pauseForMS (20);
                }
            }

            @Override
            protected void taskOnCompletion ()
            {
                leftDrive.pidUpdateTask.startEasyTask ();
                rightDrive.pidUpdateTask.startEasyTask ();

                gyroAdjustmentTask.stopEasyTask ();
                if (useRangeSensorAdjustment)
                    rangeSensorAdjustmentTask.stopEasyTask ();
            }
        };
        createdDriveTask.startEasyTask ();

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
                    reachedFinalDest = stopValue * powerSign <= ((leftDrive.encoderMotor.getCurrentPosition () + rightDrive.encoderMotor.getCurrentPosition ()) / 2.0) * powerSign;
                    break;

                case Ultrasonic:
                    reachedFinalDest = frontRangeSensor.getVALIDDistCM () <= stopValue;
                    break;

                case BottomColorAlpha:
                    reachedFinalDest = bottomColorSensor.sensor.alpha () >= stopValue;
                    break;
            }

            ProgramFlow.pauseForSingleFrame ();
        }

        createdDriveTask.stopEasyTask ();

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
        flywheels.setRPS (3);
        ProgramFlow.pauseForMS (300);
        harvester.setRPS (-5);
        ProgramFlow.pauseForMS (2200);
        flywheels.setRPS (0);
        harvester.setRPS (0);
    }


    /******** INITIALIZATION ********/
    //Initialize everything required in autonomous that isn't initialized in MainRobotBase (sensors)
    @Override
    protected void initializeOpModeSpecificHardware() throws InterruptedException
    {
        //The range sensors are especially odd to initialize, and will often require a robot power cycle.
        ConsoleManager.outputNewLineToDrivers ("Validating Front Range Sensor...");
        frontRangeSensor = new SmartRangeSensor (initialize(ModernRoboticsI2cRangeSensor.class, "Front Range Sensor"), 0x90);
        ConsoleManager.appendToLastOutputtedLine (frontRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");

        ConsoleManager.outputNewLineToDrivers ("Validating Side Range Sensor...");
        sideRangeSensor = new SmartRangeSensor (initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor"), 0x10);
        ConsoleManager.appendToLastOutputtedLine (sideRangeSensor.returningValidOutput () ? "OK!" : "FAILED!");


        //Initialize color sensors.
        ConsoleManager.outputNewLineToDrivers ("Fetching Color Sensors...");
        option1ColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Option 1 Color Sensor"), 0x4c, true);
        option2ColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Option 2 Color Sensor"), 0x5c, true);
        bottomColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "Bottom Color Sensor"), 0x3c, true);
        particleColorSensor = new SmartColorSensor (initialize(ColorSensor.class, "particleColorSensor"), 0x6c, false);
        ConsoleManager.appendToLastOutputtedLine ("OK!");

        //Initialize gyroscope.
        ConsoleManager.outputNewLineToDrivers("Calibrating Gyroscope...");
        gyroscope = new SmartGyroSensor (initialize(GyroSensor.class, "Gyroscope")); //Calibrates immediately.
        ConsoleManager.appendToLastOutputtedLine ("OK!");

        ConsoleManager.outputNewLineToDrivers ("Initialization completed!");
    }


    /******** CHILD CLASS INHERITANCE ********/
    //All child classes should have special instructions.
    protected abstract void driverStationSaysGO() throws InterruptedException;
}
