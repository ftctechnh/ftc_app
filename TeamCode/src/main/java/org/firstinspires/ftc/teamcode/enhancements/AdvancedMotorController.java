package org.firstinspires.ftc.teamcode.enhancements;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class AdvancedMotorController
{
    //TODO: Make thread-compliant

    //Only certain motors have encoders on them, so the linkedMotor object is implemented.
    public final DcMotor encoderMotor, linkedMotor;

    public enum MotorType
    {
        NeverRest40(1120), NeverRest20(1120), NeverRest3P7(45);

        public final int encoderTicksPerRevolution;
        MotorType(int encoderTicksPerRevolution)
        {
            this.encoderTicksPerRevolution = encoderTicksPerRevolution;
        }
    }
    public enum GearRatio
    {
        Two_To_One(2), One_to_One(1), One_to_Two(0.5);

        public final double ratio;

        GearRatio (double ratio)
        {
            this.ratio = ratio;
        }
    }
    public AdvancedMotorController (DcMotor encoderMotor, double initialRPSConversionFactor, GearRatio gearRatio, MotorType motorType)
    {
        this (encoderMotor, null, initialRPSConversionFactor, gearRatio, motorType);
    }
    public AdvancedMotorController (DcMotor encoderMotor, DcMotor linkedMotor, double initialRPSConversionFactor, GearRatio gearRatio, MotorType motorType)
    {
        this.encoderMotor = encoderMotor;
        this.linkedMotor = linkedMotor;

        this.rpsConversionFactor = initialRPSConversionFactor;

        this.encoderTicksPerWheelRevolution = gearRatio.ratio * motorType.encoderTicksPerRevolution;

        resetEncoder ();
    }
    //I'm purposefully avoiding putting all other parameters into other such methods, since it would prevent the final variables from being final.
    public AdvancedMotorController setMotorDirection(DcMotorSimple.Direction direction)
    {
        encoderMotor.setDirection (direction);
        if (linkedMotor != null)
            linkedMotor.setDirection (direction);

        return this;
    }

    /***** ENCODER MANAGEMENT *****/
    public void resetEncoder ()
    {
        boolean doneSuccessfully = false;
        long additionalTime = 0;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }

        doneSuccessfully = false;
        additionalTime = 0;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }

        doneSuccessfully = false;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }
    }

    /******* PID STUFF *********/
    private double desiredRPS = 0;

    //Initial conversion factor, will be changed a LOT through the course of the program.
    private double rpsConversionFactor = .25;
    public double getRPSConversionFactor() //Primarily for debugging.
    {
        return rpsConversionFactor;
    }

    /**
     * VERY Simplistic PID control which decreases or increases the rpsConversionFactor variable, thus changing the speed
     * at which the motor turns based on previous and current encoder positions.
     */
    //Stored for each run.
    private int previousMotorPosition;
    private long lastAdjustTime = 0;

    //Calculated in the constructor.
    private final double encoderTicksPerWheelRevolution;
    private double expectedTicksPerSecond;

    public void setRPS (double givenRPS)
    {
        //Will soon be modified by PID.
        desiredRPS = givenRPS;
        updateMotorPowers ();

        updateLastPosition ();

        expectedTicksPerSecond = encoderTicksPerWheelRevolution * desiredRPS;
    }

    private double expectedTicksSinceUpdate, actualTicksSinceUpdate;
    public double getExpectedTicksSinceUpdate()
    {
        return expectedTicksSinceUpdate;
    }
    public double getActualTicksSinceUpdate()
    {
        return actualTicksSinceUpdate;
    }

    private SimplisticThread pidUpdateThread = null;
    public void enablePeriodicPIDUpdates()
    {
        enablePeriodicPIDUpdates (50);
    }
    public void enablePeriodicPIDUpdates(long refreshDelay)
    {
        pidUpdateThread = new SimplisticThread (refreshDelay)
        {
            @Override
            public void actionPerUpdate ()
            {
                updateMotorPowerWithPID ();
            }
        };
    }
    public void disablePIDUpdates()
    {
        if (pidUpdateThread != null)
            pidUpdateThread.stop ();
    }
    public void resumePIDUpdates()
    {
        if (pidUpdateThread != null)
            pidUpdateThread.resume ();
    }

    public void updateMotorPowerWithPID ()
    {
        if (lastAdjustTime != 0)
        {
            expectedTicksSinceUpdate = expectedTicksPerSecond * ((System.currentTimeMillis () - lastAdjustTime) / 1000.0);

            actualTicksSinceUpdate = encoderMotor.getCurrentPosition () - previousMotorPosition;

            //Sensitivity is the coefficient below, and bounds are .5 and -.5 so that momentary errors don't result in crazy changes.
            rpsConversionFactor += Math.signum (desiredRPS) * Range.clip (((expectedTicksSinceUpdate - actualTicksSinceUpdate) * 0.0002), -.5, .5);

            updateMotorPowers ();
        }

        updateLastPosition ();
    }

    private void updateLastPosition()
    {
        previousMotorPosition = encoderMotor.getCurrentPosition ();
        lastAdjustTime = System.currentTimeMillis ();
    }

    private void updateMotorPowers()
    {
        //Set the initial power which the PID will soon modify.
        double desiredPower = Range.clip(desiredRPS * rpsConversionFactor, -1, 1);
        encoderMotor.setPower (desiredPower);
        if (linkedMotor != null)
            linkedMotor.setPower (desiredPower);
    }

    //Used rarely but useful when required.
    public void setDirectMotorPower(double power)
    {
        double actualPower = Range.clip(power, -1, 1);
        encoderMotor.setPower(actualPower);
        if (linkedMotor != null)
            linkedMotor.setPower (actualPower);
    }
}
