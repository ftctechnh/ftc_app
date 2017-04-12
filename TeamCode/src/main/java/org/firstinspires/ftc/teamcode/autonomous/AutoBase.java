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
        //Create a console to which output will be displayed.
        ConsoleManager.ProcessConsole turnConsole = new ConsoleManager.ProcessConsole ("Turning");

        //Start the PID control for the drive motors.
        leftDrive.pidUpdateTask.startEasyTask ();
        rightDrive.pidUpdateTask.startEasyTask ();

        this.desiredHeading = desiredHeading;

        //Get the startTime so that we know when to end.
        long startTime = System.currentTimeMillis();

        //Get the initial current heading to make sure that we aren't already at it.
        int currentHeading = gyroscope.getValidGyroHeading(desiredHeading);

        //Adjust as fully as possible but not beyond the time limit.
        while (System.currentTimeMillis() - startTime < maxTime || Math.abs(currentHeading - desiredHeading) >= 10)
        {
            //Verify that the heading that we thought was perfectly on point actually is on point.
            currentHeading = gyroscope.getValidGyroHeading(desiredHeading);
            if (currentHeading == this.desiredHeading)
            {
                //Brake for a moment
                hardBrake (200);

                //Verify the heading and break if it has been reached.
                currentHeading = gyroscope.getValidGyroHeading (desiredHeading);
                if (currentHeading == this.desiredHeading)
                    break;
            }

            //Turn at a speed proportional to the distance from the ideal heading.
            int thetaFromHeading = currentHeading - this.desiredHeading;

            //Calculate turning speed.
            double turnPower = Math.signum(thetaFromHeading) * (Math.abs(thetaFromHeading) * 0.05 + .2);

            //Set clipped powers.
            if (mode != TurnMode.RIGHT)
                leftDrive.setRPS (-1 * Range.clip(turnPower, -1, 1));
            if (mode != TurnMode.LEFT)
                rightDrive.setRPS (Range.clip(turnPower, -1, 1));

            turnConsole.updateWith (
                    "Turning to " + desiredHeading + " degrees",
                    "Current heading is " + currentHeading,
                    "Turn power is " + turnPower,
                    "Stopping if possible in " + (maxTime - (System.currentTimeMillis () - startTime) + "ms")
            );

            ProgramFlow.pauseForSingleFrame ();
        }

        //Disable PID on the drive again.
        leftDrive.pidUpdateTask.stopEasyTask ();
        rightDrive.pidUpdateTask.stopEasyTask ();

        //Remove the console being used to output to the drivers.
        turnConsole.destroy ();

        hardBrake (100);
    }

    /******** DRIVING METHOD ********/
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

    //Create both tasks.
    private double driveRPS;
    private boolean useRangeSensor;
    private final EasyAsyncTask
            gyroAdjustmentTask = new EasyAsyncTask ("Gyroscope Suggested Adjustments")
    {
        private ConsoleManager.ProcessConsole processConsole;

        @Override
        protected void taskToAccomplish () throws InterruptedException
        {
            while (true)
            {
                int offFromHeading = desiredHeading - gyroscope.getValidGyroHeading (desiredHeading);
                output = Math.signum (offFromHeading) * (Math.abs (offFromHeading) * 0.15);

                processConsole.updateWith (
                        "Gyro off from heading = " + offFromHeading,
                        "Suggested adjustment = " + output
                );

                ProgramFlow.pauseForMS (30);
            }
        }

        @Override
        protected void taskOnCompletion ()
        {
            processConsole.destroy ();

            output = 0;
        }
    },
            rangeSensorAdjustmentTask = new EasyAsyncTask ("Range Sensor Suggested Adjustments")
    {
        private ConsoleManager.ProcessConsole processConsole;

        @Override
        protected void taskToAccomplish () throws InterruptedException
        {
            //Create a new process console for the range sensor output.
            processConsole = new ConsoleManager.ProcessConsole ("Range Sensor Adjustment");

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

                processConsole.updateWith (
                        "Front range sensor dist = " + observedDistance,
                        "Suggested adjustment = " + output
                );

                ProgramFlow.pauseForMS (30);
            }
        }

        @Override
        protected void taskOnCompletion ()
        {
            processConsole.destroy ();
            output = 0;
        }
    },
            intelligentDriveTask = new EasyAsyncTask ("Intelligent Driving")
    {
        private ConsoleManager.ProcessConsole processConsole;

        @Override
        protected void taskToAccomplish () throws InterruptedException
        {
            //Reset encoders and start to run PID task
            leftDrive.resetEncoder ();
            rightDrive.resetEncoder ();
            leftDrive.pidUpdateTask.startEasyTask ();
            rightDrive.pidUpdateTask.startEasyTask ();

            //Start both adjustment tasks.
            gyroAdjustmentTask.startEasyTask ();
            if (useRangeSensor)
                rangeSensorAdjustmentTask.startEasyTask ();

            processConsole = ConsoleManager.getPrivateConsole ("Int. Drive Task");

            //Start to drive, adjusting based on the tasks above.
            double gyroAdjustmentFactor, rangeSensorAdjustmentFactor;
            while (true)
            {
                double adjustmentFactor = Math.signum (driveRPS) * gyroAdjustmentTask.output + rangeSensorAdjustmentTask.output;

                processConsole.updateWith (
                        "Total adjustment factor = " + adjustmentFactor,
                        "Left Drive PID conversion = " + leftDrive.getRPSConversionFactor (),
                        "Right Drive PID conversion = " + rightDrive.getRPSConversionFactor ()
                );

                leftDrive.setRPS (driveRPS * (1 + adjustmentFactor));
                rightDrive.setRPS (driveRPS * (1 - adjustmentFactor));

                ProgramFlow.pauseForMS (20);
            }
        }

        @Override
        protected void taskOnCompletion ()
        {
            ConsoleManager.outputNewSequentialLine ("Ended drive task.");
            processConsole.destroy ();

            //Stop PID tasks
            leftDrive.pidUpdateTask.stopEasyTask ();
            rightDrive.pidUpdateTask.stopEasyTask ();

            //Stop both sensor adjustment things.
            gyroAdjustmentTask.stopEasyTask ();
            if (useRangeSensor)
                rangeSensorAdjustmentTask.stopEasyTask ();
        }
    };

    //Creates a driving thread and then waits for the stopEasyTask signal from the sensors.
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure) throws InterruptedException
    {
        drive(sensorStopType, stopValue, powerUnit, powerMeasure, false);
    }
    protected void drive(SensorStopType sensorStopType, double stopValue, PowerUnits powerUnit, double powerMeasure, boolean useRangeSensorAdjustment) throws InterruptedException
    {
        //Create a new output console entirely for this process.
        ConsoleManager.ProcessConsole driveConsole = new ConsoleManager.ProcessConsole ("Drive Terminator");

        //Create the AsyncTask which will handle driving, with the other things encapsulated within it.
        driveRPS = powerMeasure * powerUnit.conversionFactor;
        useRangeSensor = useRangeSensorAdjustment;

        //Start the actual task.
        intelligentDriveTask.startEasyTask ();

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

                    driveConsole.updateWith (
                            "Current encoder position = " + currentDistance,
                            "Stopping at position = " + stopValue
                    );

                    break;

                case Ultrasonic:
                    double currentRangeVal = frontRangeSensor.getVALIDDistCM ();
                    reachedFinalDest = currentRangeVal <= stopValue;

                    driveConsole.updateWith (
                            "Current range sensor val = " + currentRangeVal,
                            "Ending at range = " + stopValue
                    );

                    break;

                case BottomColorAlpha:
                    int currentBottomAlpha = bottomColorSensor.sensor.alpha ();
                    reachedFinalDest = currentBottomAlpha >= stopValue;

                    driveConsole.updateWith (
                            "Current bottom alpha = " + currentBottomAlpha,
                            "Stopping at alpha >= " + stopValue
                    );

                    break;
            }

            ProgramFlow.pauseForSingleFrame ();
        }

        //Private console no longer required.
        driveConsole.destroy ();

        //Now that the main thread has realized that we have reached the final destination, stop the driving thread.
        intelligentDriveTask.stopEasyTask ();

        //Brake for 100 ms in order to make sure we have completely stopped.
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
        flywheels.pidUpdateTask.startEasyTask ();
        harvester.pidUpdateTask.startEasyTask ();

        flywheels.setRPS (19);
        ProgramFlow.pauseForMS (300);
        harvester.setRPS (5);
        ProgramFlow.pauseForMS (2200);
        flywheels.setRPS (0);
        harvester.setRPS (0);

        flywheels.pidUpdateTask.stopEasyTask ();
        harvester.pidUpdateTask.stopEasyTask ();
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
